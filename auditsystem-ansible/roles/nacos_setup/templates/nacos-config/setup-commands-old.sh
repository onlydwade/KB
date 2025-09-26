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

# Function to get Nacos access token
get_nacos_token() {
    local response=$(curl -s -X POST "http://localhost:8848/nacos/v1/auth/login" \
        -d "username=nacos&password=nacos")
    
    if echo "$response" | grep -q "accessToken"; then
        echo "$response" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4
    else
        error "Failed to get Nacos access token"
        echo "$response"
        return 1
    fi
}

# Function to create configuration in Nacos
create_nacos_config() {
    local data_id="$1"
    local group="$2"
    local content="$3"
    local token="$4"
    
    local response=$(curl -s -X POST "http://localhost:8848/nacos/v1/cs/configs" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "dataId=${data_id}&group=${group}&content=${content}&accessToken=${token}")
    
    if [ "$response" = "true" ]; then
        echo "✅ ${data_id} (${group})"
        return 0
    else
        echo "❌ ${data_id} (${group}): $response"
        return 1
    fi
}

# Function to verify configuration exists
verify_config() {
    local data_id="$1"
    local group="$2" 
    local token="$3"
    
    local response=$(curl -s "http://localhost:8848/nacos/v1/cs/configs?dataId=${data_id}&group=${group}&accessToken=${token}")
    
    if [ -n "$response" ] && [ "$response" != "null" ] && ! echo "$response" | grep -q '"status":'; then
        return 0
    else
        return 1
    fi
}

