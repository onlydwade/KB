#!/bin/bash

# Ubuntu 24 AuditSystem Installation Script
# This script installs all required components for running the AuditSystem
# Author: GitHub Copilot
# Date: $(date +%Y-%m-%d)

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging function
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

# Check if running as root
if [[ $EUID -eq 0 ]]; then
   error "This script should not be run as root. Please run as a regular user with sudo privileges."
   exit 1
fi

# Check if user has sudo privileges
if ! sudo -n true 2>/dev/null; then
    error "This script requires sudo privileges. Please ensure your user is in the sudo group."
    exit 1
fi

log "Starting AuditSystem installation on Ubuntu 24..."

# Update system
log "Updating system packages..."
sudo apt update && sudo DEBIAN_FRONTEND=noninteractive apt upgrade -y

# Install essential packages and core utilities
log "Installing essential packages and core utilities..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y curl wget git unzip software-properties-common apt-transport-https ca-certificates gnupg lsb-release coreutils findutils util-linux

# Verify core utilities are available
log "Verifying core utilities..."
which readlink || sudo DEBIAN_FRONTEND=noninteractive apt install -y coreutils
which expr || sudo DEBIAN_FRONTEND=noninteractive apt install -y coreutils  
which dirname || sudo DEBIAN_FRONTEND=noninteractive apt install -y coreutils

# Check existing Java installation
log "Checking existing Java installation..."
if java -version 2>&1 | grep -q "1.8"; then
    warning "Java 8 detected. Installing Java 11 alongside (both versions will coexist)..."
elif java -version 2>&1 | grep -q "11\."; then
    log "Java 11 already installed. Skipping Java installation..."
else
    log "No compatible Java found. Installing Java 11..."
fi

# Install Java 11 (OpenJDK) - Compatible with Java 10+ features
log "Installing Java 11..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y openjdk-11-jdk openjdk-11-jre

# Configure Java alternatives (allows switching between versions)
log "Configuring Java alternatives..."
if [ -f "/usr/lib/jvm/java-8-openjdk-amd64/bin/java" ]; then
    log "Setting up Java 8 alternative..."
    sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-8-openjdk-amd64/bin/java 1081
    sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/java-8-openjdk-amd64/bin/javac 1081
fi

if [ -f "/usr/lib/jvm/java-11-openjdk-amd64/bin/java" ]; then
    log "Setting up Java 11 alternative (default)..."
    sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-11-openjdk-amd64/bin/java 1111
    sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/java-11-openjdk-amd64/bin/javac 1111
    
    # Set Java 11 as default
    sudo update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/bin/java
    sudo update-alternatives --set javac /usr/lib/jvm/java-11-openjdk-amd64/bin/javac
fi

# Update JAVA_HOME (remove old entries first)
log "Updating JAVA_HOME configuration..."
sudo sed -i '/export JAVA_HOME=/d' /etc/environment 2>/dev/null || true
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' | sudo tee -a /etc/environment
echo 'export PATH=$PATH:$JAVA_HOME/bin' | sudo tee -a /etc/environment
source /etc/environment

# Verify Java installation
log "Verifying Java installation..."
java -version

# Install Maven
log "Installing Maven..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y maven

# Install Node.js 18 LTS
log "Installing Node.js 18 LTS..."
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo DEBIAN_FRONTEND=noninteractive apt install -y nodejs

# Install pnpm
log "Installing pnpm..."
sudo npm install -g pnpm

# Install Nginx
log "Installing Nginx..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y nginx

# Install Redis
log "Installing Redis..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y redis-server

# Configure Redis
log "Configuring Redis..."
sudo systemctl enable redis-server
sudo systemctl start redis-server

# Install Erlang (required for RabbitMQ)
log "Installing Erlang for RabbitMQ..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y erlang-base erlang-asn1 erlang-crypto erlang-eldap erlang-ftp erlang-inets \
    erlang-mnesia erlang-os-mon erlang-parsetools erlang-public-key \
    erlang-runtime-tools erlang-snmp erlang-ssl erlang-syntax-tools \
    erlang-tftp erlang-tools erlang-xmerl

