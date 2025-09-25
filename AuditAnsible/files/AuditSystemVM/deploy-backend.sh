#!/bin/bash

# Deploy Backend Services for VM Environment
# This script builds and deploys all Java backend services on Ubuntu VM

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

# Determine backend source location
LOCAL_BACKEND_PATH="/Users/luiswing/Desktop/GTI/AuditSystemBackend"

if [ -d "$LOCAL_BACKEND_PATH" ]; then
    # Use local source (development environment)
    log "Using local backend source code..."
    BACKEND_SOURCE="$LOCAL_BACKEND_PATH"
    
    # Create backend deployment directory
    mkdir -p /opt/auditsystem/backend
    cd /opt/auditsystem/backend
    
    # Copy backend source code
    log "Copying backend source code..."
    if [ -d "AuditSystemBackend" ]; then
        rm -rf AuditSystemBackend
    fi
    cp -r "$BACKEND_SOURCE" ./AuditSystemBackend
    BACKEND_SOURCE="/opt/auditsystem/backend/AuditSystemBackend"
else
    # Clone from GitHub (VM environment)
    log "Cloning AuditSystem backend repository..."
    cd /opt/auditsystem
    if [ -d "AuditSystemBackend" ]; then
        rm -rf AuditSystemBackend
    fi
    git clone https://oauth2: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@github.com/GTI-CTU/AuditSystemBackend.git || {
        error "Failed to clone repository. Please check your internet connection and repository access."
        exit 1
    }
    BACKEND_SOURCE="/opt/auditsystem/AuditSystemBackend"
fi

cd "$BACKEND_SOURCE"

# Set JAVA_HOME for Java 11 (supports Java 10+ features)
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# CRITICAL FIX: Ensure Nacos log directories exist (prevents Nacos startup failures)
log "Creating Nacos log directories..."
sudo mkdir -p /logs/access_log
sudo chown -R azureuser:azureuser /logs
info "✅ Nacos log directories created with proper permissions"

# Build the project
log "Building backend project with Maven..."
info "This will compile updated bootstrap.yml files with Nacos authentication credentials"
mvn clean package -DskipTests

# Create deployment structure
log "Creating deployment structure..."
mkdir -p /opt/auditsystem/backend/services/{gateway,auth,system,biz,job,report}
mkdir -p /opt/auditsystem/logs/{gateway,auth,system,biz,job,report}
mkdir -p /opt/auditsystem/uploads

# Copy built JARs to service directories
log "Deploying services..."

# Gateway Service
cp bytefinger-gateway/target/bytefinger-gateway.jar /opt/auditsystem/backend/services/gateway/
# Auth Service  
cp bytefinger-auth/target/bytefinger-auth.jar /opt/auditsystem/backend/services/auth/
# System Service
cp bytefinger-modules/bytefinger-system/target/bytefinger-system.jar /opt/auditsystem/backend/services/system/
# Business Service
cp bytefinger-modules/bytefinger-biz/target/bytefinger-biz.jar /opt/auditsystem/backend/services/biz/
# Job Service
cp bytefinger-modules/bytefinger-job/target/bytefinger-job.jar /opt/auditsystem/backend/services/job/
# Report Service
cp bytefinger-modules/bytefinger-report/target/bytefinger-report.jar /opt/auditsystem/backend/services/report/

# Create application.yml for each service
log "Creating application configurations..."

# Gateway Service Configuration
cat > /opt/auditsystem/backend/services/gateway/application.yml << 'EOF'
server:
  port: 8200

