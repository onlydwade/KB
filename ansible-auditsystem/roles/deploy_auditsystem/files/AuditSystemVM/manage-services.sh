#!/bin/bash

# AuditSystem Service Management Script
# This script helps manage all AuditSystem services

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

# Define service order for startup/shutdown
INFRASTRUCTURE_SERVICES=("mysql" "redis-server" "rabbitmq-server")
AUDITSYSTEM_SERVICES=("nacos" "auditsystem-auth" "auditsystem-system" "auditsystem-biz" "auditsystem-job" "auditsystem-report" "auditsystem-gateway")
WEB_SERVICES=("nginx")

ALL_SERVICES=("${INFRASTRUCTURE_SERVICES[@]}" "${AUDITSYSTEM_SERVICES[@]}" "${WEB_SERVICES[@]}")

# Function to check if service exists
service_exists() {
    systemctl list-unit-files | grep -q "^$1.service"
}

# Function to get service status
get_service_status() {
    local service=$1
    if service_exists "$service"; then
        if systemctl is-active --quiet "$service"; then
            echo -e "${GREEN}RUNNING${NC}"
        elif systemctl is-enabled --quiet "$service"; then
            echo -e "${YELLOW}STOPPED${NC}"
        else
            echo -e "${RED}DISABLED${NC}"
        fi
    else
        echo -e "${RED}NOT FOUND${NC}"
    fi
}

# Function to wait for service to be ready
wait_for_service() {
    local service=$1
    local max_wait=${2:-60}
    local count=0
    
    while [ $count -lt $max_wait ]; do
        if systemctl is-active --quiet "$service"; then
            return 0
        fi
        sleep 1
        count=$((count + 1))
    done
    return 1
}

# Function to check service health
check_service_health() {
    local service=$1
    case $service in
        "mysql")
            mysqladmin ping -h localhost -u root -proot123 > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "redis-server")
            redis-cli ping > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "rabbitmq-server")
            # Check if RabbitMQ is responding on its management port
            sudo rabbitmqctl status > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "nacos")
            curl -f http://localhost:8848/nacos/ > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "auditsystem-gateway")
            curl -f http://localhost:8200/actuator/health > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "auditsystem-auth")
            curl -f http://localhost:8300/actuator/health > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "auditsystem-system")
            curl -f http://localhost:8400/actuator/health > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "auditsystem-biz")
            curl -f http://localhost:8500/actuator/health > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "auditsystem-job")
            curl -f http://localhost:8600/actuator/health > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "auditsystem-report")
            curl -f http://localhost:8700/actuator/health > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        "nginx")
            curl -f http://localhost/health > /dev/null 2>&1 && echo "OK" || echo "FAIL"
            ;;
        *)
            echo "UNKNOWN"
            ;;
    esac
}

# Function to show status of all services
show_status() {
    echo
    echo "=== AuditSystem Service Status ==="
    echo
    printf "%-25s %-10s %-10s\n" "SERVICE" "STATUS" "HEALTH"
    printf "%-25s %-10s %-10s\n" "-------" "------" "------"
    
    for service in "${ALL_SERVICES[@]}"; do
        if service_exists "$service"; then
            status=$(get_service_status "$service")
            health=$(check_service_health "$service")
            printf "%-25s %-10s %-10s\n" "$service" "$status" "$health"
        else
            printf "%-25s %-10s %-10s\n" "$service" "NOT FOUND" "N/A"
        fi
    done
    echo
}