# Function to get total configuration count
get_config_count() {
    local token="$1"
    local response=$(curl -s "http://localhost:8848/nacos/v1/cs/configs?search=accurate&dataId=&group=&pageNo=1&pageSize=200" -H "Authorization: Bearer ${token}")
    
    if echo "$response" | grep -q "totalCount"; then
        echo "$response" | grep -o '"totalCount":[0-9]*' | cut -d':' -f2
    else
        # Fallback: try to count pageItems array length
        local count=$(echo "$response" | grep -o '"dataId"' | wc -l)
        echo "$count"
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
TOKEN=$(get_nacos_token)
if [ $? -ne 0 ] || [ -z "$TOKEN" ]; then
    error "Failed to authenticate with Nacos"
    exit 1
fi
info "Authentication successful"

# Check initial configuration count
INITIAL_COUNT=$(get_config_count "$TOKEN")
log "Initial configuration count: $INITIAL_COUNT"

log "Applying Nacos configurations from YAML files..."

SUCCESS_COUNT=0

# 1. Shared Application Configuration
info "Uploading shared application configuration..."
if upload_config_from_file "shared/application-local.yml" "application-local.yml" "DEFAULT_GROUP" "Shared database/redis/rabbitmq configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 2. Service Configurations
info "Uploading service configurations..."

if upload_config_from_file "services/bytefinger-auth.yml" "bytefinger-auth" "bytefinger-audit-local" "Auth Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-system.yml" "bytefinger-system" "bytefinger-audit-local" "System Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-biz.yml" "bytefinger-biz" "bytefinger-audit-local" "Business Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-job.yml" "bytefinger-job" "bytefinger-audit-local" "Job Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-report.yml" "bytefinger-report" "bytefinger-audit-local" "Report Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

if upload_config_from_file "services/bytefinger-gateway.yml" "bytefinger-gateway" "bytefinger-audit-local" "Gateway Service Configuration"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

log "Configuration upload completed! Successfully uploaded: $SUCCESS_COUNT/7 configurations"

# Track successful configurations
SUCCESS_COUNT=0
TOTAL_CONFIGS=7

# 1. Shared Application Configuration
info "Creating shared application configuration..."
SHARED_CONFIG="spring:
  application:
    name: auditsystem
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos
      config:
        server-addr: localhost:8848
        username: nacos
        password: nacos
  datasource:
    dynamic:
      primary: master
      strict: false
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/bytefinger_toutuo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
          username: auditsystem
          password: audit123
          driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    host: localhost
    port: 6379
    timeout: 10000ms
    lettuce:
      pool:
        max-active: 200
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

# File upload configuration
file:
  path: /opt/auditsystem/uploads/

# Management endpoints
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always"

if create_nacos_config "application-local.yml" "DEFAULT_GROUP" "$(echo "$SHARED_CONFIG" | sed 's/%/%25/g' | sed 's/ /%20/g' | sed 's/:/%3A/g' | sed 's/&/%26/g' | sed 's/?/%3F/g' | sed 's/=/%3D/g' | sed 's/+/%2B/g' | sed $'s/\n/%0A/g')" "$TOKEN"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 2. Auth Service Configuration
info "Creating auth service configuration..."
AUTH_CONFIG="server:
  port: 8300
spring:
  application:
    name: bytefinger-auth
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos
      config:
        server-addr: localhost:8848
        username: nacos
        password: nacos
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always"

if create_nacos_config "bytefinger-auth" "bytefinger-audit-local" "$(echo "$AUTH_CONFIG" | sed 's/%/%25/g' | sed 's/ /%20/g' | sed 's/:/%3A/g' | sed 's/&/%26/g' | sed 's/?/%3F/g' | sed 's/=/%3D/g' | sed 's/+/%2B/g' | sed $'s/\n/%0A/g')" "$TOKEN"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 3. System Service Configuration
info "Creating system service configuration..."
if create_nacos_config "bytefinger-system" "bytefinger-audit-local" "server%3A%0A%20%20port%3A%208400%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-system%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always" "$TOKEN"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 4. Biz Service Configuration
info "Creating biz service configuration..."
if create_nacos_config "bytefinger-biz" "bytefinger-audit-local" "server%3A%0A%20%20port%3A%208500%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-biz%0Auser%3A%0A%20%20deptName%3A%20%E8%BF%90%E8%90%A5%E7%AE%A1%E7%90%86%E9%83%A8%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always" "$TOKEN"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 5. Job Service Configuration
info "Creating job service configuration..."
if create_nacos_config "bytefinger-job" "bytefinger-audit-local" "server%3A%0A%20%20port%3A%208600%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-job%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always" "$TOKEN"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 6. Report Service Configuration
info "Creating report service configuration..."
if create_nacos_config "bytefinger-report" "bytefinger-audit-local" "server%3A%0A%20%20port%3A%208700%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-report%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always" "$TOKEN"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# 7. Gateway Service Configuration
info "Creating gateway service configuration..."
if create_nacos_config "bytefinger-gateway" "bytefinger-audit-local" "server%3A%0A%20%20port%3A%208200%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-gateway%0A%20%20cloud%3A%0A%20%20%20%20gateway%3A%0A%20%20%20%20%20%20routes%3A%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-auth%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-auth%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/auth/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-system%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-system%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/system/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-biz%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-biz%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/biz/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-job%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-job%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/job/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-report%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-report%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/report/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always" "$TOKEN"; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
fi

# Verify all configurations
log "Verifying configurations..."
VERIFICATION_COUNT=0

# List of expected configurations
declare -A EXPECTED_CONFIGS
EXPECTED_CONFIGS["application-local.yml"]="DEFAULT_GROUP"
EXPECTED_CONFIGS["bytefinger-auth"]="bytefinger-audit-local"
EXPECTED_CONFIGS["bytefinger-system"]="bytefinger-audit-local"
EXPECTED_CONFIGS["bytefinger-biz"]="bytefinger-audit-local"
EXPECTED_CONFIGS["bytefinger-job"]="bytefinger-audit-local"
EXPECTED_CONFIGS["bytefinger-report"]="bytefinger-audit-local"
EXPECTED_CONFIGS["bytefinger-gateway"]="bytefinger-audit-local"

for data_id in "${!EXPECTED_CONFIGS[@]}"; do
    group="${EXPECTED_CONFIGS[$data_id]}"
    if verify_config "$data_id" "$group" "$TOKEN"; then
        info "✅ Verified: $data_id ($group)"
        VERIFICATION_COUNT=$((VERIFICATION_COUNT + 1))
    else
        error "❌ Missing: $data_id ($group)"
    fi
done

# Get final configuration count
FINAL_COUNT=$(get_config_count "$TOKEN")

# Report results
log "Configuration Setup Results:"
info "Successfully created: $SUCCESS_COUNT/$TOTAL_CONFIGS configurations"
info "Successfully verified: $VERIFICATION_COUNT/$TOTAL_CONFIGS configurations"
info "Total configurations in Nacos: $FINAL_COUNT"

if [ "$SUCCESS_COUNT" -eq "$TOTAL_CONFIGS" ] && [ "$VERIFICATION_COUNT" -eq "$TOTAL_CONFIGS" ]; then
    log "✅ All Nacos configurations applied and verified successfully!"
    
    # Only restart services if all configs are successful
    if command -v systemctl > /dev/null 2>&1; then
        log "Restarting microservices..."
        sudo systemctl restart auditsystem-auth auditsystem-system auditsystem-biz auditsystem-job auditsystem-report auditsystem-gateway 2>/dev/null || warn "Some services may not be installed yet"
        
        log "Waiting 30 seconds for services to start..."
        sleep 30
        
        log "Checking service health..."
        info "Testing service endpoints..."
        
        # Health check function
        check_service() {
            local service_name=$1
            local port=$2
            local response=$(curl -s -I "localhost:${port}/actuator/health" 2>/dev/null | head -n 1)
            if [[ $response == *"200"* ]]; then
                echo "✅ ${service_name} (${port}): HEALTHY"
            elif [[ $response == *"503"* ]]; then
                echo "⚠️  ${service_name} (${port}): STARTING"
            else
                echo "❌ ${service_name} (${port}): NOT_RUNNING"
            fi
        }
        
        check_service "Auth Service" 8300
        check_service "System Service" 8400
        check_service "Biz Service" 8500
        check_service "Job Service" 8600
        check_service "Report Service" 8700
        check_service "Gateway Service" 8200
    fi
    
    log "Setup complete! Check Nacos console at http://localhost:8848/nacos (nacos/nacos)"
    info "All configurations are now available in Nacos Configuration Management"
    
else
    error "Configuration setup incomplete!"
    error "Expected: $TOTAL_CONFIGS, Created: $SUCCESS_COUNT, Verified: $VERIFICATION_COUNT"
    error "Please check Nacos logs and try again"
    exit 1
fi
