#!/bin/bash

# Secure Azure Monitor Setup (Mac-side)
# This script runs on your Mac to create Azure resources and generate VM deployment files

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
}

info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] INFO: $1${NC}"
}

warning() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

# Function to detect existing workspace and set defaults
detect_existing_workspace() {
    if command -v az &> /dev/null && az account show &> /dev/null; then
        # Try to find existing workspace
        local existing_workspace=$(az monitor log-analytics workspace list --query "[0].{name:name, resourceGroup:resourceGroup, customerId:customerId, location:location}" 2>/dev/null)
        
        if [ -n "$existing_workspace" ] && [ "$existing_workspace" != "null" ]; then
            # Extract values using jq if available, otherwise use defaults
            if command -v jq &> /dev/null; then
                DETECTED_WORKSPACE=$(echo "$existing_workspace" | jq -r '.name // empty')
                DETECTED_RESOURCE_GROUP=$(echo "$existing_workspace" | jq -r '.resourceGroup // empty')
                DETECTED_WORKSPACE_ID=$(echo "$existing_workspace" | jq -r '.customerId // empty')
                DETECTED_LOCATION=$(echo "$existing_workspace" | jq -r '.location // empty')
            else
                # Fallback: try to parse without jq
                DETECTED_WORKSPACE=$(az monitor log-analytics workspace list --query "[0].name" -o tsv 2>/dev/null || echo "")
                DETECTED_RESOURCE_GROUP=$(az monitor log-analytics workspace list --query "[0].resourceGroup" -o tsv 2>/dev/null || echo "")
                DETECTED_WORKSPACE_ID=$(az monitor log-analytics workspace list --query "[0].customerId" -o tsv 2>/dev/null || echo "")
                DETECTED_LOCATION=$(az monitor log-analytics workspace list --query "[0].location" -o tsv 2>/dev/null || echo "")
            fi
        fi
    fi
}

# Detect existing workspace first
detect_existing_workspace

# Configuration (customize these for your environment)
RESOURCE_GROUP="${AZURE_RESOURCE_GROUP:-${DETECTED_RESOURCE_GROUP:-rg-eas-dev-audit-sys-001}}"
LOCATION="${AZURE_LOCATION:-${DETECTED_LOCATION:-eastasia}}"
VM_NAME="${AZURE_VM_NAME:-vmdevbkaudit-sys001}"
LOG_ANALYTICS_WORKSPACE="${LOG_ANALYTICS_WORKSPACE:-${DETECTED_WORKSPACE:-la-auditsystem-$(date +%Y%m%d)}}"
VM_IP="${VM_IP:-20.2.21.39}"  # Your VM's public IP
VM_PASSWORD="${VM_PASSWORD:-}"  # VM password (will prompt if not set)
VM_USER="${VM_USER:-azureuser}"  # VM username

# Store detected workspace ID for later use
WORKSPACE_ID="${WORKSPACE_ID:-${DETECTED_WORKSPACE_ID:-}}"

# Output directory for VM deployment files
OUTPUT_DIR="."

# Function to check and install sshpass if needed
check_sshpass() {
    if ! command -v sshpass &> /dev/null; then
        warning "sshpass is not installed. Installing via Homebrew..."
        if command -v brew &> /dev/null; then
            brew install hudochenkov/sshpass/sshpass
            log "sshpass installed successfully"
        else
            error "Homebrew is not installed. Please install sshpass manually:"
            error "  1. Install Homebrew: /bin/bash -c \"\$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)\""
            error "  2. Install sshpass: brew install hudochenkov/sshpass/sshpass"
            exit 1
        fi
    fi
}

# Function to get VM password if not set
get_vm_password() {
    if [ -z "$VM_PASSWORD" ]; then
        echo -n "Enter password for $VM_USER@$VM_IP: "
        read -s VM_PASSWORD
        echo
        
        if [ -z "$VM_PASSWORD" ]; then
            error "Password cannot be empty"
            exit 1
        fi
    fi
}

