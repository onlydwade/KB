#!/bin/bash

# Setup Nacos Server (Native Installation)
# This script configures Nacos to run natively on Ubuntu

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

# Check if Nacos is installed
if [ ! -d "/opt/auditsystem/nacos-server" ]; then
    error "Nacos is not installed. Please run install-ubuntu-setup.sh first."
    exit 1
fi

log "Setting up Nacos server..."

# Fix: Create Nacos log directories (discovered issue - Nacos fails without this)
log "Creating Nacos log directories..."
sudo mkdir -p /logs/access_log
sudo chown -R azureuser:azureuser /logs
info "✅ Created /logs/access_log with proper permissions"

# Check if local MySQL is running
log "Testing local MySQL connectivity..."
if ! sudo systemctl is-active --quiet mysql; then
    error "Local MySQL service is not running. Starting MySQL..."
    sudo systemctl start mysql
    sleep 5
    if ! sudo systemctl is-active --quiet mysql; then
        error "Failed to start MySQL service. Please check MySQL installation."
        exit 1
    fi
fi

log "Testing local MySQL connection..."
if ! sudo mysql -e "SELECT 1 as test;" > /dev/null 2>&1; then
    error "Cannot connect to local MySQL database. Please check MySQL installation and permissions."
    info "You may need to run: sudo mysql_secure_installation"
    exit 1
else
    log "Local MySQL connection successful!"
fi

# Create Nacos MySQL schema
log "Creating Nacos database schema..."
cat > /tmp/nacos-mysql-schema.sql << 'EOF'
/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/******************************************/
/*   Database: nacos                     */
/******************************************/
USE nacos;

/******************************************/
/*   Table: config_info                   */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) DEFAULT NULL COMMENT 'group_id',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text COMMENT '配置的模式',
  `encrypted_data_key` text NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   Table: config_info_aggr              */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';

/******************************************/
/*   Table: config_info_beta              */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   Table: config_info_tag               */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   Table: config_tags_relation          */
/******************************************/
CREATE TABLE IF NOT EXISTS `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   Table: group_capacity                */
/******************************************/
CREATE TABLE IF NOT EXISTS `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   Table: his_config_info               */
/******************************************/
CREATE TABLE IF NOT EXISTS `his_config_info` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `op_type` char(10) DEFAULT NULL COMMENT 'operation type',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '密钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