# Function to start all services
start_all() {
    log "Starting all AuditSystem services..."
    
    # Start infrastructure services first
    for service in "${INFRASTRUCTURE_SERVICES[@]}"; do
        if service_exists "$service"; then
            log "Starting $service..."
            sudo systemctl start "$service"
            if wait_for_service "$service" 30; then
                log "$service started successfully"
            else
                error "Failed to start $service"
                return 1
            fi
        fi
    done
    
    # Wait a bit for infrastructure to be ready
    sleep 5
    
    # CRITICAL FIX: Ensure Nacos log directories exist before starting AuditSystem services
    log "Ensuring Nacos log directories exist..."
    if [ ! -d "/logs/access_log" ]; then
        info "Creating missing Nacos log directories..."
        sudo mkdir -p /logs/access_log
        sudo chown -R azureuser:azureuser /logs
        info "✅ Nacos log directories created"
    else
        info "✅ Nacos log directories already exist"
    fi
    
    # Start AuditSystem services
    for service in "${AUDITSYSTEM_SERVICES[@]}"; do
        if service_exists "$service"; then
            log "Starting $service..."
            sudo systemctl start "$service"
            if wait_for_service "$service" 60; then
                log "$service started successfully"
            else
                warning "$service may still be starting up"
            fi
            sleep 10  # Wait between service starts
        fi
    done
    
    # Start web services last
    for service in "${WEB_SERVICES[@]}"; do
        if service_exists "$service"; then
            log "Starting $service..."
            sudo systemctl start "$service"
            if wait_for_service "$service" 15; then
                log "$service started successfully"
            else
                error "Failed to start $service"
                return 1
            fi
        fi
    done
    
    log "All services startup completed!"
    sleep 5
    show_status
}

# Function to stop all services
stop_all() {
    log "Stopping all AuditSystem services..."
    
    # Stop in reverse order
    for service in "${WEB_SERVICES[@]}" "${AUDITSYSTEM_SERVICES[@]}" "${INFRASTRUCTURE_SERVICES[@]}"; do
        if service_exists "$service" && systemctl is-active --quiet "$service"; then
            log "Stopping $service..."
            sudo systemctl stop "$service"
            
            # Wait up to 10 seconds for service to stop
            local count=0
            while systemctl is-active --quiet "$service" && [ $count -lt 10 ]; do
                sleep 1
                count=$((count + 1))
            done
            
            # If still running after timeout, force stop
            if systemctl is-active --quiet "$service"; then
                warning "$service didn't stop gracefully, forcing stop..."
                sudo systemctl kill -s TERM "$service" || true
                sleep 2
                
                # Final force kill if needed
                if systemctl is-active --quiet "$service"; then
                    warning "Force killing $service..."
                    sudo systemctl kill -s KILL "$service" || true
                    sleep 1
                fi
            fi
        fi
    done
    
    log "All services stopped!"
    show_status
}

# Function to restart all services
restart_all() {
    log "Restarting all AuditSystem services..."
    stop_all
    sleep 5
    start_all
}

# Function to show logs for a specific service
show_logs() {
    local service=$1
    local lines=${2:-50}
    
    if [ -z "$service" ]; then
        error "Please specify a service name"
        echo "Available services:"
        for svc in "${ALL_SERVICES[@]}"; do
            echo "  - $svc"
        done
        return 1
    fi
    
    if service_exists "$service"; then
        log "Showing last $lines lines of $service logs..."
        sudo journalctl -u "$service" -n "$lines" --no-pager
    else
        error "Service $service not found"
        return 1
    fi
}

# Function to follow logs for a specific service
follow_logs() {
    local service=$1
    
    if [ -z "$service" ]; then
        error "Please specify a service name"
        return 1
    fi
    
    if service_exists "$service"; then
        log "Following logs for $service (Ctrl+C to exit)..."
        sudo journalctl -u "$service" -f
    else
        error "Service $service not found"
        return 1
    fi
}