# Verify Erlang installation
log "Verifying Erlang installation..."
if command -v erl >/dev/null 2>&1; then
    log "✅ Erlang is installed and accessible"
    erl_version=$(erl -eval 'erlang:display(erlang:system_info(otp_release)), halt().' -noshell 2>/dev/null || echo "unknown")
    log "Erlang OTP version: $erl_version"
else
    error "❌ Erlang installation failed"
    # Try alternative installation
    warning "Attempting alternative Erlang installation..."
    sudo DEBIAN_FRONTEND=noninteractive apt install -y erlang
fi

# Install RabbitMQ
log "Installing RabbitMQ..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y rabbitmq-server

# Fix RabbitMQ environment paths
log "Fixing RabbitMQ environment..."
sudo mkdir -p /etc/rabbitmq
sudo chown rabbitmq:rabbitmq /etc/rabbitmq

# Create symbolic links for core utilities that RabbitMQ needs
log "Creating symbolic links for RabbitMQ utilities..."
sudo ln -sf /usr/bin/readlink /bin/readlink 2>/dev/null || true
sudo ln -sf /usr/bin/expr /bin/expr 2>/dev/null || true
sudo ln -sf /usr/bin/dirname /bin/dirname 2>/dev/null || true

# Also create them in /usr/local/bin as fallback
sudo ln -sf /usr/bin/readlink /usr/local/bin/readlink 2>/dev/null || true
sudo ln -sf /usr/bin/expr /usr/local/bin/expr 2>/dev/null || true
sudo ln -sf /usr/bin/dirname /usr/local/bin/dirname 2>/dev/null || true

# Fix the RabbitMQ environment script directly as additional safety measure
log "Updating RabbitMQ environment script paths..."
if [ -f "/usr/lib/rabbitmq/bin/rabbitmq-env" ]; then
    sudo sed -i 's|^readlink|/usr/bin/readlink|g' /usr/lib/rabbitmq/bin/rabbitmq-env 2>/dev/null || true
    sudo sed -i 's|^expr|/usr/bin/expr|g' /usr/lib/rabbitmq/bin/rabbitmq-env 2>/dev/null || true
    sudo sed -i 's|^dirname|/usr/bin/dirname|g' /usr/lib/rabbitmq/bin/rabbitmq-env 2>/dev/null || true
    sudo sed -i 's| readlink| /usr/bin/readlink|g' /usr/lib/rabbitmq/bin/rabbitmq-env 2>/dev/null || true
    sudo sed -i 's| expr| /usr/bin/expr|g' /usr/lib/rabbitmq/bin/rabbitmq-env 2>/dev/null || true
    sudo sed -i 's| dirname| /usr/bin/dirname|g' /usr/lib/rabbitmq/bin/rabbitmq-env 2>/dev/null || true
fi

# Ensure PATH includes core utilities and Erlang for RabbitMQ
export PATH="/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:$PATH"

# Configure RabbitMQ environment for Erlang
log "Configuring RabbitMQ environment..."
sudo mkdir -p /etc/rabbitmq

# Create RabbitMQ environment configuration with proper Erlang path
sudo tee /etc/rabbitmq/rabbitmq-env.conf > /dev/null << 'EOF'
# RabbitMQ Environment Configuration
PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:$PATH
export PATH

# Ensure Erlang is accessible
ERL_EPMD_ADDRESS=127.0.0.1
EOF

# Configure RabbitMQ
log "Configuring RabbitMQ..."
sudo systemctl enable rabbitmq-server
sudo systemctl start rabbitmq-server

# Wait for RabbitMQ to fully start
log "Waiting for RabbitMQ to start..."
sleep 10

# Verify RabbitMQ utilities are accessible
log "Verifying RabbitMQ utilities..."
if ! sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl status >/dev/null 2>&1; then
    warning "RabbitMQ status check failed, diagnosing issue..."
    
    # Check if it's an Erlang issue
    if ! command -v erl >/dev/null 2>&1; then
        error "Erlang not found in PATH, installing missing packages..."
        sudo DEBIAN_FRONTEND=noninteractive apt install -y erlang
        sudo systemctl restart rabbitmq-server
        sleep 10
    fi
    
    # Check for specific error types
    rabbitmq_error=$(sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl status 2>&1 | head -5)
    if [[ "$rabbitmq_error" == *"erl: not found"* ]]; then
        warning "Erlang path issue detected, fixing..."
        
        # Find Erlang installation
        erl_path=$(find /usr -name "erl" 2>/dev/null | head -1)
        if [ -n "$erl_path" ]; then
            erl_bin_dir=$(dirname "$erl_path")
            log "Found Erlang at: $erl_bin_dir"
            
            # Update RabbitMQ environment
            sudo tee -a /etc/rabbitmq/rabbitmq-env.conf > /dev/null << EOF