# Function to prompt for configuration with defaults
prompt_for_configuration() {
    echo
    info "=== Azure Monitor Configuration ==="
    echo
    
    # Show detected values if any
    if [ -n "$DETECTED_WORKSPACE" ]; then
        info "✅ Detected existing workspace: $DETECTED_WORKSPACE"
        info "✅ Resource Group: $DETECTED_RESOURCE_GROUP"
        info "✅ Location: $DETECTED_LOCATION"
        info "✅ Workspace ID: $DETECTED_WORKSPACE_ID"
        echo
    fi
    
    # Prompt for Resource Group
    echo -n "Resource Group [$RESOURCE_GROUP]: "
    read -r input_rg
    if [ -n "$input_rg" ]; then
        RESOURCE_GROUP="$input_rg"
    fi
    
    # Prompt for Location
    echo -n "Azure Location [$LOCATION]: "
    read -r input_location
    if [ -n "$input_location" ]; then
        LOCATION="$input_location"
    fi
    
    # Prompt for Workspace Name
    echo -n "Log Analytics Workspace Name [$LOG_ANALYTICS_WORKSPACE]: "
    read -r input_workspace
    if [ -n "$input_workspace" ]; then
        LOG_ANALYTICS_WORKSPACE="$input_workspace"
    fi
    
    # Prompt for VM details
    echo -n "VM IP Address [$VM_IP]: "
    read -r input_vm_ip
    if [ -n "$input_vm_ip" ]; then
        VM_IP="$input_vm_ip"
    fi
    
    echo -n "VM Username [$VM_USER]: "
    read -r input_vm_user
    if [ -n "$input_vm_user" ]; then
        VM_USER="$input_vm_user"
    fi
    
    echo
    info "=== Configuration Summary ==="
    info "• Resource Group: $RESOURCE_GROUP"
    info "• Location: $LOCATION"
    info "• Workspace: $LOG_ANALYTICS_WORKSPACE"
    info "• VM IP: $VM_IP"
    info "• VM User: $VM_USER"
    echo
    
    echo -n "Proceed with this configuration? (Y/n): "
    read -r confirm
    if [[ "$confirm" =~ ^[Nn]$ ]]; then
        echo "Configuration cancelled."
        exit 1
    fi
}

# Function to test VM connectivity
test_vm_connection() {
    log "Testing VM connectivity to $VM_USER@$VM_IP..."
    
    if sshpass -p "$VM_PASSWORD" ssh -o ConnectTimeout=10 -o StrictHostKeyChecking=no "$VM_USER@$VM_IP" "echo 'Connection successful'" > /dev/null 2>&1; then
        log "✅ VM connection test successful"
    else
        error "❌ Failed to connect to VM. Please check:"
        error "  - VM IP address: $VM_IP"
        error "  - Username: $VM_USER"
        error "  - Password"
        error "  - VM is running and accessible"
        exit 1
    fi
}

# Function to check if Azure CLI is installed and configured
check_azure_cli() {
    if ! command -v az &> /dev/null; then
        error "Azure CLI is not installed. Please install it first:"
        info "brew install azure-cli"
        exit 1
    fi
    
    if ! az account show &> /dev/null; then
        error "Azure CLI is not logged in. Please run: az login"
        exit 1
    fi
    
    local subscription_name=$(az account show --query name -o tsv)
    local subscription_id=$(az account show --query id -o tsv)
    
    log "Azure CLI is configured and authenticated"
    info "Subscription: $subscription_name ($subscription_id)"
}

# Function to create resource group if it doesn't exist
create_resource_group() {
    log "Checking/Creating resource group: $RESOURCE_GROUP"
    
    if az group show --name "$RESOURCE_GROUP" &>/dev/null; then
        info "Resource group '$RESOURCE_GROUP' already exists"
    else
        az group create \
            --name "$RESOURCE_GROUP" \
            --location "$LOCATION" \
            --tags Environment=Production Application=AuditSystem
        
        log "Resource group '$RESOURCE_GROUP' created successfully"
    fi
}

# Function to create Log Analytics workspace (FREE TIER)
create_log_analytics_workspace() {
    log "Creating Log Analytics workspace (FREE tier): $LOG_ANALYTICS_WORKSPACE"
    
    # Check if workspace already exists
    if az monitor log-analytics workspace show --resource-group "$RESOURCE_GROUP" --workspace-name "$LOG_ANALYTICS_WORKSPACE" &>/dev/null; then
        info "Log Analytics workspace '$LOG_ANALYTICS_WORKSPACE' already exists"
    else
        az monitor log-analytics workspace create \
            --resource-group "$RESOURCE_GROUP" \
            --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
            --location "$LOCATION" \
            --sku PerGB2018 \
            --retention-time 30 \
            --tags Environment=Production Application=AuditSystem CreatedBy=SecureSetup
        
        log "Log Analytics workspace created successfully (FREE tier with 30-day retention)"
    fi
    
    # Get workspace details
    local workspace_id=$(az monitor log-analytics workspace show \
        --resource-group "$RESOURCE_GROUP" \
        --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
        --query customerId -o tsv)
    
    local workspace_key=$(az monitor log-analytics workspace get-shared-keys \
        --resource-group "$RESOURCE_GROUP" \
        --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
        --query primarySharedKey -o tsv)
    
    local workspace_resource_id=$(az monitor log-analytics workspace show \
        --resource-group "$RESOURCE_GROUP" \
        --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
        --query id -o tsv)
    
    info "✅ Workspace ID: $workspace_id"
    info "✅ Resource ID: $workspace_resource_id"
    
    # Store details for VM deployment
    WORKSPACE_ID="$workspace_id"
    WORKSPACE_KEY="$workspace_key"
    WORKSPACE_RESOURCE_ID="$workspace_resource_id"
}