# Function to debug failing services
debug_services() {
    local lines=${1:-100}
    
    log "Checking for failing services and showing their logs..."
    echo
    
    local failing_services=()
    
    for service in "${ALL_SERVICES[@]}"; do
        if service_exists "$service"; then
            local health=$(check_service_health "$service")
            local status=$(get_service_status "$service")
            
            # Check if service is in a problematic state
            if [[ "$health" == "FAIL" ]] || [[ "$status" == *"STOPPED"* ]] || [[ "$status" == *"DISABLED"* ]]; then
                failing_services+=("$service")
            fi
        fi
    done
    
    if [ ${#failing_services[@]} -eq 0 ]; then
        log "No failing services detected!"
        return 0
    fi
    
    info "Found ${#failing_services[@]} services with issues:"
    for service in "${failing_services[@]}"; do
        echo "  - $service"
    done
    echo
    
    for service in "${failing_services[@]}"; do
        echo "======================================================"
        log "Logs for $service (last $lines lines):"
        echo "======================================================"
        sudo journalctl -u "$service" -n "$lines" --no-pager
        echo
        echo "======================================================"
        log "Service status for $service:"
        echo "======================================================"
        sudo systemctl status "$service" --no-pager
        echo
        echo
    done
    
    info "Debug complete. Check the logs above for error details."
    echo
    info "To follow live logs for a specific service, use:"
    info "  $0 follow <service-name>"
    echo
}

# Function to setup Nacos configurations
setup_nacos_config() {
    log "Setting up Nacos configurations..."
    
    local script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    local nacos_setup_script="$script_dir/setup-nacos-config.sh"
    
    if [ ! -f "$nacos_setup_script" ]; then
        error "Nacos setup script not found: $nacos_setup_script"
        return 1
    fi
    
    # Make script executable
    chmod +x "$nacos_setup_script"
    
    # Run the setup script
    "$nacos_setup_script"
}

# Function to check current Nacos configurations
check_nacos_config() {
    local nacos_host="localhost"
    local nacos_port="8848"
    local nacos_user="nacos"
    local nacos_pass="nacos"
    
    log "Checking current Nacos configurations..."
    
    # Check if Nacos is accessible
    if ! curl -f "http://$nacos_host:$nacos_port/nacos/" >/dev/null 2>&1; then
        error "Cannot connect to Nacos at $nacos_host:$nacos_port"
        error "Please ensure Nacos is running and accessible"
        return 1
    fi
    
    echo
    info "=== NACOS CONFIGURATION DUMP ==="
    echo
    
    # 1. Check shared configuration
    info "1. Shared Configuration (application-local.yml):"
    echo "================================================"
    local shared_config=$(curl -s -u "$nacos_user:$nacos_pass" "http://$nacos_host:$nacos_port/nacos/v1/cs/configs?dataId=application-local.yml&group=DEFAULT_GROUP" 2>/dev/null)
    
    if [ -n "$shared_config" ] && [ "$shared_config" != "config data not exist" ]; then
        echo "$shared_config"
        
        # Check for critical properties
        echo
        info "Checking critical properties in shared config:"
        if echo "$shared_config" | grep -q "downLoad.host"; then
            info "✓ downLoad.host property found"
        else
            error "✗ downLoad.host property MISSING"
        fi
        
        if echo "$shared_config" | grep -q "file:"; then
            info "✓ file.path property found"
        else
            error "✗ file.path property MISSING"
        fi
        
        if echo "$shared_config" | grep -q "username: auditsystem" && echo "$shared_config" | grep -q "password: auditsystem123"; then
            info "✓ RabbitMQ auditsystem credentials found"
        else
            warning "⚠ RabbitMQ credentials may be incorrect (should be auditsystem/auditsystem123)"
        fi
    else
        error "✗ Shared configuration NOT FOUND"
    fi
    
    echo
    echo
    
    # 2. Check service configurations
    info "2. Service Configurations:"
    echo "========================="
    
    local services=("bytefinger-gateway" "bytefinger-auth" "bytefinger-system" "bytefinger-biz" "bytefinger-job" "bytefinger-report")
    local group="bytefinger-audit-local"
    
    for service in "${services[@]}"; do
        info "--- $service ---"
        local service_config=$(curl -s -u "$nacos_user:$nacos_pass" "http://$nacos_host:$nacos_port/nacos/v1/cs/configs?dataId=$service&group=$group" 2>/dev/null)
        
        if [ -n "$service_config" ] && [ "$service_config" != "config data not exist" ]; then
            echo "$service_config"
            info "✓ $service configuration exists"
        else
            error "✗ $service configuration NOT FOUND"
        fi
        echo
    done
    
    # 3. List all configurations
    info "3. All Configurations Summary:"
    echo "============================="
    local all_configs=$(curl -s -u "$nacos_user:$nacos_pass" "http://$nacos_host:$nacos_port/nacos/v1/cs/configs?search=accurate&pageNo=1&pageSize=200" 2>/dev/null)
    
    if [ -n "$all_configs" ]; then
        echo "$all_configs" | grep -E "(dataId|group)" || echo "$all_configs"
    else
        error "Failed to retrieve configurations list"
    fi
    
    echo
    
    # 4. Check namespaces
    info "4. Available Namespaces:"
    echo "========================"
    local namespaces=$(curl -s -u "$nacos_user:$nacos_pass" "http://$nacos_host:$nacos_port/nacos/v1/console/namespaces" 2>/dev/null)
    
    if [ -n "$namespaces" ]; then
        echo "$namespaces"
        if echo "$namespaces" | grep -q "bytefinger-audit-local"; then
            info "✓ bytefinger-audit-local namespace exists"
        else
            error "✗ bytefinger-audit-local namespace NOT FOUND"
        fi
    else
        error "Failed to retrieve namespaces"
    fi
    
    echo
    log "Nacos configuration check completed!"
    echo
    info "To fix missing configurations, run: $0 setup-nacos"
}

# Function to apply Nacos configurations
apply_nacos_config() {
    local script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    local nacos_script="$script_dir/setup-nacos-config.sh"
    
    if [ ! -f "$nacos_script" ]; then
        error "Nacos configuration script not found: $nacos_script"
        return 1
    fi
    
    log "Applying Nacos configurations..."
    chmod +x "$nacos_script"
    "$nacos_script"
}

# Function to apply Nacos configurations without restarting services
apply_nacos_config_only() {
    local script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    local nacos_script="$script_dir/setup-nacos-config.sh"
    
    if [ ! -f "$nacos_script" ]; then
        error "Nacos configuration script not found: $nacos_script"
        return 1
    fi
    
    log "Applying Nacos configurations (no restart)..."
    chmod +x "$nacos_script"
    "$nacos_script" no-restart
}

# Function to enable all services at boot
enable_all() {
    log "Enabling all services at boot..."
    for service in "${ALL_SERVICES[@]}"; do
        if service_exists "$service"; then
            sudo systemctl enable "$service"
            log "Enabled $service"
        fi
    done
    log "All services enabled at boot!"
}

# Function to disable all services at boot
disable_all() {
    log "Disabling all services at boot..."
    for service in "${AUDITSYSTEM_SERVICES[@]}"; do
        if service_exists "$service"; then
            sudo systemctl disable "$service"
            log "Disabled $service"
        fi
    done
    log "AuditSystem services disabled at boot!"
}

# Function to show help
show_help() {
    echo "AuditSystem Service Management Script"
    echo
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo
    echo "Commands:"
    echo "  status                    Show status of all services"
    echo "  start                     Start all services in correct order"
    echo "  stop                      Stop all services (includes force stop if needed)"
    echo "  restart                   Restart all services"
    echo "  enable                    Enable all services at boot"
    echo "  disable                   Disable AuditSystem services at boot"
    echo "  logs <service> [lines]    Show logs for specific service (default: 50 lines)"
    echo "  follow <service>          Follow logs for specific service"
    echo "  debug [lines]             Show logs for all failing services (default: 100 lines)"
    echo "  setup-nacos              Setup Nacos configurations from nacos-config/ directory"
    echo "  check-nacos              Check current Nacos configurations and identify issues"
    echo "  health                    Show health check for all services"
    echo "  help                      Show this help message"
    echo
    echo "Available services:"
    for service in "${ALL_SERVICES[@]}"; do
        echo "  - $service"
    done
    echo
    echo "Examples:"
    echo "  $0 status"
    echo "  $0 start"
    echo "  $0 stop     # Automatically handles stubborn services"
    echo "  $0 logs auditsystem-gateway"
    echo "  $0 follow nacos"
    echo "  $0 debug    # Shows logs for all failing services"
    echo "  $0 debug 200  # Shows last 200 lines for failing services"
    echo "  $0 setup-nacos  # Upload all configurations to Nacos"
    echo "  $0 check-nacos  # Check current Nacos configurations"
    echo
}

# Main script logic
case "$1" in
    "status")
        show_status
        ;;
    "start")
        start_all
        ;;
    "stop")
        stop_all
        ;;
    "restart")
        restart_all
        ;;
    "enable")
        enable_all
        ;;
    "disable")
        disable_all
        ;;
    "logs")
        show_logs "$2" "$3"
        ;;
    "follow")
        follow_logs "$2"
        ;;
    "debug")
        debug_services "$2"
        ;;
    "setup-nacos")
        setup_nacos_config
        ;;
    "check-nacos")
        check_nacos_config
        ;;
    "health")
        show_status
        ;;
    "help"|"--help"|"-h")
        show_help
        ;;
    "")
        show_help
        ;;
    *)
        error "Unknown command: $1"
        echo
        show_help
        exit 1
        ;;
esac
