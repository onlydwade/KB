# AuditSystem VM Deployment Package

This package contains all the required scripts and documentation to deploy the complete AuditSystem on a bare Ubuntu 24.04 VM without Docker.

> **📝 Latest Update**: All critical fixes discovered during deployment have been systematically integrated into automation scripts. See `TROUBLESHOOTING.md` for details on resolved issues including Nacos log directory permissions, bootstrap.yml compilation requirements, and authentication fixes.

## 📋 Prerequisites

- **Operating System**: Ubuntu 24.04LTS (Server or Desktop)
- **Memory**: Minimum 8GB RAM recommended
- **Storage**: At least 20GB free disk space
- **Network**: Internet access for downloading packages and repositories
- **User Privileges**: Non-root user with sudo privileges
- **Git Access**: Access to the GitHub repositories (GTI-CTU/AuditSystemBackend and GTI-CTU/AuditSystemFrontend)

## 📁 Package Contents

```
AuditSystemVM/
├── README.md                       # This documentation
├── QUICK-START.md                 # Quick deployment guide
├── install-ubuntu-setup.sh        # System setup and dependencies (Java 11)
├── check-java-compatibility.sh    # Java 11 compatibility verification
├── setup-nacos.sh                # Nacos service discovery setup
├── deploy-backend.sh             # Backend microservices deployment
├── deploy-frontend.sh            # Frontend Vue.js application
├── configure-nginx.sh            # Nginx web server configuration
├── manage-services.sh            # Service lifecycle management
├── azure-monitor-deployment/     # Azure Monitor integration
│   ├── README.md                 # Azure Monitor setup guide
│   ├── mac-azure-monitor-setup.sh # Main Azure Monitor setup (Mac-side)
│   ├── vm-health-monitor.sh      # Health monitoring daemon (VM-side)
│   ├── configure-azure-monitor-agent.sh # Azure agent installer
│   └── DEPLOYMENT_INSTRUCTIONS.md # Detailed deployment guide
└── troubleshooting/              # Troubleshooting guides
    ├── common-issues.md          # Common problems and solutions
    ├── service-logs.md           # How to check service logs
    └── port-conflicts.md         # Resolving port conflicts
```

## 🚀 Quick Deployment

### One-Command Deployment
```bash
cd AuditSystemVM
chmod +x *.sh
./install-ubuntu-setup.sh && ./check-java-compatibility.sh && ./setup-nacos.sh && ./deploy-backend.sh && ./deploy-frontend.sh && ./configure-nginx.sh && ./manage-services.sh start
```

### Step-by-Step Deployment

#### 0. Java Compatibility Check (Optional but Recommended)
```bash
./check-java-compatibility.sh
```

#### 1. System Setup (5-10 minutes)
```bash
./install-ubuntu-setup.sh
```
**What it does:**
- Updates Ubuntu packages
- Installs Java 11 OpenJDK (supports Java 10+ features)
- Installs Maven 3.9
- Installs Node.js 18 LTS and pnpm
- Sets up MySQL 8.0 with root/root123 credentials
- Configures Redis server
- Sets up RabbitMQ server
- Installs and configures Nginx
- Creates necessary directories and users

#### 2. Service Discovery Setup (2-3 minutes)
```bash
./setup-nacos.sh
```
**What it does:**
- Downloads Nacos 2.2.0
- Configures standalone mode with MySQL backend
- Creates systemd service
- Sets up admin credentials (nacos/nacos)
- **Initializes audit system database (bytefinger_toutuo)**
- **Creates admin user (admin/admin123) for application access**

#### 3. Backend Deployment (10-15 minutes)
```bash
./deploy-backend.sh
```
**What it does:**
- Clones/updates AuditSystemBackend repository
- Builds all 6 microservices with Maven
- Creates systemd services for each microservice:
  - `auditsystem-auth` (Port 8300)
  - `auditsystem-system` (Port 8400)
  - `auditsystem-biz` (Port 8500)
  - `auditsystem-job` (Port 8600)
  - `auditsystem-report` (Port 8700)
  - `auditsystem-gateway` (Port 8200)

#### 4. Frontend Deployment (5-8 minutes)
```bash
./deploy-frontend.sh
```
**What it does:**
- Clones/updates AuditSystemFrontend repository
- Builds Vue.js application with pnpm
- Optimizes build with 4GB memory allocation
- Deploys to Nginx web root

#### 5. Web Server Configuration (1-2 minutes)
```bash
./configure-nginx.sh
```
**What it does:**
- Configures Nginx for Vue.js SPA routing
- Sets up API reverse proxy to backend gateway
- Optimizes static file serving
- Enables gzip compression

#### 6. Service Management
```bash
# Start all services
./manage-services.sh start

# Check service status
./manage-services.sh status

# View service logs
./manage-services.sh logs [service-name]

# Restart all services
./manage-services.sh restart

# Stop all services
./manage-services.sh stop
```

## 🌐 Access Points

After successful deployment, you can access:

| Service | URL | Default Credentials |
|---------|-----|-------------------|
| **Frontend Application** | http://your-vm-ip | - |
| **Nacos Console** | http://your-vm-ip:8848/nacos | nacos/nacos |
| **API Gateway** | http://your-vm-ip:8200 | - |
| **Auth Service** | http://your-vm-ip:8300 | - |
| **System Service** | http://your-vm-ip:8400 | - |
| **Business Service** | http://your-vm-ip:8500 | - |
| **Job Service** | http://your-vm-ip:8600 | - |
| **Report Service** | http://your-vm-ip:8700 | - |

## 💾 Database Access