# Function to create VM deployment files (secure - no credentials exposed)
generate_vm_deployment_files() {
    log "Generating secure VM deployment files..."
    
    mkdir -p "$OUTPUT_DIR"
    
    # 1. Create VM-side health monitoring script (no Azure credentials needed)
    cat > "$OUTPUT_DIR/vm-health-monitor.sh" << 'EOF'
#!/bin/bash

# VM Health Monitor (No Azure credentials required)
# This script generates health data for Azure Monitor Agent pickup

set -e

LOG_FILE="/var/log/azure-monitor-health.json"
TEMP_FILE="/tmp/health-check-$(date +%s).json"

# Function to check service health (same as your existing logic)
check_service_health() {
    local service=$1
    local start_time=$(date +%s.%3N)
    local status="OK"
    local error_message=""
    
    case $service in
        "mysql")
            # Check if MySQL process is running and port is open
            if systemctl is-active --quiet mysql && nc -z localhost 3306 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="MySQL service not running or port 3306 not accessible"
            fi
            ;;
        "redis-server")
            # Check if Redis process is running and port is open
            if systemctl is-active --quiet redis-server && nc -z localhost 6379 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Redis service not running or port 6379 not accessible"
            fi
            ;;
        "rabbitmq-server")
            # Check if RabbitMQ process is running and port is open
            if systemctl is-active --quiet rabbitmq-server && nc -z localhost 5672 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="RabbitMQ service not running or port 5672 not accessible"
            fi
            ;;
        "nacos")
            # Check Nacos with timeout and better error handling
            if timeout 5 curl -f -s http://localhost:8848/nacos/ > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Nacos web interface not responding on port 8848"
            fi
            ;;
        "auditsystem-gateway")
            # Check Gateway service with timeout
            if timeout 5 curl -f -s http://localhost:8200/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Gateway health endpoint not responding on port 8200"
            fi
            ;;
        "auditsystem-auth")
            # Check Auth service with timeout
            if timeout 5 curl -f -s http://localhost:8300/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Auth service health endpoint not responding on port 8300"
            fi
            ;;
        "auditsystem-system")
            # Check System service with timeout
            if timeout 5 curl -f -s http://localhost:8400/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="System service health endpoint not responding on port 8400"
            fi
            ;;
        "auditsystem-biz")
            # Check Business service with timeout
            if timeout 5 curl -f -s http://localhost:8500/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Business service health endpoint not responding on port 8500"
            fi
            ;;
        "auditsystem-job")
            # Check Job service with timeout
            if timeout 5 curl -f -s http://localhost:8600/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Job service health endpoint not responding on port 8600"
            fi
            ;;
        "auditsystem-report")
            # Check Report service with timeout
            if timeout 5 curl -f -s http://localhost:8700/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Report service health endpoint not responding on port 8700"
            fi
            ;;
        "nginx")
            # Check Nginx with timeout and fallback to port check
            if timeout 5 curl -f -s http://localhost/health > /dev/null 2>&1 || timeout 5 curl -f -s http://localhost > /dev/null 2>&1 || nc -z localhost 80 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Nginx not responding on port 80"
            fi
            ;;
    esac
    
    local end_time=$(date +%s.%3N)
    local response_time_ms=$(echo "($end_time - $start_time) * 1000" | bc | cut -d. -f1)
    
    cat << EOJSON
{
    "service": "$service",
    "status": "$status",
    "response_time_ms": $response_time_ms,
    "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)",
    "error_message": "$error_message"
}
EOJSON
}