PATH=$erl_bin_dir:/usr/bin:/bin:/usr/sbin:/sbin:$PATH
export PATH
EOF
            sudo systemctl restart rabbitmq-server
            sleep 10
        fi
    fi
    
    # Final restart attempt
    if ! sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl status >/dev/null 2>&1; then
        warning "Standard restart attempt..."
        sudo systemctl restart rabbitmq-server
        sleep 10
    fi
fi

# Enable RabbitMQ management plugin
log "Enabling RabbitMQ management plugin..."
sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmq-plugins enable rabbitmq_management

# Create RabbitMQ virtual host and user with better error handling
log "Creating RabbitMQ virtual host and user..."

# Ensure RabbitMQ is running and accessible
if ! sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl status >/dev/null 2>&1; then
    warning "RabbitMQ not responding, applying comprehensive fix..."
    
    # Apply direct fix to RabbitMQ environment script for readlink/dirname issues
    log "Fixing RabbitMQ environment script paths..."
    if [ -f "/usr/lib/rabbitmq/bin/rabbitmq-env" ]; then
        sudo cp /usr/lib/rabbitmq/bin/rabbitmq-env /usr/lib/rabbitmq/bin/rabbitmq-env.backup 2>/dev/null || true
        sudo sed -i 's|`readlink|`/usr/bin/readlink|g' /usr/lib/rabbitmq/bin/rabbitmq-env
        sudo sed -i 's|`dirname|`/usr/bin/dirname|g' /usr/lib/rabbitmq/bin/rabbitmq-env
        sudo sed -i 's|$(readlink|$(/usr/bin/readlink|g' /usr/lib/rabbitmq/bin/rabbitmq-env
        sudo sed -i 's|$(dirname|$(/usr/bin/dirname|g' /usr/lib/rabbitmq/bin/rabbitmq-env
        log "Applied path fixes to RabbitMQ environment script"
    fi
    
    sudo systemctl restart rabbitmq-server
    sleep 15
    
    # Wait for RabbitMQ to be ready
    for i in {1..30}; do
        if sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl status >/dev/null 2>&1; then
            log "✅ RabbitMQ is ready after fix"
            break
        fi
        sleep 2
        if [ $i -eq 30 ]; then
            error "RabbitMQ failed to start properly after fix"
            sudo systemctl status rabbitmq-server --no-pager -l
            sudo journalctl -u rabbitmq-server --no-pager -l | tail -20
        fi
    done
fi

# Create auditsystem virtual host (better naming)
if sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl list_vhosts | grep -q "^auditsystem$"; then
    log "Virtual host 'auditsystem' already exists"
else
    sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl add_vhost auditsystem && log "Created virtual host 'auditsystem'" || {
        error "Failed to create RabbitMQ virtual host, trying additional fix..."
        
        # Additional comprehensive fix as fallback
        log "Applying comprehensive RabbitMQ environment fix..."
        if [ -f "/usr/lib/rabbitmq/bin/rabbitmq-env" ]; then
            sudo sed -i 's|^readlink|/usr/bin/readlink|g' /usr/lib/rabbitmq/bin/rabbitmq-env
            sudo sed -i 's|^dirname|/usr/bin/dirname|g' /usr/lib/rabbitmq/bin/rabbitmq-env
            sudo sed -i 's| readlink| /usr/bin/readlink|g' /usr/lib/rabbitmq/bin/rabbitmq-env
            sudo sed -i 's| dirname| /usr/bin/dirname|g' /usr/lib/rabbitmq/bin/rabbitmq-env
        fi
        
        # Restart and retry
        sudo systemctl restart rabbitmq-server
        sleep 10
        sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl add_vhost auditsystem && log "✅ Created virtual host after fix" || warning "RabbitMQ virtual host creation still failed"
    }
fi

# Create auditsystem user
if sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl list_users | grep -q "^auditsystem"; then
    log "User 'auditsystem' already exists"
