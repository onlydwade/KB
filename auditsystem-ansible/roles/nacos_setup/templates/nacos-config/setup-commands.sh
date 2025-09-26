#!/bin/bash

# Nacos Configuration Setup Script
# This script applies all required configurations to Nacos for AuditSystem microservices
# Updated to read from YAML files instead of hardcoding content

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

# Function to get Nacos authentication token
get_nacos_token() {
    local response=$(curl -s -X POST "http://localhost:8848/nacos/v1/auth/login" \
        -d "username=nacos&password=nacos")
    
    if [[ $response == *"accessToken"* ]]; then
        echo $response | python3 -c "import sys, json; print(json.load(sys.stdin)['accessToken'])"
    else
        error "Failed to get authentication token: $response"
        return 1
    fi
}

# Function to upload configuration from file
upload_config_from_file() {
    local file_path=$1
    local data_id=$2
    local group=$3
    local description=$4
    
    if [ ! -f "$file_path" ]; then
        error "Configuration file not found: $file_path"
        return 1
    fi
    
    # URL encode the file content
    local content=$(cat "$file_path" | python3 -c "import sys, urllib.parse; print(urllib.parse.quote(sys.stdin.read()))")
    
    # Create the configuration
    local result=$(curl -s -X POST "http://localhost:8848/nacos/v1/cs/configs?accessToken=$ACCESS_TOKEN" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "dataId=${data_id}&group=${group}&content=${content}")
    
    if [ "$result" = "true" ]; then
        echo "✅ ${data_id} (${group}) - ${description}"
        return 0
    else
        echo "❌ ${data_id} (${group}) - FAILED: $result"
        return 1
    fi
}

# Function to verify configuration exists
verify_config() {
    local data_id=$1
    local group=$2
    
    local result=$(curl -s "http://localhost:8848/nacos/v1/cs/configs?accessToken=$ACCESS_TOKEN&dataId=${data_id}&group=${group}")
    
    if [[ $result == *"timestamp"* ]] && [[ $result == *"Forbidden"* ]]; then
        return 1
    elif [[ $result != "" ]] && [[ $result != "null" ]]; then
        return 0
    else
        return 1
    fi
}

# Function to get total configuration count
get_config_count() {
    local response=$(curl -s "http://localhost:8848/nacos/v1/cs/configs?accessToken=$ACCESS_TOKEN&search=accurate&pageNo=1&pageSize=200")
    
    if [[ $response == *"totalCount"* ]]; then
        echo $response | python3 -c "import sys, json; print(json.load(sys.stdin)['totalCount'])" 2>/dev/null || echo "0"
    else
        echo "0"
    fi
}

# Check if Nacos is running
if ! curl -f http://localhost:8848/nacos/ > /dev/null 2>&1; then
    error "Nacos is not running or not accessible at localhost:8848"
    error "Please start Nacos first: sudo systemctl start nacos"
    exit 1
fi

log "Starting Nacos configuration setup..."

# Create database first
log "Creating database..."
sudo mysql -e "CREATE DATABASE IF NOT EXISTS bytefinger_toutuo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null
if [ $? -eq 0 ]; then
    info "Database 'bytefinger_toutuo' created successfully"
else
    warn "Database creation failed or already exists"
fi

# Get authentication token
log "Getting Nacos authentication token..."
ACCESS_TOKEN=$(get_nacos_token)
if [ -z "$ACCESS_TOKEN" ]; then
    error "Failed to get authentication token"
    exit 1
fi
info "Authentication successful"

# Create uploads directory
log "Creating uploads directory..."
sudo mkdir -p /opt/auditsystem/uploads
sudo chown -R auditsystem:auditsystem /opt/auditsystem/uploads 2>/dev/null || true

log "Applying Nacos configurations from YAML files..."

SUCCESS_COUNT=0

# 1. Shared Application Configuration
info "Uploading shared application configuration..."
if upload_config_from_file "shared/application-local.yml" "application-local.yml" "DEFAULT_GROUP" "Shared database/redis/rabbitmq configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 2. Service Configurations
info "Uploading service configurations..."

if upload_config_from_file "services/bytefinger-auth.yml" "bytefinger-auth-local.yml" "bytefinger-audit-local" "Auth Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-system.yml" "bytefinger-system-local.yml" "bytefinger-audit-local" "System Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-biz.yml" "bytefinger-biz-local.yml" "bytefinger-audit-local" "Business Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-job.yml" "bytefinger-job-local.yml" "bytefinger-audit-local" "Job Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-report.yml" "bytefinger-report-local.yml" "bytefinger-audit-local" "Report Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-gateway.yml" "bytefinger-gateway-local.yml" "bytefinger-audit-local" "Gateway Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

log "Configuration upload completed! Successfully uploaded: $SUCCESS_COUNT/7 configurations"

# Verify configurations
log "Verifying configurations..."
VERIFIED_COUNT=0

configs=("application-local.yml:DEFAULT_GROUP" "bytefinger-auth-local.yml:bytefinger-audit-local" "bytefinger-system-local.yml:bytefinger-audit-local" "bytefinger-biz-local.yml:bytefinger-audit-local" "bytefinger-job-local.yml:bytefinger-audit-local" "bytefinger-report-local.yml:bytefinger-audit-local" "bytefinger-gateway-local.yml:bytefinger-audit-local")

for config in "${configs[@]}"; do
    IFS=':' read -r data_id group <<< "$config"
    if verify_config "$data_id" "$group"; then
        echo "✅ Verified: $data_id ($group)"
        VERIFIED_COUNT=$((VERIFIED_COUNT + 1))
    else
        echo "❌ Failed to verify: $data_id ($group)"
    fi
done