# Function to get system metrics
get_system_metrics() {
    # Get CPU usage with fallback methods
    local cpu_usage=0
    if command -v top >/dev/null 2>&1; then
        cpu_usage=$(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | awk -F'%' '{print $1}' | tr -d ' ' 2>/dev/null || echo "0")
    elif [ -f /proc/loadavg ]; then
        # Alternative: use load average as CPU indicator
        cpu_usage=$(awk '{print $1*10}' /proc/loadavg 2>/dev/null || echo "0")
    fi
    
    # Get memory usage with error handling
    local memory_info=0
    if command -v free >/dev/null 2>&1; then
        memory_info=$(free -m | awk 'NR==2{printf "%.2f", $3*100/$2 }' 2>/dev/null || echo "0")
    elif [ -f /proc/meminfo ]; then
        # Alternative method using /proc/meminfo
        local total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2}' 2>/dev/null || echo "1")
        local free_mem=$(grep MemAvailable /proc/meminfo | awk '{print $2}' 2>/dev/null || echo "$total_mem")
        memory_info=$(echo "scale=2; (($total_mem - $free_mem) * 100) / $total_mem" | bc -l 2>/dev/null || echo "0")
    fi
    
    # Get disk usage with error handling
    local disk_usage=0
    if command -v df >/dev/null 2>&1; then
        disk_usage=$(df -h / | awk 'NR==2 {print $5}' | sed 's/%//g' 2>/dev/null || echo "0")
    fi
    
    # Get load average with error handling
    local load_avg=0
    if command -v uptime >/dev/null 2>&1; then
        load_avg=$(uptime | awk -F'load average:' '{print $2}' | awk '{print $1}' | sed 's/,//g' | tr -d ' ' 2>/dev/null || echo "0")
    elif [ -f /proc/loadavg ]; then
        load_avg=$(awk '{print $1}' /proc/loadavg 2>/dev/null || echo "0")
    fi
    
    # Ensure all values are numeric
    cpu_usage=${cpu_usage//[^0-9.]/}
    memory_info=${memory_info//[^0-9.]/}
    disk_usage=${disk_usage//[^0-9]/}
    load_avg=${load_avg//[^0-9.]/}
    
    # Set defaults if empty
    cpu_usage=${cpu_usage:-0}
    memory_info=${memory_info:-0}
    disk_usage=${disk_usage:-0}
    load_avg=${load_avg:-0}
    
    cat << EOJSON
{
    "cpu_usage_percent": $cpu_usage,
    "memory_usage_percent": $memory_info,
    "disk_usage_percent": $disk_usage,
    "load_average_1min": $load_avg,
    "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)"
}
EOJSON
}

# Main health check function
generate_health_report() {
    local services=("mysql" "redis-server" "rabbitmq-server" "nacos" "auditsystem-gateway" "auditsystem-auth" "auditsystem-system" "auditsystem-biz" "auditsystem-job" "auditsystem-report" "nginx")
    
    # Add error handling and logging
    exec 2>/tmp/health-check-error.log
    
    echo "{"
    echo "  \"timestamp\": \"$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)\","
    echo "  \"hostname\": \"$(hostname)\","
    echo "  \"services\": ["
    
    local first=true
    local failed_services=0
    local total_services=${#services[@]}
    
    for service in "${services[@]}"; do
        if [ "$first" = true ]; then
            first=false
        else
            echo ","
        fi
        
        # Capture service health with error handling
        local service_health
        if service_health=$(check_service_health "$service" 2>/dev/null); then
            echo "    $service_health"
            # Check if this service failed
            if echo "$service_health" | grep -q '"status": "FAIL"'; then
                ((failed_services++))
            fi
        else
            # If check_service_health fails completely, create a failure entry
            ((failed_services++))
            cat << EOJSON
    {
        "service": "$service",
        "status": "FAIL",
        "response_time_ms": 0,
        "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)",
        "error_message": "Health check function failed"
    }
EOJSON
        fi
    done
    
    echo "  ],"
    
    # Get system metrics with error handling
    local system_metrics
    if system_metrics=$(get_system_metrics 2>/dev/null); then
        echo "  \"system_metrics\": $system_metrics,"
    else
        echo "  \"system_metrics\": {"
        echo "    \"cpu_usage_percent\": 0,"
        echo "    \"memory_usage_percent\": 0,"
        echo "    \"disk_usage_percent\": 0,"
        echo "    \"load_average_1min\": 0,"
        echo "    \"timestamp\": \"$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)\","
        echo "    \"error\": \"Failed to collect system metrics\""
        echo "  },"
    fi
    
    local health_percentage=$(echo "scale=2; ($total_services - $failed_services) * 100 / $total_services" | bc -l 2>/dev/null || echo "0")
    
    echo "  \"overall_health\": {"
    echo "    \"status\": \"$([ $failed_services -eq 0 ] && echo "HEALTHY" || echo "DEGRADED")\","
    echo "    \"healthy_services\": $(($total_services - $failed_services)),"
    echo "    \"total_services\": $total_services,"
    echo "    \"health_percentage\": $health_percentage"
    echo "  }"
    echo "}"
}

# Execute based on command line argument
case "$1" in
    "--check")
        # One-time health check
        generate_health_report
        ;;
    "--daemon")
        # Continuous monitoring (writes to log file for Azure Monitor Agent)
        echo "[$(date)] Starting health monitoring daemon..."
        
        # Ensure log directory exists
        sudo mkdir -p /var/log
        sudo touch "$LOG_FILE"
        sudo chown azureuser:azureuser "$LOG_FILE"
        
        # Main monitoring loop with error recovery
        while true; do
            echo "[$(date)] Running health check..."
            
            # Generate health report with error handling
            if generate_health_report > "$TEMP_FILE" 2>&1; then
                if [ -s "$TEMP_FILE" ] && grep -q "timestamp" "$TEMP_FILE"; then
                    # Valid JSON file, append to log file
                    cat "$TEMP_FILE" >> "$LOG_FILE"
                    echo "" >> "$LOG_FILE"  # Add newline separator
                    
                    # Keep log file size manageable (last 1000 lines)
                    if [ -f "$LOG_FILE" ]; then
                        tail -1000 "$LOG_FILE" > "$TEMP_FILE.tmp" && mv "$TEMP_FILE.tmp" "$LOG_FILE"
                    fi
                    
                    echo "[$(date)] ✅ Health check completed and logged"
                else
                    echo "[$(date)] ⚠️  WARNING: Invalid health report generated, contents:"
                    cat "$TEMP_FILE" | head -5
                    echo "[$(date)] Continuing with next cycle..."
                fi
            else
                echo "[$(date)] ❌ ERROR: Health report generation failed"
                
                # Log the error for debugging
                cat << EOERROR >> "$LOG_FILE"
{
    "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)",
    "hostname": "$(hostname)",
    "error": "Health check generation failed",
    "details": "$(cat $TEMP_FILE 2>/dev/null | head -3 | tr '\n' ' ')"
}

EOERROR
            fi
            
            # Clean up temp file
            [ -f "$TEMP_FILE" ] && rm -f "$TEMP_FILE"
            
            echo "[$(date)] Sleeping for 5 minutes..."
            sleep 300  # 5 minutes
        done
        ;;
    "--install")
        # Install as systemd service
        echo "Installing health monitoring service..."
        
        # Create systemd service
        sudo tee /etc/systemd/system/azure-health-monitor.service > /dev/null << 'EOSERVICE'
[Unit]
Description=Azure Health Monitor for AuditSystem
After=network.target mysql.service nacos.service

[Service]
Type=simple
User=azureuser
Group=azureuser
WorkingDirectory=/home/azureuser
ExecStart=/home/azureuser/vm-health-monitor.sh --daemon
Restart=always
RestartSec=30
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOSERVICE

        sudo systemctl daemon-reload
        sudo systemctl enable azure-health-monitor.service
        echo "✅ Service installed. Start with: sudo systemctl start azure-health-monitor"
        ;;
    *)
        echo "Usage: $0 [--check|--daemon|--install]"
        echo "  --check    Run one-time health check"
        echo "  --daemon   Run continuous monitoring"
        echo "  --install  Install as systemd service"
        ;;