| Service | Connection | Credentials |
|---------|------------|-------------|
| **MySQL** | your-vm-ip:3306 | root/root123 |
| **Redis** | your-vm-ip:6379 | (no password) |
| **RabbitMQ** | your-vm-ip:5672 | guest/guest |
| **RabbitMQ Management** | http://your-vm-ip:15672 | guest/guest |

## � Admin Access

### Audit System Application
| Component | URL | Credentials |
|-----------|-----|-------------|
| **Application Login** | http://your-vm-ip (Frontend) | admin/admin123 |
| **Database (bytefinger_toutuo)** | your-vm-ip:3306 | auditsystem/audit123 |

### Infrastructure Components  
| Component | URL | Credentials |
|-----------|-----|-------------|
| **Nacos Management** | http://your-vm-ip:8848/nacos | nacos/nacos |

> **Note**: The audit system database (bytefinger_toutuo) is automatically initialized with minimal admin setup during Nacos configuration. The admin user credentials are: `admin/admin123`

## 📊 Azure Monitor Integration

For comprehensive health monitoring and alerting, deploy Azure Monitor integration:

### Quick Setup
```bash
cd azure-monitor-deployment
./mac-azure-monitor-setup.sh interactive-setup
```

**Features:**
- 🔍 **Real-time Health Monitoring**: Database, message queue, and microservice health checks
- 📈 **Azure Log Analytics**: Centralized logging and analytics
- 🚨 **Custom Alerts**: Automated notifications for service failures
- 📊 **Performance Metrics**: CPU, memory, disk, and network monitoring
- 🛡️ **Security**: Non-privileged health checks with timeout protection

**What it Monitors:**
- MySQL, Redis, RabbitMQ service status and connectivity
- All 6 microservices (Gateway, Auth, System, Business, Job, Report)
- System resources (CPU, memory, disk usage)
- Network connectivity and response times

For detailed setup instructions, see [`azure-monitor-deployment/README.md`](./azure-monitor-deployment/README.md)

## 🔧 Service Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Microservices │
│   (Nginx:80)    │───▶│   (Port 8200)   │───▶│   (8300-8700)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   Nacos         │    │   Infrastructure │
                       │   (Port 8848)   │    │   MySQL/Redis   │
                       └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
                                              ┌─────────────────┐
                                              │ Azure Monitor   │
                                              │ Health Checks   │
                                              └─────────────────┘
```

## 📊 Resource Requirements

### Minimum System Requirements
- **CPU**: 2 cores
- **RAM**: 8GB
- **Storage**: 20GB free space
- **Network**: 100Mbps internet connection

### Recommended System Requirements
- **CPU**: 4 cores
- **RAM**: 16GB
- **Storage**: 50GB SSD
- **Network**: 1Gbps internet connection

## 🛠️ Troubleshooting

### Common Issues

#### 1. Port Conflicts
```bash
# Check which process is using a port
sudo netstat -tulpn | grep :8200

# Kill process if needed
sudo kill -9 <PID>
```

#### 2. Service Won't Start
```bash
# Check service status
./manage-services.sh status

# View service logs
sudo journalctl -u auditsystem-gateway -f

# Restart specific service
sudo systemctl restart auditsystem-gateway
```

#### 3. Build Failures
```bash
# Check Java version (should be 11.x)
java -version

# Check Maven version
mvn -version

# Run Java compatibility check
./check-java-compatibility.sh

# Check Node.js version
node --version
```

#### 4. Database Connection Issues
```bash
# Test MySQL connection
mysql -u root -proot123 -e "SELECT VERSION();"

# Check MySQL service
sudo systemctl status mysql
```

### Log Locations

| Service | Log Location |
|---------|-------------|
| **Nacos** | `/opt/nacos/logs/` |
| **Microservices** | `/opt/auditsystem/logs/` |
| **Nginx** | `/var/log/nginx/` |
| **MySQL** | `/var/log/mysql/` |
| **System Services** | `journalctl -u <service-name>` |

## 🔄 Updates and Maintenance

### Update Backend Code
```bash
./deploy-backend.sh
./manage-services.sh restart backend
```

### Update Frontend Code
```bash
./deploy-frontend.sh
sudo systemctl reload nginx
```

### System Updates
```bash
sudo apt update && sudo apt upgrade -y
./manage-services.sh restart all
```

## 🆘 Support and Recovery

### Backup Important Data
```bash
# Backup MySQL databases
mysqldump -u root -proot123 --all-databases > backup.sql

# Backup Nacos configuration
tar -czf nacos-backup.tar.gz /opt/nacos/data/
```

### Complete System Reset
```bash
./manage-services.sh stop
sudo rm -rf /opt/auditsystem /opt/nacos
./install-ubuntu-setup.sh
# Then redeploy everything
```

## 📞 Getting Help

1. **Check this README** - Most common issues are covered here
2. **Review logs** - Use `./manage-services.sh logs` to check service logs
3. **Service status** - Use `./manage-services.sh status` to check all services
4. **System resources** - Use `htop` or `systemctl status` to check system load

## 🎯 Success Indicators

After deployment, you should see:
- ✅ All services showing as "RUNNING" in `./manage-services.sh status`
- ✅ Frontend accessible at http://your-vm-ip
- ✅ Nacos console accessible at http://your-vm-ip:8848/nacos
- ✅ All 6 microservices registered in Nacos
- ✅ No error messages in service logs

---

**Deployment Time**: Approximately 25-40 minutes on a standard VM
**Maintenance**: Regular updates recommended monthly
**Support**: Refer to troubleshooting section or contact system administrator