log "Verification completed! Verified: $VERIFIED_COUNT/7 configurations"

# Get total configuration count
TOTAL_CONFIGS=$(get_config_count)
log "Total configurations in Nacos: $TOTAL_CONFIGS"

# Function to fix bootstrap configuration for a service
fix_bootstrap_config() {
    local service_name=$1
    local service_port=$2
    local bootstrap_path="/opt/auditsystem/AuditSystemBackend/bytefinger-modules/bytefinger-${service_name}/src/main/resources/bootstrap.yml"
    
    # Create directory if it doesn't exist
    local bootstrap_dir=$(dirname "$bootstrap_path")
    if [ ! -d "$bootstrap_dir" ]; then
        echo "[WARN] Resources directory not found: $bootstrap_dir"
        return 1
    fi
    
    # Create bootstrap file if it doesn't exist
    if [ ! -f "$bootstrap_path" ]; then
        echo "[INFO] Creating missing bootstrap file: $bootstrap_path"
    fi
    
    info "Fixing bootstrap configuration for $service_name..."
    
    # Create the corrected bootstrap configuration
    cat > "$bootstrap_path" << EOF
# Spring Boot Configuration for ${service_name^} Service
server:
  port: ${service_port}

spring:
  application:
    name: bytefinger-${service_name}
  profiles:
    active: local

---
# Local Environment Configuration
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
        namespace: 
        group: DEFAULT_GROUP
      config:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
        namespace:
        file-extension: yml
        group: bytefinger-audit-local
        shared-configs:
          - data-id: application-local.yml
            group: DEFAULT_GROUP
            refresh: true

  config:
    activate:
      on-profile: local

---
# Development Environment (for future use)
spring:
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
        username: nacos
        password: nacos
        namespace: bytefinger-audit-dev
      config:
        server-addr: nacos:8848
        username: nacos
        password: nacos
        namespace: bytefinger-audit-dev
        file-extension: yml
        group: bytefinger-audit-dev
        shared-configs:
          - data-id: application-dev.yml
            group: DEFAULT_GROUP

  config:
    activate:
      on-profile: dev
EOF
    
    echo "✅ Updated bootstrap for $service_name (port $service_port)"
    return 0
}

# Function to fix gateway bootstrap (special case)
fix_gateway_bootstrap() {
    local bootstrap_path="/opt/auditsystem/AuditSystemBackend/bytefinger-gateway/src/main/resources/bootstrap.yml"
    
    if [ ! -f "$bootstrap_path" ]; then
        warn "Gateway bootstrap file not found: $bootstrap_path"
        return 1
    fi
    
    info "Fixing bootstrap configuration for gateway..."
    
    cat > "$bootstrap_path" << 'EOF'
# Spring Boot Configuration for Gateway Service
server:
  port: 8200

spring:
  application:
    name: bytefinger-gateway
  profiles:
    active: local

---
# Local Environment Configuration
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
        namespace: 
        group: DEFAULT_GROUP
      config:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
        namespace:
        file-extension: yml
        group: bytefinger-audit-local
        shared-configs:
          - data-id: application-local.yml
            group: DEFAULT_GROUP
            refresh: true

  config:
    activate:
      on-profile: local
EOF
    
    echo "✅ Updated bootstrap for gateway (port 8200)"
    return 0
}

# Fix all bootstrap configurations
log "Fixing microservice bootstrap configurations..."

BOOTSTRAP_SUCCESS_COUNT=0

# Fix bootstrap files for all services
services=("auth:8300" "system:8400" "biz:8500" "job:8600" "report:8700")

for service in "${services[@]}"; do
    IFS=':' read -r service_name service_port <<< "$service"
    if fix_bootstrap_config "$service_name" "$service_port"; then
        BOOTSTRAP_SUCCESS_COUNT=$((BOOTSTRAP_SUCCESS_COUNT + 1))
    fi
done

# Fix gateway bootstrap (special case)
if fix_gateway_bootstrap; then
    BOOTSTRAP_SUCCESS_COUNT=$((BOOTSTRAP_SUCCESS_COUNT + 1))
fi

log "Bootstrap configuration completed! Fixed: $BOOTSTRAP_SUCCESS_COUNT/6 service configurations"

# Check if all configurations are successful before proceeding
if [ "$SUCCESS_COUNT" -eq 7 ] && [ "$VERIFIED_COUNT" -eq 7 ] && [ "$BOOTSTRAP_SUCCESS_COUNT" -eq 6 ]; then
    log "✅ All configurations and bootstrap files ready!"
    info "Proceeding with service restart..."
else
    warn "⚠️  Some configurations may have failed. Check the output above."
    info "Proceeding with service restart anyway..."
fi

log "Restarting microservices..."
sudo systemctl restart auditsystem-auth auditsystem-system auditsystem-biz auditsystem-job auditsystem-report auditsystem-gateway 2>/dev/null || warn "Some services may not be installed yet"

log "Waiting 10 seconds for services to start..."
sleep 10

log "✅ Complete Nacos setup finished!"
info "Summary:"
info "  • Nacos configurations: $SUCCESS_COUNT/7 uploaded, $VERIFIED_COUNT/7 verified"
info "  • Bootstrap files: $BOOTSTRAP_SUCCESS_COUNT/6 fixed"
info "  • Total Nacos configs: $TOTAL_CONFIGS"
info ""
info "Access points:"
info "  • Nacos Console: http://localhost:8848/nacos (nacos/nacos)"
info "  • Auth Service: http://localhost:8300/actuator/health"
info "  • Gateway: http://localhost:8200/actuator/health"
info ""
info "All configurations are now available in Nacos Configuration Management"
info "Services should now be able to register with Nacos using proper authentication"