esac
EOF

    # 2. Create Azure Monitor Agent configuration script
    # Get the workspace key if not already set
    if [ -z "$WORKSPACE_KEY" ] && [ -n "$WORKSPACE_ID" ] && [ -n "$LOG_ANALYTICS_WORKSPACE" ]; then
        log "Getting workspace key for deployment..."
        WORKSPACE_KEY=$(az monitor log-analytics workspace get-shared-keys \
            --resource-group "$RESOURCE_GROUP" \
            --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
            --query primarySharedKey -o tsv 2>/dev/null)
    fi
    
    cat > "$OUTPUT_DIR/configure-azure-monitor-agent.sh" << EOF
#!/bin/bash

# Configure Azure Monitor Agent on VM
# This script will be run on the VM to configure the Azure Monitor Agent

set -e

# Azure Monitor Agent configuration (values will be provided during deployment)
WORKSPACE_ID="$WORKSPACE_ID"
WORKSPACE_KEY="$WORKSPACE_KEY"

echo "Configuring Azure Monitor Agent..."

# Install required packages
sudo apt-get update
sudo apt-get install -y curl wget jq bc netcat-openbsd coreutils

# Make health monitoring script executable
chmod +x /home/azureuser/vm-health-monitor.sh

# Download and install Azure Monitor Agent
echo "Installing Azure Monitor Agent..."
wget https://raw.githubusercontent.com/Microsoft/OMS-Agent-for-Linux/master/installer/scripts/onboard_agent.sh -O /tmp/onboard_agent.sh
sudo sh /tmp/onboard_agent.sh -w "$WORKSPACE_ID" -s "$WORKSPACE_KEY"

# Create custom log configuration for our health data
echo "Configuring custom log collection..."
sudo tee /etc/opt/microsoft/omsagent/$WORKSPACE_ID/conf/omsagent.d/auditsystem-health.conf > /dev/null << 'EOFCONFIG'
<source>
  type tail
  path /var/log/azure-monitor-health.json
  pos_file /var/opt/microsoft/omsagent/$WORKSPACE_ID/state/auditsystem-health.log.pos
  tag oms.api.AuditSystemHealth
  format json
  read_from_head true
</source>

<filter oms.api.AuditSystemHealth>
  type filter_json
  json_key_name log
</filter>

<match oms.api.AuditSystemHealth>
  type out_oms_api
  log_level info
  buffer_chunk_limit 5m
  buffer_type file
  buffer_path /var/opt/microsoft/omsagent/$WORKSPACE_ID/state/out_oms_api_auditsystem*.buffer
  buffer_queue_limit 10
  flush_interval 20s
  retry_limit 10
  retry_wait 30s
  max_retry_wait 300s
</match>
EOFCONFIG

# Restart OMS Agent to pick up new configuration
sudo systemctl restart omsagent-$WORKSPACE_ID

# Install and start our health monitoring service
/home/azureuser/vm-health-monitor.sh --install
sudo systemctl start azure-health-monitor

echo "✅ Azure Monitor Agent configured successfully!"
echo "✅ Health monitoring service started"
echo ""
echo "To check status:"
echo "  sudo systemctl status azure-health-monitor"
echo "  sudo systemctl status omsagent-$WORKSPACE_ID"
echo ""
echo "To view logs:"
echo "  tail -f /var/log/azure-monitor-health.json"
echo "  sudo journalctl -u azure-health-monitor -f"
EOF

    # 3. Create deployment instructions
    cat > "$OUTPUT_DIR/DEPLOYMENT_INSTRUCTIONS.md" << EOF