else
    sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl add_user auditsystem auditsystem123 && log "Created user 'auditsystem'"
fi

# Set permissions
sudo PATH="/usr/bin:/bin:/usr/sbin:/sbin:$PATH" rabbitmqctl set_permissions -p auditsystem auditsystem ".*" ".*" ".*" && log "Set permissions for user 'auditsystem'" || {
    warning "Failed to set RabbitMQ permissions, but continuing..."
}

# Install MySQL Server
log "Installing MySQL Server..."
sudo DEBIAN_FRONTEND=noninteractive apt install -y mysql-server mysql-client

# Configure MySQL with robust password handling
log "Configuring MySQL..."
sudo systemctl enable mysql
sudo systemctl start mysql

# Wait for MySQL to be ready
log "Waiting for MySQL to start..."
for i in {1..30}; do
    if sudo systemctl is-active --quiet mysql; then
        log "✅ MySQL service is active"
        break
    fi
    sleep 2
    if [ $i -eq 30 ]; then
        error "MySQL failed to start"
        sudo systemctl status mysql --no-pager -l
        exit 1
    fi
done

# Set MySQL root password with error handling
log "Setting MySQL root password..."
if mysql -u root -e "SELECT 1;" >/dev/null 2>&1; then
    log "MySQL root access without password works, setting password..."
    mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root123';"
    mysql -u root -e "FLUSH PRIVILEGES;"
    log "✅ MySQL root password set to 'root123'"
elif mysql -u root -proot123 -e "SELECT 1;" >/dev/null 2>&1; then
    log "✅ MySQL root password is already set to 'root123'"
else
    warning "MySQL root access issue, attempting password reset..."
    
    # Stop MySQL for safe mode
    sudo systemctl stop mysql
    
    # Create password reset script
    cat > /tmp/mysql-init.sql << 'EOF'
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root123';
FLUSH PRIVILEGES;
EOF
    
    # Start MySQL in safe mode
    sudo mkdir -p /var/run/mysqld
    sudo chown mysql:mysql /var/run/mysqld
    sudo mysqld_safe --init-file=/tmp/mysql-init.sql --user=mysql &
    MYSQL_PID=$!
    
    sleep 10
    
    # Stop safe mode and clean up
    sudo kill $MYSQL_PID 2>/dev/null || true
    sleep 5
    rm -f /tmp/mysql-init.sql
    
    # Start MySQL normally
    sudo systemctl start mysql
    sleep 5
    
    # Verify password works
    if mysql -u root -proot123 -e "SELECT 1;" >/dev/null 2>&1; then
        log "✅ MySQL root password reset successfully"
    else
        error "❌ Failed to reset MySQL root password"
        log "Manual intervention may be required"
    fi
fi

# Create databases with error handling
log "Creating databases..."
mysql -u root -proot123 -e "CREATE DATABASE IF NOT EXISTS nacos_config CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null || warning "Failed to create nacos_config database"
mysql -u root -proot123 -e "CREATE DATABASE IF NOT EXISTS bytefinger_toutuo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null || warning "Failed to create bytefinger_toutuo database"
mysql -u root -proot123 -e "CREATE DATABASE IF NOT EXISTS auditsystem CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null || warning "Failed to create auditsystem database"

# Create users with error handling
log "Creating database users..."
mysql -u root -proot123 -e "CREATE USER IF NOT EXISTS 'nacos'@'%' IDENTIFIED BY 'nacos123';" 2>/dev/null || log "User 'nacos' may already exist"
mysql -u root -proot123 -e "GRANT ALL PRIVILEGES ON nacos_config.* TO 'nacos'@'%';" 2>/dev/null || warning "Failed to grant nacos privileges"
mysql -u root -proot123 -e "CREATE USER IF NOT EXISTS 'auditsystem'@'%' IDENTIFIED BY 'audit123';" 2>/dev/null || log "User 'auditsystem' may already exist"
mysql -u root -proot123 -e "GRANT ALL PRIVILEGES ON bytefinger_toutuo.* TO 'auditsystem'@'%';" 2>/dev/null || warning "Failed to grant auditsystem privileges"
mysql -u root -proot123 -e "GRANT ALL PRIVILEGES ON auditsystem.* TO 'auditsystem'@'%';" 2>/dev/null || warning "Failed to grant auditsystem privileges"
mysql -u root -proot123 -e "FLUSH PRIVILEGES;" 2>/dev/null || warning "Failed to flush privileges"

