#!/bin/bash

# Configure Nginx for AuditSystem
# This script configures Nginx to serve the frontend and proxy API requests

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

# Backup existing nginx configuration
log "Backing up existing nginx configuration..."
sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.backup.$(date +%Y%m%d_%H%M%S)

# Create main nginx configuration
log "Creating nginx configuration..."
sudo tee /etc/nginx/nginx.conf > /dev/null << 'EOF'
user www-data;
worker_processes auto;
pid /run/nginx.pid;
include /etc/nginx/modules-enabled/*.conf;

events {
    worker_connections 1024;
    use epoll;
    multi_accept on;
}

http {
    ##
    # Basic Settings
    ##
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    types_hash_max_size 2048;
    client_max_body_size 100M;
    server_names_hash_bucket_size 64;

    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    ##
    # SSL Settings
    ##
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_prefer_server_ciphers on;

    ##
    # Logging Settings
    ##
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                   '$status $body_bytes_sent "$http_referer" '
                   '"$http_user_agent" "$http_x_forwarded_for" '
                   'rt=$request_time uct="$upstream_connect_time" '
                   'uht="$upstream_header_time" urt="$upstream_response_time"';

    access_log /var/log/nginx/access.log main;
    error_log /var/log/nginx/error.log;

    ##
    # Gzip Settings
    ##
    gzip on;
    gzip_vary on;
    gzip_proxied any;
    gzip_comp_level 6;
    gzip_types
        text/plain
        text/css
        text/xml
        text/javascript
        application/json
        application/javascript
        application/xml+rss
        application/atom+xml
        image/svg+xml;

    ##
    # Upstream Backend Services
    ##
    upstream backend_gateway {
        server 127.0.0.1:8200 max_fails=3 fail_timeout=30s;
    }

    upstream backend_auth {
        server 127.0.0.1:8300 max_fails=3 fail_timeout=30s;
    }

    upstream backend_system {
        server 127.0.0.1:8400 max_fails=3 fail_timeout=30s;
    }

    upstream backend_biz {
        server 127.0.0.1:8500 max_fails=3 fail_timeout=30s;
    }

    upstream backend_job {
        server 127.0.0.1:8600 max_fails=3 fail_timeout=30s;
    }

    upstream backend_report {
        server 127.0.0.1:8700 max_fails=3 fail_timeout=30s;
    }

    ##
    # Rate Limiting
    ##
    limit_req_zone $binary_remote_addr zone=api:10m rate=10r/s;
    limit_req_zone $binary_remote_addr zone=auth:10m rate=5r/s;

    ##
    # Virtual Host Configs
    ##
    include /etc/nginx/conf.d/*.conf;
    include /etc/nginx/sites-enabled/*;
}
EOF

# Remove default site
sudo rm -f /etc/nginx/sites-enabled/default

# Create AuditSystem site configuration
log "Creating AuditSystem site configuration..."
sudo tee /etc/nginx/sites-available/auditsystem > /dev/null << 'EOF'
server {
    listen 80 default_server;
    listen [::]:80 default_server;
    
    server_name _;
    root /var/www/auditsystem;
    index index.html;

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;

    # Frontend static files
    location / {
        try_files $uri $uri/ /index.html;
        expires 1h;
        add_header Cache-Control "public, immutable";
    }

    # Static assets with longer cache
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 30d;
        add_header Cache-Control "public, immutable";
        access_log off;
    }

    # API Gateway proxy
    location /api/ {
        limit_req zone=api burst=20 nodelay;
        
        proxy_pass http://backend_gateway/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
        
        proxy_buffering on;
        proxy_buffer_size 4k;
        proxy_buffers 8 4k;
    }

    # Authentication service direct access
    location /auth/ {
        limit_req zone=auth burst=10 nodelay;
        
        proxy_pass http://backend_auth/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # System service direct access
    location /system/ {
        limit_req zone=api burst=20 nodelay;
        
        proxy_pass http://backend_system/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # Business service direct access
    location /biz/ {
        limit_req zone=api burst=20 nodelay;
        
        proxy_pass http://backend_biz/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # Job service direct access
    location /job/ {
        limit_req zone=api burst=20 nodelay;
        
        proxy_pass http://backend_job/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # Report service direct access
    location /report/ {
        limit_req zone=api burst=20 nodelay;
        
        proxy_pass http://backend_report/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # Nacos console access (for administration)
    location /nacos/ {
        proxy_pass http://127.0.0.1:8848/nacos/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # Health check endpoint
    location /health {
        access_log off;
        return 200 "healthy\n";
        add_header Content-Type text/plain;
    }

    # Deny access to sensitive files
    location ~ /\. {
        deny all;
        access_log off;
        log_not_found off;
    }

    location ~ ~$ {
        deny all;
        access_log off;
        log_not_found off;
    }

    # Error pages
    error_page 404 /404.html;
    error_page 500 502 503 504 /50x.html;
    
    location = /50x.html {
        root /var/www/auditsystem;
    }
}
EOF

# Enable the site
log "Enabling AuditSystem site..."
sudo ln -sf /etc/nginx/sites-available/auditsystem /etc/nginx/sites-enabled/

# Create custom error pages
log "Creating custom error pages..."
sudo tee /var/www/auditsystem/50x.html > /dev/null << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <title>Service Temporarily Unavailable</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            text-align: center; 
            margin-top: 100px; 
            background-color: #f5f5f5; 
        }
        .error-container { 
            background: white; 
            padding: 40px; 
            margin: 0 auto; 
            max-width: 500px; 
            border-radius: 8px; 
            box-shadow: 0 2px 10px rgba(0,0,0,0.1); 
        }
        h1 { color: #e74c3c; }
        p { color: #666; }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Service Temporarily Unavailable</h1>
        <p>The AuditSystem is currently experiencing technical difficulties.</p>
        <p>Please try again in a few moments.</p>
    </div>
</body>
</html>
EOF

# Test nginx configuration
log "Testing nginx configuration..."
if sudo nginx -t; then
    log "Nginx configuration is valid"
else
    error "Nginx configuration test failed"
    exit 1
fi

# Restart nginx
log "Restarting nginx..."
sudo systemctl restart nginx
sudo systemctl enable nginx

# Create log rotation for application logs
log "Setting up log rotation..."
sudo tee /etc/logrotate.d/auditsystem > /dev/null << 'EOF'
/opt/auditsystem/logs/*/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 auditsystem auditsystem
    postrotate
        systemctl reload nginx > /dev/null 2>&1 || true
    endscript
}
EOF