# Azure Monitor Deployment Instructions

## Overview
Your Azure Log Analytics workspace has been created successfully. Follow these steps to configure monitoring on your VM.

## Azure Resources Created
- **Resource Group**: $RESOURCE_GROUP
- **Log Analytics Workspace**: $LOG_ANALYTICS_WORKSPACE
- **Workspace ID**: $WORKSPACE_ID
- **Location**: $LOCATION

## VM Deployment Steps

### 1. Copy Files to VM
Copy these files to your VM using sshpass:
\`\`\`bash
# If sshpass is not installed: brew install hudochenkov/sshpass/sshpass
sshpass -p 'YOUR_VM_PASSWORD' scp vm-health-monitor.sh $VM_USER@$VM_IP:/home/azureuser/
sshpass -p 'YOUR_VM_PASSWORD' scp configure-azure-monitor-agent.sh $VM_USER@$VM_IP:/home/azureuser/
\`\`\`

### 2. Run Configuration on VM
SSH to your VM and run:
\`\`\`bash
sshpass -p 'YOUR_VM_PASSWORD' ssh $VM_USER@$VM_IP
chmod +x configure-azure-monitor-agent.sh
sudo ./configure-azure-monitor-agent.sh
\`\`\`

### 3. Verify Installation
Check that services are running:
\`\`\`bash
sudo systemctl status azure-health-monitor
sudo systemctl status omsagent-$WORKSPACE_ID
\`\`\`

### 4. Test Health Monitoring
Run a test:
\`\`\`bash
./vm-health-monitor.sh --check
\`\`\`

## Viewing Data in Azure

### 1. Access Log Analytics
Visit: https://portal.azure.com
Navigate to: Resource Groups > $RESOURCE_GROUP > $LOG_ANALYTICS_WORKSPACE

### 2. Query Your Data
Use these KQL queries to view your health data:

#### View Recent Health Checks
\`\`\`kql
AuditSystemHealth_CL
| where TimeGenerated > ago(1h)
| order by TimeGenerated desc
| take 50
\`\`\`

#### Service Health Summary
\`\`\`kql
AuditSystemHealth_CL
| where TimeGenerated > ago(1h)
| extend ServiceName = tostring(services_s)
| extend Status = tostring(services_status_s)
| summarize 
    HealthyCount = countif(Status == "OK"),
    FailureCount = countif(Status == "FAIL"),
    TotalChecks = count()
    by ServiceName
| extend HealthPercentage = (HealthyCount * 100.0) / TotalChecks
| order by HealthPercentage asc
\`\`\`

#### System Performance Over Time
\`\`\`kql
AuditSystemHealth_CL
| where TimeGenerated > ago(6h)
| extend CPUUsage = todouble(system_metrics_cpu_usage_percent_d)
| extend MemoryUsage = todouble(system_metrics_memory_usage_percent_d)
| extend DiskUsage = todouble(system_metrics_disk_usage_percent_d)
| summarize 
    AvgCPU = avg(CPUUsage),
    AvgMemory = avg(MemoryUsage),
    AvgDisk = avg(DiskUsage)
    by bin(TimeGenerated, 10m)
| order by TimeGenerated desc
| render timechart
\`\`\`

## Next Steps (Optional - Can be done later)
1. Set up Application Insights for detailed application monitoring
2. Create custom alerts for service failures
3. Build custom dashboards
4. Configure email/SMS notifications

## Troubleshooting
If data isn't appearing in Log Analytics:
1. Check agent status: \`sudo systemctl status omsagent-$WORKSPACE_ID\`
2. Check agent logs: \`sudo tail -f /var/opt/microsoft/omsagent/$WORKSPACE_ID/log/omsagent.log\`
3. Verify health monitor: \`sudo systemctl status azure-health-monitor\`
4. Check log file: \`tail -f /var/log/azure-monitor-health.json\`

## Cost Information
- Log Analytics (first 5GB/month): **FREE**
- Data ingestion (estimated): ~10-20MB/month for this setup
- Retention: 30 days (FREE tier)
- **Total estimated cost: $0.00/month** (within free limits)
EOF

    # Make scripts executable
    chmod +x "$OUTPUT_DIR/vm-health-monitor.sh"
    chmod +x "$OUTPUT_DIR/configure-azure-monitor-agent.sh"
    
    log "✅ VM deployment files generated in: $OUTPUT_DIR"
}

# Function to deploy files to VM using sshpass
deploy_to_vm() {
    log "Deploying Azure Monitor files to VM..."
    
    check_sshpass
    get_vm_password
    test_vm_connection
    
    # Copy files to VM
    log "Copying health monitoring script to VM..."
    sshpass -p "$VM_PASSWORD" scp -o StrictHostKeyChecking=no "vm-health-monitor.sh" "$VM_USER@$VM_IP:/home/azureuser/"
    
    log "Copying configuration script to VM..."
    sshpass -p "$VM_PASSWORD" scp -o StrictHostKeyChecking=no "configure-azure-monitor-agent.sh" "$VM_USER@$VM_IP:/home/azureuser/"
    
    log "✅ Files copied successfully to VM"
    
    # Optionally run configuration automatically
    info "Would you like to automatically configure Azure Monitor Agent on the VM? (y/N)"
    read -r configure_now
    
    if [[ "$configure_now" =~ ^[Yy]$ ]]; then
        log "Configuring Azure Monitor Agent on VM..."
        sshpass -p "$VM_PASSWORD" ssh -o StrictHostKeyChecking=no "$VM_USER@$VM_IP" "chmod +x configure-azure-monitor-agent.sh && sudo ./configure-azure-monitor-agent.sh"
        log "✅ Azure Monitor Agent configuration completed!"
    else
        info "To configure later, SSH to your VM and run:"
        info "  sshpass -p 'YOUR_PASSWORD' ssh $VM_USER@$VM_IP"
        info "  chmod +x configure-azure-monitor-agent.sh"
        info "  sudo ./configure-azure-monitor-agent.sh"
    fi
}

# Function to show deployment summary
show_deployment_summary() {
    log "🎉 Azure Monitor setup completed successfully!"
    echo
    info "=== AZURE RESOURCES CREATED ==="
    info "• Resource Group: $RESOURCE_GROUP"
    info "• Log Analytics Workspace: $LOG_ANALYTICS_WORKSPACE"
    info "• Workspace ID: $WORKSPACE_ID"
    info "• Location: $LOCATION"
    echo
    info "=== VM DEPLOYMENT FILES CREATED ==="
    info "• $OUTPUT_DIR/vm-health-monitor.sh"
    info "• $OUTPUT_DIR/configure-azure-monitor-agent.sh"
    info "• $OUTPUT_DIR/DEPLOYMENT_INSTRUCTIONS.md"
    echo
    info "=== NEXT STEPS ==="
    info "1. Review the deployment instructions: $OUTPUT_DIR/DEPLOYMENT_INSTRUCTIONS.md"
    info "2. Deploy to VM automatically: $0 deploy-to-vm"
    info "   OR manually copy files with: sshpass -p 'PASSWORD' scp *.sh $VM_USER@$VM_IP:/home/azureuser/"
    info "3. SSH to VM and run: sshpass -p 'PASSWORD' ssh $VM_USER@$VM_IP"
    info "4. Access your data in Azure Portal > Log Analytics"
    echo
    info "=== AZURE PORTAL LINKS ==="
    info "• Log Analytics: https://portal.azure.com/#@/resource$WORKSPACE_RESOURCE_ID"
    info "• Resource Group: https://portal.azure.com/#@/resource/subscriptions/$(az account show --query id -o tsv)/resourceGroups/$RESOURCE_GROUP"
    echo
    info "=== COST INFORMATION ==="
    info "• Monthly cost: $0.00 (within 5GB free tier)"
    info "• Estimated usage: ~20MB/month"
    info "• Data retention: 30 days (free)"
    echo
}

# Main execution
case "$1" in
    "create-workspace")
        check_azure_cli
        create_resource_group
        create_log_analytics_workspace
        ;;
    "generate-deployment")
        if [ -z "$WORKSPACE_ID" ]; then
            # Try to get workspace details from existing workspace
            if [ -n "$LOG_ANALYTICS_WORKSPACE" ] && [ -n "$RESOURCE_GROUP" ]; then
                check_azure_cli
                log "Getting workspace details for: $LOG_ANALYTICS_WORKSPACE"
                
                local workspace_id=$(az monitor log-analytics workspace show \
                    --resource-group "$RESOURCE_GROUP" \
                    --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
                    --query customerId -o tsv 2>/dev/null)
                
                local workspace_key=$(az monitor log-analytics workspace get-shared-keys \
                    --resource-group "$RESOURCE_GROUP" \
                    --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
                    --query primarySharedKey -o tsv 2>/dev/null)
                
                local workspace_resource_id=$(az monitor log-analytics workspace show \
                    --resource-group "$RESOURCE_GROUP" \
                    --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
                    --query id -o tsv 2>/dev/null)
                
                if [ -n "$workspace_id" ]; then
                    WORKSPACE_ID="$workspace_id"
                    WORKSPACE_KEY="$workspace_key"
                    WORKSPACE_RESOURCE_ID="$workspace_resource_id"
                    
                    info "✅ Using existing workspace: $LOG_ANALYTICS_WORKSPACE"
                    info "✅ Workspace ID: $WORKSPACE_ID"
                else
                    error "Could not find workspace '$LOG_ANALYTICS_WORKSPACE' in resource group '$RESOURCE_GROUP'"
                    exit 1
                fi
            else
                error "No workspace information available. Please run 'create-workspace' first or set environment variables"
                exit 1
            fi
        fi
        generate_vm_deployment_files
        ;;
    "full-setup")
        check_azure_cli
        create_resource_group
        create_log_analytics_workspace
        generate_vm_deployment_files
        show_deployment_summary
        ;;
    "interactive-setup")
        check_azure_cli
        prompt_for_configuration
        create_resource_group
        create_log_analytics_workspace
        generate_vm_deployment_files
        show_deployment_summary
        ;;
    "quick-deploy")
        # Use existing workspace and deploy with minimal prompts
        if [ -z "$WORKSPACE_ID" ]; then
            if [ -n "$LOG_ANALYTICS_WORKSPACE" ] && [ -n "$RESOURCE_GROUP" ]; then
                check_azure_cli
                log "Getting workspace details for: $LOG_ANALYTICS_WORKSPACE"
                
                local workspace_id=$(az monitor log-analytics workspace show \
                    --resource-group "$RESOURCE_GROUP" \
                    --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
                    --query customerId -o tsv 2>/dev/null)
                
                local workspace_key=$(az monitor log-analytics workspace get-shared-keys \
                    --resource-group "$RESOURCE_GROUP" \
                    --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
                    --query primarySharedKey -o tsv 2>/dev/null)
                
                local workspace_resource_id=$(az monitor log-analytics workspace show \
                    --resource-group "$RESOURCE_GROUP" \
                    --workspace-name "$LOG_ANALYTICS_WORKSPACE" \
                    --query id -o tsv 2>/dev/null)
                
                if [ -n "$workspace_id" ]; then
                    WORKSPACE_ID="$workspace_id"
                    WORKSPACE_KEY="$workspace_key"
                    WORKSPACE_RESOURCE_ID="$workspace_resource_id"
                    
                    info "✅ Using existing workspace: $LOG_ANALYTICS_WORKSPACE"
                    generate_vm_deployment_files
                    deploy_to_vm
                else
                    error "Could not find workspace. Please run 'interactive-setup' first"
                    exit 1
                fi
            else
                error "No workspace found. Please run 'interactive-setup' first"
                exit 1
            fi
        else
            generate_vm_deployment_files
            deploy_to_vm
        fi
        ;;
    "deploy-to-vm")
        if [ ! -d "$OUTPUT_DIR" ]; then
            error "No deployment files found. Please run 'full-setup' first"
            exit 1
        fi
        deploy_to_vm
        ;;
    *)
        echo "🔧 Secure Azure Monitor Setup (FREE Log Analytics Only)"
        echo
        echo "Usage: $0 [COMMAND]"
        echo
        echo "Commands:"
        echo "  interactive-setup   🌟 Interactive setup with prompts (recommended for first time)"
        echo "  quick-deploy        ⚡ Quick deploy using existing workspace (recommended)"
        echo "  full-setup          🔧 Automated setup with defaults"
        echo "  create-workspace    🏗️  Create Log Analytics workspace only"
        echo "  generate-deployment 📝 Generate VM deployment files only"
        echo "  deploy-to-vm        🚀 Deploy files to VM using sshpass"
        echo
        if [ -n "$DETECTED_WORKSPACE" ]; then
            echo "🔍 Detected Configuration:"
            echo "  Workspace: $DETECTED_WORKSPACE"
            echo "  Resource Group: $DETECTED_RESOURCE_GROUP"
            echo "  Location: $DETECTED_LOCATION"
            echo "  Workspace ID: $DETECTED_WORKSPACE_ID"
            echo
        fi
        echo "Environment Variables (optional):"
        echo "  AZURE_RESOURCE_GROUP    Resource group name (detected: ${DETECTED_RESOURCE_GROUP:-not found})"
        echo "  AZURE_LOCATION          Azure region (detected: ${DETECTED_LOCATION:-not found})"
        echo "  AZURE_VM_NAME           VM name (default: vmdevbkaudit-sys001)"
        echo "  LOG_ANALYTICS_WORKSPACE Workspace name (detected: ${DETECTED_WORKSPACE:-not found})"
        echo "  VM_IP                   VM IP address (default: 20.2.21.39)"
        echo "  VM_PASSWORD             VM password (will prompt if not set)"
        echo "  VM_USER                 VM username (default: azureuser)"
        echo
        echo "Quick Start Examples:"
        if [ -n "$DETECTED_WORKSPACE" ]; then
            echo "  $0 quick-deploy                    # Use detected workspace and deploy"
        fi
        echo "  $0 interactive-setup               # Interactive setup with prompts"
        echo "  VM_IP=your.vm.ip $0 full-setup     # Automated setup with custom VM IP"
        echo
        echo "🔐 Security Note:"
        echo "  - This script runs on your Mac with your Azure credentials"
        echo "  - VM deployment files contain no sensitive information"
        echo "  - Azure Monitor Agent on VM uses workspace ID/key only"
        echo
        echo "💰 Cost: FREE (within 5GB Log Analytics limit)"
        exit 1
        ;;
esac
