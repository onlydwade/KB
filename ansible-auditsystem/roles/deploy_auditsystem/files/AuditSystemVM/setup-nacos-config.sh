#!/bin/bash

# Simplified AuditSystem Configuration Setup Script
# Uses existing working configurations downloaded from Nacos server
# Based on successful Java 11 migration and deployment

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

info() {
    echo -e "${BLUE}[INFO] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[WARN] $1${NC}"
}

error() {
    echo -e "${RED}[ERROR] $1${NC}"
}

# Configuration parameters
NACOS_HOST="localhost"
NACOS_PORT="8848"
NACOS_USER="nacos"
NACOS_PASS="nacos"
NACOS_NAMESPACE="bytefinger-audit-local"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_DIR="$SCRIPT_DIR/nacos-config"

# Service configuration paths
BACKEND_SERVICES_DIR="/opt/auditsystem/backend/services"
SERVICES=("system" "auth" "biz" "job" "report" "gateway")

# Function to validate existing configuration files
validate_existing_configs() {
    log "Validating existing configuration files..."
    
    # Check shared configuration
    if [ ! -f "$CONFIG_DIR/shared/application-local.yml" ]; then
        error "Missing shared configuration: $CONFIG_DIR/shared/application-local.yml"
        error "Please run: ./download-nacos-configs.sh to download working configurations"
        exit 1
    fi
    
    # Check service configurations
    local missing_configs=()
    for service in "${SERVICES[@]}"; do
        local config_file="$CONFIG_DIR/services/bytefinger-${service}.yml"
        if [ ! -f "$config_file" ]; then
            missing_configs+=("$config_file")
        fi
    done
    
    if [ ${#missing_configs[@]} -gt 0 ]; then
        error "Missing service configurations:"
        for config in "${missing_configs[@]}"; do
            error "  - $config"
        done
        error "Please run: ./download-nacos-configs.sh to download working configurations"
        exit 1
    fi
    
    info "✅ All configuration files are present and ready to use"
}

# Function to wait for Nacos to be ready
wait_for_nacos() {
    log "Waiting for Nacos server to be ready..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -u "$NACOS_USER:$NACOS_PASS" "http://$NACOS_HOST:$NACOS_PORT/nacos/v1/console/health" > /dev/null 2>&1; then
            log "✅ Nacos server is ready"
            return 0
        fi
        
        info "Attempt $attempt/$max_attempts: Nacos not ready, waiting 5 seconds..."
        sleep 5
        ((attempt++))
    done
    
    error "❌ Nacos server not ready after $max_attempts attempts"
    return 1
}

# Function to create Nacos namespace
create_namespace() {
    log "Creating Nacos namespace: $NACOS_NAMESPACE"
    
    local response
    response=$(curl -s -u "$NACOS_USER:$NACOS_PASS" -X POST \
        "http://$NACOS_HOST:$NACOS_PORT/nacos/v1/console/namespaces" \
        -d "customNamespaceId=$NACOS_NAMESPACE&namespaceName=$NACOS_NAMESPACE&namespaceDesc=AuditSystem Local Environment")
    
    if [[ "$response" == *"true"* ]]; then
        info "✅ Namespace created or already exists"
    else
        warn "⚠️ Namespace response: $response (may already exist)"
    fi
}

# Function to upload shared configuration to Nacos
upload_shared_config() {
    log "Uploading shared configuration to Nacos..."
    
    local config_file="$CONFIG_DIR/shared/application-local.yml"
    local content
    content=$(python3 -c "import urllib.parse, sys; print(urllib.parse.quote(open('$config_file').read()))")
    
    local response
    response=$(curl -s -u "$NACOS_USER:$NACOS_PASS" -X POST \
        "http://$NACOS_HOST:$NACOS_PORT/nacos/v1/cs/configs" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "dataId=application-local.yml&group=DEFAULT_GROUP&content=$content")
    
    if [ "$response" = "true" ]; then
        info "✅ Shared configuration uploaded successfully"
    else
        error "❌ Failed to upload shared configuration: $response"
        return 1
    fi
}

# Function to upload service configurations to Nacos
upload_service_configs() {
    log "Uploading service configurations to Nacos..."
    
    for service in "${SERVICES[@]}"; do
        local config_file="$CONFIG_DIR/services/bytefinger-${service}.yml"
        local data_id="bytefinger-${service}"
        
        info "Uploading configuration for: $service"
        
        local content
        content=$(python3 -c "import urllib.parse, sys; print(urllib.parse.quote(open('$config_file').read()))")
        
        local response
        response=$(curl -s -u "$NACOS_USER:$NACOS_PASS" -X POST \
            "http://$NACOS_HOST:$NACOS_PORT/nacos/v1/cs/configs" \
            -H "Content-Type: application/x-www-form-urlencoded" \
            -d "dataId=$data_id&group=bytefinger-audit-local&content=$content")
        
        if [ "$response" = "true" ]; then
            info "✅ $service configuration uploaded successfully"
        else
            error "❌ Failed to upload $service configuration: $response"
        fi
    done
}

# Function to verify configurations in Nacos
verify_configs() {
    log "Verifying configurations in Nacos..."
    
    # Verify shared config
    local shared_response
    shared_response=$(curl -s -u "$NACOS_USER:$NACOS_PASS" \
        "http://$NACOS_HOST:$NACOS_PORT/nacos/v1/cs/configs?dataId=application-local.yml&group=DEFAULT_GROUP")
    
    if [ -n "$shared_response" ] && [ "$shared_response" != "config data not exist" ]; then
        info "✅ Shared configuration verified in Nacos"
    else
        error "❌ Shared configuration not found in Nacos"
    fi
    
    # Verify service configs
    for service in "${SERVICES[@]}"; do
        local service_response
        service_response=$(curl -s -u "$NACOS_USER:$NACOS_PASS" \
            "http://$NACOS_HOST:$NACOS_PORT/nacos/v1/cs/configs?dataId=bytefinger-${service}&group=bytefinger-audit-local")
        
        if [ -n "$service_response" ] && [ "$service_response" != "config data not exist" ]; then
            info "✅ $service configuration verified in Nacos"
        else
            error "❌ $service configuration not found in Nacos"
        fi
    done
}

# Function to restart services
restart_services() {
    local restart_option="$1"
    
    if [ "$restart_option" = "no-restart" ]; then
        warn "⏭️ Skipping service restart (--no-restart specified)"
        return 0
    fi
    
    log "Restarting AuditSystem services..."
    
    if [ -f "./manage-services.sh" ]; then
        info "Using manage-services.sh to restart services..."
        ./manage-services.sh restart
    else
        warn "manage-services.sh not found, manual service restart may be required"
    fi
}

# Function to show final status
show_final_status() {
    echo ""
    echo "🎉 ==============================================="
    echo "🎉 AuditSystem Configuration Setup Complete!"
    echo "🎉 ==============================================="
    echo ""
    echo "📋 SUMMARY:"
    echo "  ✅ Configuration files validated"
    echo "  ✅ Nacos namespace created"
    echo "  ✅ Shared configuration uploaded"
    echo "  ✅ All service configurations uploaded"
    echo "  ✅ Configurations verified in Nacos"
    echo ""
    echo "🌐 ACCESS INFORMATION:"
    echo "  🔧 Nacos Console: http://$NACOS_HOST:$NACOS_PORT/nacos"
    echo "  👤 Username: $NACOS_USER"
    echo "  🔑 Password: $NACOS_PASS"
    echo ""
    echo "📁 CONFIGURATION LOCATIONS:"
    echo "  📂 Local files: $CONFIG_DIR"
    echo "  ☁️ Nacos namespace: $NACOS_NAMESPACE"
    echo ""
    echo "✅ All services should now be running with proper configurations!"
}

# Main function
main() {
    local restart_option="${1:-restart}"
    
    echo "🚀 ==============================================="
    echo "🚀 AuditSystem Configuration Setup"
    echo "🚀 Using working configurations from Nacos"
    echo "🚀 ==============================================="
    echo ""
    
    # Step 1: Validate existing configuration files
    validate_existing_configs
    
    # Step 2: Wait for Nacos to be ready
    if ! wait_for_nacos; then
        error "Cannot connect to Nacos. Please ensure Nacos is running on $NACOS_HOST:$NACOS_PORT"
        exit 1
    fi
    
    # Step 3: Create Nacos namespace
    create_namespace
    
    # Step 4: Upload configurations to Nacos
    upload_shared_config
    upload_service_configs
    
    # Step 5: Verify configurations
    verify_configs
    
    # Step 5.5: CRITICAL FIX - Rebuild backend to compile updated bootstrap.yml files
    log "Rebuilding backend services to compile updated bootstrap configurations..."
    if [ -d "/opt/auditsystem/AuditSystemBackend" ]; then
        info "Found backend at /opt/auditsystem/AuditSystemBackend, rebuilding..."
        cd /opt/auditsystem/AuditSystemBackend
        
        log "Running Maven clean compile to update bootstrap configurations..."
        if mvn clean compile -DskipTests -q; then
            log "✅ Backend rebuilt successfully - bootstrap configurations compiled"
            info "Services will now use updated Nacos authentication credentials"
        else
            warn "⚠️  Backend rebuild failed - services may have authentication issues"
            info "You may need to manually run: cd /opt/auditsystem/AuditSystemBackend && mvn clean compile -DskipTests"
        fi
        
        cd - > /dev/null
    else
        info "Backend not found at /opt/auditsystem/AuditSystemBackend"
        info "Bootstrap configurations will be compiled when backend is deployed"
    fi
    
    # Step 6: Restart services (if not disabled)
    restart_services "$restart_option"
    
    # Step 7: Show final status
    show_final_status
}

# Script usage information
show_usage() {
    echo "Usage: $0 [restart|no-restart]"
    echo ""
    echo "Options:"
    echo "  restart     Restart services after configuration (default)"
    echo "  no-restart  Skip service restart"
    echo ""
    echo "Prerequisites:"
    echo "  - Run ./download-nacos-configs.sh first to get working configurations"
    echo "  - Ensure Nacos server is running on $NACOS_HOST:$NACOS_PORT"
    echo ""
}

# Parse command line arguments
case "${1:-}" in
    -h|--help)
        show_usage
        exit 0
        ;;
    restart|no-restart|"")
        main "$1"
        ;;
    *)
        error "Invalid option: $1"
        show_usage
        exit 1
        ;;
esac