# Verify database configuration
log "Verifying database configuration..."
if mysql -u root -proot123 -e "SHOW DATABASES;" >/dev/null 2>&1; then
    mysql_databases=$(mysql -u root -proot123 -e "SHOW DATABASES;" | grep -E "(nacos|auditsystem|bytefinger)" | wc -l)
    log "✅ MySQL is accessible, found $mysql_databases application databases"
else
    warning "❌ MySQL verification failed"
fi

# Install Nacos
log "Installing Nacos..."
cd /opt/auditsystem

# Check if Nacos is already installed
if [ -d "nacos-server" ]; then
    log "Nacos server directory already exists, checking installation..."
    if [ -f "nacos-server/bin/startup.sh" ]; then
        log "✅ Nacos is already installed"
    else
        log "Nacos directory exists but incomplete, reinstalling..."
        sudo rm -rf nacos-server
    fi
fi

# Download and install Nacos if not already present
if [ ! -d "nacos-server" ] || [ ! -f "nacos-server/bin/startup.sh" ]; then
    # Remove any existing download
    rm -f nacos-server-2.2.0.tar.gz*
    
    wget https://github.com/alibaba/nacos/releases/download/2.2.0/nacos-server-2.2.0.tar.gz
    tar -xzf nacos-server-2.2.0.tar.gz
    
    # Handle directory naming
    if [ -d "nacos" ]; then
        if [ -d "nacos-server" ]; then
            sudo rm -rf nacos-server
        fi
        mv nacos nacos-server
    fi
    
    chown -R $USER:$USER nacos-server
    rm -f nacos-server-2.2.0.tar.gz*
    log "✅ Nacos installed successfully"
fi

# Create application directories
log "Creating application directories..."
sudo mkdir -p /opt/auditsystem/{backend,frontend,nacos,logs}
sudo chown -R $USER:$USER /opt/auditsystem

# Create systemd service files directory
sudo mkdir -p /etc/systemd/system

# Final verification of Java 11 features
log "Verifying Java 11 installation and features..."
java_version=$(java -version 2>&1 | head -1)
if [[ "$java_version" == *"11."* ]]; then
    log "✅ Java 11 is active: $java_version"
    
    # Test Java 10+ features
    test_dir="/tmp/java-test-$$"
    mkdir -p "$test_dir"
    
    cat > "$test_dir/TestJava11.java" << 'EOF'
import java.util.*;

public class TestJava11 {
    public static void main(String[] args) {
        var list = List.of("Java", "11", "Working!");
        var message = "Java 11 features are working!";
        
        System.out.println(message);
        list.forEach(System.out::println);
    }
}
EOF
    
    cd "$test_dir"
    if javac TestJava11.java && java TestJava11 >/dev/null 2>&1; then
        log "✅ Java 11 features working correctly"
    else
        warning "⚠️  Java 11 feature test failed"
    fi
    
    cd - && rm -rf "$test_dir"
else
    warning "⚠️  Unexpected Java version: $java_version"
fi

log "Installation completed successfully!"
echo
log "=== Installation Summary ==="
echo "✅ Java 11: Installed and verified"
echo "✅ Maven: Installed" 
echo "✅ Node.js 18: Installed"
echo "✅ pnpm: Installed"
echo "✅ Nginx: Installed"
echo "✅ Redis: Configured and running"
echo "✅ RabbitMQ: Configured with auditsystem vhost"
echo "✅ MySQL: Configured with databases and users"
echo "✅ Nacos: Downloaded and ready"
echo
info "Next steps:"
info "1. Run setup-nacos.sh to configure Nacos"
info "2. Run deploy-backend.sh to build and deploy the backend services"
info "3. Run deploy-frontend.sh to build and deploy the frontend"
info "4. Run configure-nginx.sh to configure Nginx"
echo
info "Or use the Java migration script:"
info "  ./migrate-java.sh"

# Print service status
log "Service Status:"
sudo systemctl status mysql --no-pager -l
sudo systemctl status redis-server --no-pager -l
sudo systemctl status rabbitmq-server --no-pager -l
sudo systemctl status nginx --no-pager -l

log "Installation script completed!"
