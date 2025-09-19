#!/bin/bash

# AuditSystem DEV Environment Deployment Script
# This script automates the complete deployment of infrastructure and application

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
    exit 1
}

warning() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] INFO: $1${NC}"
}

# Check prerequisites
check_prerequisites() {
    log "Checking prerequisites..."
    
    # Check if Azure CLI is installed
    if ! command -v az &> /dev/null; then
        error "Azure CLI is not installed. Please install it first."
    fi
    
    # Check if Terraform is installed
    if ! command -v terraform &> /dev/null; then
        error "Terraform is not installed. Please install it first."
    fi
    
    # Check if logged in to Azure
    if ! az account show &> /dev/null; then
        error "Not logged in to Azure. Please run 'az login' first."
    fi
    
    # Check subscription
    CURRENT_SUB=$(az account show --query id --output tsv)
    EXPECTED_SUB="d4915ef0-d9e7-41cc-80ad-1a6a5ef0aa9d"
    
    if [ "$CURRENT_SUB" != "$EXPECTED_SUB" ]; then
        warning "Current subscription: $CURRENT_SUB"
        warning "Expected subscription: $EXPECTED_SUB"
        echo "Do you want to switch to the correct subscription? (y/n)"
        read -r response
        if [[ "$response" =~ ^[Yy]$ ]]; then
            az account set --subscription "$EXPECTED_SUB"
            log "Switched to subscription: $EXPECTED_SUB"
        else
            error "Please switch to the correct subscription and try again."
        fi
    fi
    
    log "Prerequisites check completed successfully!"
}

# Initialize Terraform
init_terraform() {
    log "Initializing Terraform..."
    
    # Copy example variables if tfvars doesn't exist
    if [ ! -f terraform.tfvars ]; then
        cp terraform.tfvars.example terraform.tfvars
        warning "Created terraform.tfvars from example. Please review and modify if needed."
        echo "Press Enter to continue or Ctrl+C to exit and modify terraform.tfvars"
        read -r
    fi
    
    terraform init
    log "Terraform initialized successfully!"
}

# Plan deployment
plan_deployment() {
    log "Creating deployment plan..."
    terraform plan -out=tfplan
    
    echo ""
    warning "Please review the above plan carefully."
    echo "Do you want to proceed with the deployment? (y/n)"
    read -r response
    if [[ ! "$response" =~ ^[Yy]$ ]]; then
        error "Deployment cancelled by user."
    fi
}

# Deploy infrastructure
deploy_infrastructure() {
    log "Deploying Azure infrastructure..."
    terraform apply tfplan
    
    log "Infrastructure deployment completed!"
    
    # Save outputs
    terraform output -json > deployment-outputs.json
    log "Deployment outputs saved to deployment-outputs.json"
}

# Display connection information
display_connection_info() {
    log "Deployment Summary:"
    echo ""
    
    # Get outputs
    VM_IP=$(terraform output -raw vm_public_ip)
    VM_USER=$(terraform output -raw vm_admin_username)
    MYSQL_HOST=$(terraform output -raw mysql_server_fqdn)
    MYSQL_USER=$(terraform output -raw mysql_admin_username)
    
    info "VM Connection:"
    echo "  SSH: ssh $VM_USER@$VM_IP"
    echo "  Username: $VM_USER"
    echo "  Password: (check terraform output vm_admin_password)"
    echo ""
    
    info "MySQL Connection:"
    echo "  Host: $MYSQL_HOST"
    echo "  Username: $MYSQL_USER"
    echo "  Password: (check terraform output mysql_admin_password)"
    echo ""
    
    info "Application URLs:"
    echo "  Frontend: http://$VM_IP"
    echo "  Nacos Console: http://$VM_IP:8848/nacos"
    echo "  API Gateway: http://$VM_IP:8200"
    echo ""
    
    info "Next Steps:"
    echo "1. SSH to the VM: ssh $VM_USER@$VM_IP"
    echo "2. Copy AuditSystemVM deployment package to the VM"
    echo "3. Modify MySQL connection strings to use Azure MySQL"
    echo "4. Run the AuditSystem deployment scripts"
    echo ""
    
    warning "Remember to save the passwords from terraform outputs!"
}

# Get sensitive outputs
get_passwords() {
    echo ""
    log "Retrieving sensitive information..."
    echo ""
    
    echo "VM Admin Password:"
    terraform output -raw vm_admin_password
    echo ""
    
    echo "MySQL Admin Password:"
    terraform output -raw mysql_admin_password
    echo ""
    
    warning "Please save these passwords securely!"
}

# Main deployment flow
main() {
    echo "=========================================="
    echo "  AuditSystem DEV Environment Deployment"
    echo "=========================================="
    echo ""
    
    check_prerequisites
    init_terraform
    plan_deployment
    deploy_infrastructure
    display_connection_info
    
    echo ""
    echo "Do you want to display the generated passwords? (y/n)"
    read -r response
    if [[ "$response" =~ ^[Yy]$ ]]; then
        get_passwords
    fi
    
    echo ""
    log "Deployment completed successfully!"
    info "Infrastructure is ready for AuditSystem deployment."
}

# Run main function
main "$@"
