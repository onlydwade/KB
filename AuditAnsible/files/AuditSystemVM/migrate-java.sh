#!/bin/bash

# Java Migration Helper Script
# Helps users transition from Java 8 to Java 11 for AuditSystem

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

warning() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] INFO: $1${NC}"
}

echo "=== Java 8 to Java 11 Migration Helper ==="
echo "Date: $(date)"
echo

# Check current Java version
current_java=$(java -version 2>&1 | grep -oP 'version "\K[^"]+' || echo "Not found")
log "Current default Java version: $current_java"

# Check what Java versions are installed
log "Checking installed Java versions..."
if [ -d "/usr/lib/jvm" ]; then
    ls -la /usr/lib/jvm/ | grep -E "(java-8|java-11)" || echo "No OpenJDK versions found"
fi

# Check if Java 8 is running
if [[ "$current_java" == 1.8.* ]]; then
    warning "You are currently using Java 8"
    
    # Check if any AuditSystem services are running
    log "Checking for running AuditSystem services..."
    running_services=()
    for service in auditsystem-auth auditsystem-system auditsystem-biz auditsystem-job auditsystem-report auditsystem-gateway; do
        if systemctl is-active --quiet "$service" 2>/dev/null; then
            running_services+=("$service")
        fi
    done
    
    if [ ${#running_services[@]} -gt 0 ]; then
        warning "Found running AuditSystem services: ${running_services[*]}"
        echo
        log "Migration steps:"
        echo "1. Stop all services: ./manage-services.sh stop"
        echo "2. Install Java 11: ./install-ubuntu-setup.sh"
        echo "3. Rebuild backend: ./deploy-backend.sh"
        echo "4. Start services: ./manage-services.sh start"
        echo
        
        read -p "Do you want to proceed with the migration now? (y/N): " confirm
        if [[ $confirm == [yY] || $confirm == [yY][eE][sS] ]]; then
            log "Starting migration process..."
            
            # Stop services (now includes automatic force stop if needed)
            log "Stopping AuditSystem services..."
            ./manage-services.sh stop
            
            # Quick verification that all services are stopped
            log "Verifying all services are stopped..."
            remaining_services=()
            for service in auditsystem-auth auditsystem-system auditsystem-biz auditsystem-job auditsystem-report auditsystem-gateway; do
                if systemctl is-active --quiet "$service" 2>/dev/null; then
                    remaining_services+=("$service")
                fi
            done
            
            if [ ${#remaining_services[@]} -gt 0 ]; then
                error "❌ Failed to stop services: ${remaining_services[*]}"
                log "The stop command should have handled this. Please check the logs."
                exit 1
            else
                log "✅ All services stopped successfully"
            fi
            
            # Install Java 11
            log "Installing Java 11..."
            ./install-ubuntu-setup.sh
            
            # Verify Java switch
            new_java=$(java -version 2>&1 | grep -oP 'version "\K[^"]+')
            if [[ "$new_java" == 11.* ]]; then
                log "✅ Successfully switched to Java 11: $new_java"
                
                # Rebuild backend
                log "Rebuilding backend with Java 11..."
                ./deploy-backend.sh
                
                # Start services
                log "Starting services..."
                ./manage-services.sh start
                
                log "✅ Migration completed successfully!"
                log "Check service status with: ./manage-services.sh status"
            else
                error "❌ Java switch failed. Current version: $new_java"
                exit 1
            fi
        else
            log "Migration cancelled. You can run this script again when ready."
        fi
    else
        log "No running AuditSystem services found. Safe to proceed with Java 11 installation."
        echo "Run: ./install-ubuntu-setup.sh"
    fi
    
elif [[ "$current_java" == 11.* ]]; then
    log "✅ You are already using Java 11: $current_java"
    log "No migration needed!"
    
else
    warning "Unexpected Java version: $current_java"
    log "Installing Java 11..."
    ./install-ubuntu-setup.sh
fi

# Show Java alternatives
echo
log "Available Java alternatives:"
sudo update-alternatives --list java 2>/dev/null || echo "No alternatives configured"

echo
log "Tips for managing multiple Java versions:"
echo "• Switch Java version: sudo update-alternatives --config java"
echo "• Check current version: java -version"
echo "• Set JAVA_HOME: export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64"
echo "• Verify setup: ./check-java-compatibility.sh"

echo
log "=== Migration Helper Complete ==="
