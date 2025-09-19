#!/bin/bash

# Deploy Frontend Application
# This script builds and deploys the Vue.js frontend application

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

# Clone or use existing frontend source
if [ -n "$DOCKER_CONTAINER" ] || [ ! -d "/Users/luiswing/Desktop/GTI/AuditSystemFrontend" ]; then
    # Running in Docker or source not available locally - clone from GitHub
    log "Cloning AuditSystem frontend repository (dev-luis branch)..."
    cd /opt/auditsystem
    if [ -d "AuditSystemFrontend" ]; then
        rm -rf AuditSystemFrontend
    fi
    git clone -b dev-luis https://oauth2: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@github.com/GTI-CTU/AuditSystemFrontend.git || {
        error "Failed to clone repository. Please check your internet connection and repository access."
        exit 1
    }
    FRONTEND_SOURCE="/opt/auditsystem/AuditSystemFrontend"
else
    # Use local source and ensure we're on dev-luis branch
    FRONTEND_SOURCE="/Users/luiswing/Desktop/GTI/AuditSystemFrontend"
    if [ ! -d "$FRONTEND_SOURCE" ]; then
        error "Frontend source directory not found: $FRONTEND_SOURCE"
        exit 1
    fi
    
    log "Switching to dev-luis branch in local frontend source..."
    cd "$FRONTEND_SOURCE"
    
    # Fetch latest changes and switch to dev-luis branch
    git fetch origin || {
        error "Failed to fetch latest changes from repository"
        exit 1
    }
    
    # Switch to dev-luis branch (create local branch if it doesn't exist)
    if git show-ref --verify --quiet refs/heads/dev-luis; then
        log "Switching to existing dev-luis branch..."
        git checkout dev-luis
        git pull origin dev-luis
    else
        log "Creating and switching to dev-luis branch..."
        git checkout -b dev-luis origin/dev-luis
    fi
    
    # Create frontend deployment directory
    mkdir -p /opt/auditsystem/frontend
    cd /opt/auditsystem/frontend
    
    # Copy frontend source code
    log "Copying frontend source code..."
    if [ -d "AuditSystemFrontend" ]; then
        rm -rf AuditSystemFrontend
    fi
    cp -r "$FRONTEND_SOURCE" ./AuditSystemFrontend
fi

cd "$FRONTEND_SOURCE"

# Install dependencies
log "Installing frontend dependencies..."
npm install

# Build the frontend application
log "Building frontend application..."
if NODE_OPTIONS="--max-old-space-size=4096" npm run build; then
    log "Build successful!"
else
    error "Build failed!"
    error "Build errors:"
    NODE_OPTIONS="--max-old-space-size=4096" npm run build
    exit 1
fi

# Verify dist directory was created
if [ ! -d "dist" ]; then
    error "Build completed but no 'dist' directory found!"
    exit 1
fi

log "Build completed successfully."

# Create nginx web directory
sudo mkdir -p /var/www/auditsystem
sudo rm -rf /var/www/auditsystem/*

# Copy built files to nginx directory
log "Copying built files to web directory..."
sudo cp -r dist/* /var/www/auditsystem/
sudo chown -R www-data:www-data /var/www/auditsystem
sudo chmod -R 755 /var/www/auditsystem

# Create a simple index.html fallback for SPA routing
log "Creating SPA routing fallback..."
sudo tee /var/www/auditsystem/404.html > /dev/null << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>AuditSystem</title>
    <script>
        // Redirect 404s to index.html for SPA routing
        window.location.href = '/';
    </script>
</head>
<body>
    <p>Redirecting...</p>
</body>
</html>
EOF

log "Frontend deployment completed!"
info "Frontend files deployed to: /var/www/auditsystem"
info "Next step: Configure Nginx with configure-nginx.sh"

# Create a development server service (optional)
log "Creating development server service (optional)..."
sudo tee /etc/systemd/system/auditsystem-frontend-dev.service > /dev/null << EOF
[Unit]
Description=AuditSystem Frontend Development Server
After=network.target

[Service]
Type=simple
User=$USER
Group=$USER
WorkingDirectory=/opt/auditsystem/frontend/AuditSystemFrontend
ExecStart=/usr/bin/npm run dev
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=auditsystem-frontend-dev
Environment=NODE_ENV=development

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable auditsystem-frontend-dev

info "Development server service created: auditsystem-frontend-dev"
info "To start dev server: sudo systemctl start auditsystem-frontend-dev"
info "Development server will run on: http://localhost:3000"