/******************************************/
/*   Table: tenant_capacity               */
/******************************************/
CREATE TABLE IF NOT EXISTS `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';

CREATE TABLE IF NOT EXISTS `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE IF NOT EXISTS `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY,
	`password` varchar(500) NOT NULL,
	`enabled` boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS `roles` (
	`username` varchar(50) NOT NULL,
	`role` varchar(50) NOT NULL,
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE IF NOT EXISTS `permissions` (
    `role` varchar(50) NOT NULL,
    `resource` varchar(255) NOT NULL,
    `action` varchar(8) NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT IGNORE INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);
INSERT IGNORE INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
EOF

# Execute the SQL script on local MySQL
log "Creating nacos database and user if they don't exist..."
sudo mysql -e "CREATE DATABASE IF NOT EXISTS nacos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
sudo mysql -e "CREATE USER IF NOT EXISTS 'auditsystem'@'localhost' IDENTIFIED BY 'audit123';"
sudo mysql -e "GRANT ALL PRIVILEGES ON *.* TO 'auditsystem'@'localhost' WITH GRANT OPTION;"
sudo mysql -e "GRANT ALL PRIVILEGES ON nacos.* TO 'auditsystem'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"

log "Applying schema to nacos database..."
mysql -u auditsystem -p'audit123' nacos < /tmp/nacos-mysql-schema.sql
rm /tmp/nacos-mysql-schema.sql

# Initialize Audit System Database
log "Setting up Audit System database..."
sudo mysql -e "CREATE DATABASE IF NOT EXISTS bytefinger_toutuo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
sudo mysql -e "GRANT ALL PRIVILEGES ON bytefinger_toutuo.* TO 'auditsystem'@'localhost';"
sudo mysql -e "FLUSH PRIVILEGES;"

# Check if minimal SQL setup file exists and apply it
if [ -f "sql-files/minimal-admin-setup.sql" ]; then
    log "Applying minimal audit system database setup..."
    sudo mysql < sql-files/minimal-admin-setup.sql
    log "✅ Audit system database initialized with admin user"
    info "Admin login credentials:"
    info "  Username: admin"
    info "  Password: admin123"
    info "  Database: bytefinger_toutuo"
else
    info "⚠️  Minimal SQL setup file not found at sql-files/minimal-admin-setup.sql"
    info "   You can manually apply the database setup later"
fi

# Configure Nacos
log "Configuring Nacos application.properties..."
cat > /opt/auditsystem/nacos-server/conf/application.properties << 'EOF'
#*************** Spring Boot Related Configurations ***************#
### Default web context path:
server.servlet.contextPath=/nacos
### Default web server port:
server.port=8848

#*************** Network Related Configurations ***************#
### If prefer hostname over ip for Nacos server addresses in cluster.conf:
# nacos.inetutils.prefer-hostname-over-ip=false

### Specify local server's IP:
# nacos.inetutils.ip-address=

#*************** Config Module Related Configurations ***************#
### If use MySQL as datasource:
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://localhost:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=auditsystem
db.password.0=audit123

### Connection pool configuration: hikariCP
db.pool.config.connectionTimeout=30000
db.pool.config.validationTimeout=10000
db.pool.config.maximumPoolSize=20
db.pool.config.minimumIdle=2

#*************** Naming Module Related Configurations ***************#
### Data dispatch task execution period in milliseconds:
# nacos.naming.distro.taskDispatchPeriod=200

### Data count of batch sync task:
# nacos.naming.distro.batchSyncKeyCount=1000

### Retry delay in milliseconds if sync task failed:
# nacos.naming.distro.syncRetryDelay=5000

### If enable data warmup. If set to false, the server would accept request without local data preparation:
# nacos.naming.data.warmup=true

### If enable the instance auto expiration, kind like of health check of instance:
# nacos.naming.expireInstance=true

### will be removed and replaced by `nacos.naming.clean` properties
nacos.naming.empty-service.auto-clean=true
nacos.naming.empty-service.clean.initial-delay-ms=50000
nacos.naming.empty-service.clean.period-time-ms=30000

### Add in 2.0.0
### The interval to clean empty service, unit: milliseconds.
# nacos.naming.clean.empty-service.interval=60000

### The NamingService cache size in cluster environment.
# nacos.naming.cache.registry.size=10000

#*************** CMDB Module Related Configurations ***************#
### The interval to dump external CMDB in milliseconds:
# nacos.cmdb.dumpTaskInterval=3600000

### The interval of polling data change event in milliseconds:
# nacos.cmdb.eventTaskInterval=10000

### The interval of loading labels in milliseconds:
# nacos.cmdb.labelTaskInterval=300000

### If turn on data loading task so that the local cache will be updated when init:
# nacos.cmdb.loadDataAtStart=false

#*************** Metrics Related Configurations ***************#
### Metrics for prometheus
#management.endpoints.web.exposure.include=*

### Metrics for elastic search
management.metrics.export.elastic.enabled=false
#management.metrics.export.elastic.host=http://localhost:9200

### Metrics for influx
management.metrics.export.influx.enabled=false
#management.metrics.export.influx.db=springboot
#management.metrics.export.influx.uri=http://localhost:8086
#management.metrics.export.influx.auto-create-db=true
#management.metrics.export.influx.consistency=one
#management.metrics.export.influx.compressed=true

#*************** Access Log Related Configurations ***************#
### If turn on the access log:
server.tomcat.accesslog.enabled=true

### The access log pattern:
server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D %{User-Agent}i %{Request-Source}i

### The directory of access log:
server.tomcat.accesslog.directory=logs/access_log

#*************** Access Control Related Configurations ***************#
### If enable spring security, this option is deprecated in 1.2.0:
#spring.security.enabled=false

### The ignore urls of auth, is deprecated in 1.2.0:
nacos.security.ignore.urls=/,/error,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-ui/public/**,/v1/auth/**,/v1/console/health/**,/actuator/**,/v1/console/server/**

### The auth system to use, currently only 'nacos' and 'ldap' is supported:
nacos.core.auth.system.type=nacos

### If turn on auth system:
nacos.core.auth.enabled=true

### The token expiration in seconds:
nacos.core.auth.default.token.expire.seconds=18000

### The default token:
nacos.core.auth.default.token.secret.key=SecretKey012345678901234567890123456789012345678901234567890123456789

### Turn on/off caching of auth information. By turning on this switch, the update of auth information would have a 15 seconds delay.
nacos.core.auth.caching.enabled=true

### Since 1.4.1, Turn on/off white auth for user-agent: nacos-server, only for upgrade from old version.
nacos.core.auth.enable.userAgentAuthWhite=false

### Since 1.4.1, worked when nacos.core.auth.enabled=true and nacos.core.auth.enable.userAgentAuthWhite=false.
### The two properties is the white list for auth and used by identity the request from other server.
nacos.core.auth.server.identity.key=serverIdentity
nacos.core.auth.server.identity.value=security

### worked when nacos.core.auth.system.type=nacos
### The token expiration in seconds:
nacos.core.auth.plugin.nacos.token.expire.seconds=18000

### worked when nacos.core.auth.system.type=nacos
### The token secret key:
nacos.core.auth.plugin.nacos.token.secret.key=SecretKey012345678901234567890123456789012345678901234567890123456789

#*************** Istio Related Configurations ***************#
### If turn on the MCP server:
nacos.istio.mcp.server.enabled=false
EOF

# Create systemd service for Nacos
log "Creating Nacos systemd service..."
sudo tee /etc/systemd/system/nacos.service > /dev/null << EOF
[Unit]
Description=Nacos Server
After=network.target mysql.service

[Service]
Type=forking
User=$USER
Group=$USER
ExecStart=/opt/auditsystem/nacos-server/bin/startup.sh -m standalone
ExecStop=/opt/auditsystem/nacos-server/bin/shutdown.sh
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=nacos
Environment=JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

[Install]
WantedBy=multi-user.target
EOF

# Make startup script executable
chmod +x /opt/auditsystem/nacos-server/bin/startup.sh
chmod +x /opt/auditsystem/nacos-server/bin/shutdown.sh

# Test local MySQL connection before starting Nacos
log "Testing local MySQL connection..."
if ! mysql -u auditsystem -p'audit123' -e "SELECT 1;" > /dev/null 2>&1; then
    error "Cannot connect to local MySQL database. Please check credentials and MySQL service."
    exit 1
fi

# Enable and start Nacos service
log "Starting Nacos service..."

# DEBUG: Check log directory permissions before starting
info "DEBUG: Checking log directory permissions..."
ls -la /logs/ || info "No /logs directory found"
ls -la /logs/access_log/ || info "No /logs/access_log directory found"

# DEBUG: Check nacos configuration file
info "DEBUG: Checking Nacos configuration file..."
if [ -f "/opt/auditsystem/nacos-server/conf/application.properties" ]; then
    info "✅ Nacos configuration file exists"
    info "Database configuration:"
    grep -E "db\.url|db\.user|db\.password" /opt/auditsystem/nacos-server/conf/application.properties || true
    info "Authentication configuration:"
    grep -E "auth\.enabled|auth\.plugin" /opt/auditsystem/nacos-server/conf/application.properties || true
else
    error "❌ Nacos configuration file missing!"
fi

# DEBUG: Check Java installation
info "DEBUG: Checking Java installation..."
java -version 2>&1 | head -3

# DEBUG: Check Nacos startup script
info "DEBUG: Checking Nacos startup script permissions..."
ls -la /opt/auditsystem/nacos-server/bin/startup.sh

sudo systemctl daemon-reload
sudo systemctl enable nacos

# Stop any existing Nacos process
log "Stopping any existing Nacos processes..."
sudo systemctl stop nacos 2>/dev/null || true
sleep 3

# DEBUG: Check for any remaining Java processes
info "DEBUG: Checking for any remaining Nacos processes..."
ps aux | grep nacos | grep -v grep || info "No Nacos processes found"

# Start Nacos
log "Starting Nacos service..."
sudo systemctl start nacos

# Wait a moment for startup
sleep 5

# Check if the service started successfully
if ! sudo systemctl is-active --quiet nacos; then
    error "Nacos service failed to start. Checking detailed status..."
    sudo systemctl status nacos --no-pager -l
    
    error "Checking Nacos logs..."
    sudo journalctl -u nacos --no-pager -l --since "5 minutes ago" | tail -20
    
    error "Checking system logs for Nacos..."
    tail -20 /var/log/syslog | grep nacos || true
    
    error "Checking if Nacos startup script can be executed manually..."
    cd /opt/auditsystem/nacos-server
    ls -la bin/startup.sh
    info "Attempting manual startup test (this may fail, but shows errors)..."
    sudo -u $USER /opt/auditsystem/nacos-server/bin/startup.sh -m standalone || true
    
    exit 1
fi

# Wait for Nacos to start
log "Waiting for Nacos to start up..."
sleep 30

# DEBUG: More thorough Nacos startup verification
info "DEBUG: Checking Nacos process status..."
ps aux | grep nacos | grep -v grep || info "No Nacos processes in ps output"

info "DEBUG: Checking Nacos port 8848..."
netstat -tlnp | grep 8848 || info "Port 8848 not listening"

info "DEBUG: Testing Nacos health endpoint..."
curl -v http://localhost:8848/nacos/actuator/health || info "Health endpoint not responding"

info "DEBUG: Testing Nacos main page..."
curl -I http://localhost:8848/nacos/ || info "Main page not responding"

# Check if Nacos is running
if curl -f http://localhost:8848/nacos/ > /dev/null 2>&1; then
    log "Nacos started successfully!"
    info "Nacos Console: http://localhost:8848/nacos"
    info "Default username: nacos"
    info "Default password: nacos"
    info "Local MySQL database: nacos on localhost:3306"
    
    # DEBUG: Test authentication
    info "DEBUG: Testing Nacos authentication..."
    curl -X POST "http://localhost:8848/nacos/v1/auth/login" -d "username=nacos&password=nacos" | head -c 200 || info "Authentication test failed"
    
    # Apply Nacos Configuration
    log "Applying Nacos microservices configuration..."
    
    # Check if nacos-config setup script exists
    if [ -f "nacos-config/setup-commands.sh" ]; then
        info "Found nacos-config setup script, applying configurations..."
        
        # Wait additional time for Nacos to be fully ready
        log "Waiting additional 15 seconds for Nacos to be fully ready..."
        sleep 15
        
        # Execute the configuration script
        cd nacos-config
        chmod +x setup-commands.sh
        
        # Run the configuration setup
        ./setup-commands.sh
        
        if [ $? -eq 0 ]; then
            log "✅ Nacos configurations applied successfully!"
            
            # CRITICAL FIX: Rebuild backend to compile updated bootstrap.yml files
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
            
            # Verify configurations are actually in Nacos
            log "Verifying Nacos configuration count..."
            TOKEN=$(curl -s -X POST "http://localhost:8848/nacos/v1/auth/login" -d "username=nacos&password=nacos" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)
            if [ -n "$TOKEN" ]; then
                CONFIG_COUNT=$(curl -s "http://localhost:8848/nacos/v1/cs/configs?search=accurate&pageNo=1&pageSize=200&accessToken=${TOKEN}" | grep -o '"totalCount":[0-9]*' | cut -d':' -f2)
                if [ -z "$CONFIG_COUNT" ]; then
                    # Fallback: count dataId occurrences
                    CONFIG_COUNT=$(curl -s "http://localhost:8848/nacos/v1/cs/configs?search=accurate&pageNo=1&pageSize=200&accessToken=${TOKEN}" | grep -o '"dataId"' | wc -l)
                fi
                if [ -n "$CONFIG_COUNT" ] && [ "$CONFIG_COUNT" -ge 7 ]; then
                    info "✅ Verified: $CONFIG_COUNT configurations found in Nacos"
                    info "All microservice configurations are now available in Nacos"
                else
                    warn "⚠️  Only $CONFIG_COUNT configurations found (expected 7)"
                    info "You may need to manually verify in Nacos console"
                fi
            else
                warn "⚠️  Could not verify configuration count due to authentication"
            fi
        else
            error "❌ Failed to apply some Nacos configurations"
            info "You can manually run: cd nacos-config && ./setup-commands.sh"
        fi
        
        cd ..
    else
        info "⚠️  Nacos configuration script not found at nacos-config/setup-commands.sh"
        info "   You can manually apply configurations later using:"
        info "   cd nacos-config && ./setup-commands.sh"
    fi
    
else
    error "Failed to start Nacos. Running comprehensive diagnostics..."
    
    error "=== NACOS STARTUP FAILURE DIAGNOSTICS ==="
    
    error "1. Systemd service status:"
    sudo systemctl status nacos --no-pager -l || true
    
    error "2. Recent systemd journal logs:"
    sudo journalctl -u nacos -n 50 --no-pager || true
    
    error "3. System logs (checking for nacos):"
    grep -i nacos /var/log/syslog | tail -10 || info "No nacos entries in syslog"
    
    error "4. Java processes:"
    ps aux | grep java || info "No Java processes found"
    
    error "5. Port 8848 status:"
    sudo netstat -tlnp | grep 8848 || info "Port 8848 not listening"
    
    error "6. Nacos working directory and permissions:"
    ls -la /opt/auditsystem/nacos-server/ || true
    ls -la /opt/auditsystem/nacos-server/bin/ || true
    ls -la /opt/auditsystem/nacos-server/conf/ || true
    
    error "7. Log directory status:"
    ls -la /logs/ || info "No /logs directory"
    ls -la /logs/access_log/ || info "No /logs/access_log directory"
    
    error "8. Database connectivity test:"
    mysql -e "SELECT 1 as db_test;" 2>&1 || info "Database connection failed"
    
    error "9. Manual startup attempt (for better error messages):"
    cd /opt/auditsystem/nacos-server
    info "Working directory: $(pwd)"
    info "Attempting manual startup..."
    ./bin/startup.sh -m standalone 2>&1 | head -20 || info "Manual startup failed"
    
    error "10. Checking Java version and JAVA_HOME:"
    java -version 2>&1 | head -3
    echo "JAVA_HOME: $JAVA_HOME"
    ls -la /usr/lib/jvm/java-8-openjdk-amd64/ || info "Java 8 not found in expected location"
    
    exit 1
fi

log "Nacos setup completed!"
log "✅ Setup Summary:"
info "  • Nacos Server: http://localhost:8848/nacos (nacos/nacos)"
info "  • Audit Database: bytefinger_toutuo (auditsystem/audit123)"
info "  • Admin Login: admin/admin123"
info "  • Microservice Configurations: Applied to Nacos"
info ""
info "Next steps:"
info "  1. Deploy backend services: ./deploy-backend.sh"
info "  2. Deploy frontend: ./deploy-frontend.sh"
info "  3. Configure Nginx: ./configure-nginx.sh"