# Set up firewall rules (if ufw is installed)
if command -v ufw >/dev/null 2>&1; then
    log "Configuring firewall rules..."
    sudo ufw allow 80/tcp
    sudo ufw allow 22/tcp
    info "Firewall configured to allow HTTP (80) and SSH (22)"
fi

log "Nginx configuration completed!"
info "AuditSystem is now accessible at: http://your-server-ip"
info "Nacos Console available at: http://your-server-ip/nacos"
info "Health check endpoint: http://your-server-ip/health"

# Check nginx status
if sudo systemctl is-active --quiet nginx; then
    log "Nginx is running successfully"
else
    error "Nginx is not running. Check logs: sudo journalctl -u nginx -f"
    exit 1
fi

# Display service endpoints
info "Service endpoints:"
info "  Frontend: http://localhost (port 80)"
info "  API Gateway: http://localhost/api/ -> http://localhost:8200"
info "  Auth Service: http://localhost/auth/ -> http://localhost:8300"
info "  System Service: http://localhost/system/ -> http://localhost:8400"
info "  Business Service: http://localhost/biz/ -> http://localhost:8500"
info "  Job Service: http://localhost/job/ -> http://localhost:8600"
info "  Report Service: http://localhost/report/ -> http://localhost:8700"
info "  Nacos Console: http://localhost/nacos/ -> http://localhost:8848"
