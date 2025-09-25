# Quick Start Guide - AuditSystem VM Deployment

## 🚀 Ultra-Quick Deployment (One Command)

```bash
cd AuditSystemVM
chmod +x *.sh && ./install-ubuntu-setup.sh && ./setup-nacos.sh && ./deploy-backend.sh && ./deploy-frontend.sh && ./configure-nginx.sh && ./manage-services.sh start
```

**Total Time**: ~25-40 minutes

## ⚡ Step-by-Step (Recommended)

### 1. Prepare Scripts
```bash
cd AuditSystemVM
chmod +x *.sh
```

### 2. Install System Dependencies
```bash
./install-ubuntu-setup.sh
```
⏱️ **Time**: 5-10 minutes | **What**: Java, Maven, Node.js, MySQL, Redis, RabbitMQ, Nginx

### 3. Setup Service Discovery
```bash
./setup-nacos.sh
```
⏱️ **Time**: 2-3 minutes | **What**: Nacos service registry

### 4. Deploy Backend Services
```bash
./deploy-backend.sh
```
⏱️ **Time**: 10-15 minutes | **What**: 6 microservices (Auth, System, Business, Job, Report, Gateway)

### 5. Deploy Frontend Application
```bash
./deploy-frontend.sh
```
⏱️ **Time**: 5-8 minutes | **What**: Vue.js application build and deployment

### 6. Configure Web Server
```bash
./configure-nginx.sh
```
⏱️ **Time**: 1-2 minutes | **What**: Nginx configuration for SPA routing

### 7. Start All Services
```bash
./manage-services.sh start
```
⏱️ **Time**: 1-2 minutes | **What**: Start all services in correct order

## ✅ Verification

### Check Service Status
```bash
./manage-services.sh status
```

Expected output:
```
✅ mysql: RUNNING
✅ redis-server: RUNNING  
✅ rabbitmq-server: RUNNING
✅ nacos: RUNNING
✅ auditsystem-auth: RUNNING
✅ auditsystem-system: RUNNING
✅ auditsystem-biz: RUNNING
✅ auditsystem-job: RUNNING
✅ auditsystem-report: RUNNING
✅ auditsystem-gateway: RUNNING
✅ nginx: RUNNING
```

### Access Application
- **Frontend**: http://your-vm-ip
- **Nacos**: http://your-vm-ip:8848/nacos (nacos/nacos)

## 🛠️ Quick Commands

| Action | Command |
|--------|---------|
| **Start all** | `./manage-services.sh start` |
| **Stop all** | `./manage-services.sh stop` |
| **Restart all** | `./manage-services.sh restart` |
| **Check status** | `./manage-services.sh status` |
| **View logs** | `./manage-services.sh logs [service]` |

## 🚨 If Something Goes Wrong

1. **Check logs**: `./manage-services.sh logs`
2. **Restart services**: `./manage-services.sh restart`
3. **Check port conflicts**: `sudo netstat -tulpn | grep :8200`
4. **See detailed README.md** for troubleshooting

## 📋 Prerequisites Checklist

- [ ] Ubuntu 24.04 LTS
- [ ] 8GB+ RAM
- [ ] 20GB+ free disk space
- [ ] Internet access
- [ ] Non-root user with sudo
- [ ] Access to GitHub repositories

---

**Need help?** Check the full `README.md` for detailed troubleshooting and configuration options.