spring:
  application:
    name: bytefinger-gateway
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: bytefinger-audit-local
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
        namespace: bytefinger-audit-local
        group: DEFAULT_GROUP
    gateway:
      routes:
        - id: bytefinger-auth
          uri: lb://bytefinger-auth
          predicates:
            - Path=/auth/**
          filters:
            - StripPrefix=1
        - id: bytefinger-system
          uri: lb://bytefinger-system
          predicates:
            - Path=/system/**
          filters:
            - StripPrefix=1
        - id: bytefinger-biz
          uri: lb://bytefinger-biz
          predicates:
            - Path=/biz/**
          filters:
            - StripPrefix=1
        - id: bytefinger-job
          uri: lb://bytefinger-job
          predicates:
            - Path=/job/**
          filters:
            - StripPrefix=1
        - id: bytefinger-report
          uri: lb://bytefinger-report
          predicates:
            - Path=/report/**
          filters:
            - StripPrefix=1

logging:
  file:
    name: /opt/auditsystem/logs/gateway/gateway.log
  level:
    com.bytefinger: debug
EOF

# Auth Service Configuration  
cat > /opt/auditsystem/backend/services/auth/application.yml << 'EOF'
server:
  port: 8300

spring:
  application:
    name: bytefinger-auth
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: bytefinger-audit-local
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
        namespace: bytefinger-audit-local
        group: DEFAULT_GROUP
  redis:
    host: 127.0.0.1
    port: 6379
    timeout: 60000
  datasource:
    url: jdbc:mysql://localhost:3306/bytefinger_toutuo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
    username: auditsystem
    password: audit123
    driver-class-name: com.mysql.cj.jdbc.Driver

logging:
  file:
    name: /opt/auditsystem/logs/auth/auth.log
  level:
    com.bytefinger: debug
EOF

# System Service Configuration
cat > /opt/auditsystem/backend/services/system/application.yml << 'EOF'
server:
  port: 8400

spring:
  application:
    name: bytefinger-system
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: bytefinger-audit-local
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
        namespace: bytefinger-audit-local
        group: DEFAULT_GROUP
  redis:
    host: 127.0.0.1
    port: 6379
    timeout: 60000
  datasource:
    url: jdbc:mysql://localhost:3306/bytefinger_toutuo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
    username: auditsystem
    password: audit123
    driver-class-name: com.mysql.cj.jdbc.Driver

logging:
  file:
    name: /opt/auditsystem/logs/system/system.log
  level:
    com.bytefinger: debug
EOF

# Business Service Configuration
cat > /opt/auditsystem/backend/services/biz/application.yml << 'EOF'
server:
  port: 8500

spring:
  application:
    name: bytefinger-biz
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: bytefinger-audit-local
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
        namespace: bytefinger-audit-local
        group: DEFAULT_GROUP
  redis:
    host: 127.0.0.1
    port: 6379
    timeout: 60000
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: auditsystem
    password: auditsystem123
    virtual-host: auditsystem
  datasource:
    url: jdbc:mysql://localhost:3306/bytefinger_toutuo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
    username: auditsystem
    password: audit123
    driver-class-name: com.mysql.cj.jdbc.Driver

logging:
  file:
    name: /opt/auditsystem/logs/biz/biz.log
  level:
    com.bytefinger: debug
EOF

# Job Service Configuration
cat > /opt/auditsystem/backend/services/job/application.yml << 'EOF'
server:
  port: 8600

spring:
  application:
    name: bytefinger-job
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: bytefinger-audit-local
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
        namespace: bytefinger-audit-local
        group: DEFAULT_GROUP
  redis:
    host: 127.0.0.1
    port: 6379
    timeout: 60000
  datasource:
    url: jdbc:mysql://localhost:3306/bytefinger_toutuo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
    username: auditsystem
    password: audit123
    driver-class-name: com.mysql.cj.jdbc.Driver

logging:
  file:
    name: /opt/auditsystem/logs/job/job.log
  level:
    com.bytefinger: debug
EOF

# Report Service Configuration
cat > /opt/auditsystem/backend/services/report/application.yml << 'EOF'
server:
  port: 8700

spring:
  application:
    name: bytefinger-report
  profiles:
    active: dev
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: bytefinger-audit-local
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
        namespace: bytefinger-audit-local
        group: DEFAULT_GROUP
  redis:
    host: 127.0.0.1
    port: 6379
    timeout: 60000
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: auditsystem
    password: auditsystem123
    virtual-host: auditsystem
  datasource:
    url: jdbc:mysql://localhost:3306/bytefinger_toutuo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8
    username: auditsystem
    password: audit123
    driver-class-name: com.mysql.cj.jdbc.Driver

logging:
  file:
    name: /opt/auditsystem/logs/report/report.log
  level:
    com.bytefinger: debug
EOF

# Setup Nacos configurations
log "Setting up Nacos configurations..."
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NACOS_SETUP_SCRIPT="$SCRIPT_DIR/setup-nacos-config.sh"

if [ -f "$NACOS_SETUP_SCRIPT" ]; then
    chmod +x "$NACOS_SETUP_SCRIPT"
    if "$NACOS_SETUP_SCRIPT"; then
        log "Nacos configurations applied successfully"
    else
        warning "Failed to apply Nacos configurations. Services may need manual configuration."
    fi
else
    warning "Nacos setup script not found at $NACOS_SETUP_SCRIPT"
    warning "Please run './setup-nacos-config.sh' manually after deployment"
fi

# Create systemd services for each microservice
log "Creating systemd services..."

services=("gateway" "auth" "system" "biz" "job" "report")
ports=("8200" "8300" "8400" "8500" "8600" "8700")

for i in "${!services[@]}"; do
    service="${services[$i]}"
    port="${ports[$i]}"
    
    sudo tee /etc/systemd/system/auditsystem-${service}.service > /dev/null << EOF
[Unit]
Description=AuditSystem ${service^} Service
After=network.target nacos.service mysql.service redis-server.service

[Service]
Type=simple
User=$USER
Group=$USER
WorkingDirectory=/opt/auditsystem/backend/services/${service}
ExecStart=/usr/lib/jvm/java-11-openjdk-amd64/bin/java -Xms512m -Xmx1024m -jar bytefinger-${service}.jar
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=auditsystem-${service}
Environment=JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

[Install]
WantedBy=multi-user.target
EOF
done

# Set ownership
sudo chown -R $USER:$USER /opt/auditsystem/backend
sudo chown -R $USER:$USER /opt/auditsystem/logs

# Reload systemd and enable services
log "Enabling services..."
sudo systemctl daemon-reload

for service in "${services[@]}"; do
    sudo systemctl enable auditsystem-${service}
done

# Apply Nacos configurations
log "Setting up Nacos configurations..."
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NACOS_SCRIPT="$SCRIPT_DIR/setup-nacos-config.sh"

if [ -f "$NACOS_SCRIPT" ]; then
    chmod +x "$NACOS_SCRIPT"
    if curl -f http://localhost:8848/nacos/ > /dev/null 2>&1; then
        log "Applying Nacos configurations..."
        "$NACOS_SCRIPT" no-restart
        log "Nacos configurations applied successfully!"
    else
        warning "Nacos is not running. Configurations will be applied when Nacos starts."
        info "To apply configurations later, run: ./setup-nacos-config.sh"
    fi
else
    warning "Nacos configuration script not found at: $NACOS_SCRIPT"
fi

log "Backend deployment completed!"
info "Services created:"
for i in "${!services[@]}"; do
    service="${services[$i]}"
    port="${ports[$i]}"
    info "  - auditsystem-${service} (port ${port})"
done

info "To start all services, run: sudo systemctl start auditsystem-{gateway,auth,system,biz,job,report}"
info "To check service status: sudo systemctl status auditsystem-<service-name>"
info "To view logs: sudo journalctl -u auditsystem-<service-name> -f"

warning "Note: Services are not started automatically. Start them manually after ensuring dependencies are running."
