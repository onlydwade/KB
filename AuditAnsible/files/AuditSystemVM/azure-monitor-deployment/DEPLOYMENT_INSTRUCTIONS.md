# Azure Monitor Deployment Instructions

## ⚠️ Important: Modern Azure Monitor Agent (AMA)

**This setup now uses the new Azure Monitor Agent (AMA)** instead of the deprecated OMS agent. The OMS agent was deprecated on August 31, 2024, and Microsoft recommends migrating to AMA.

### Migration Strategy:
- 🆕 **Primary**: Azure Monitor Agent (AMA) - Microsoft's modern monitoring solution
- 🔄 **Compatibility**: Log Analytics agent - Maintains custom log functionality during transition
- 📊 **Seamless**: No data loss during migration


### Benefits of New Setup:
- ✅ **Better performance** - More efficient resource usage  
- ✅ **Enhanced security** - Modern authentication methods
- ✅ **Simplified management** - Centralized agent configuration

## 📁 Files Overview

This folder contains all scripts needed for Azure Monitor integration with your Audit System VM:

- **`mac-azure-monitor-setup.sh`** - Main setup script (run on Mac)
- **`vm-health-monitor.sh`** - Health monitoring daemon (deployed to VM)  
- **`configure-azure-monitor-agent.sh`** - Modern Azure Monitor Agent installer (AMA + compatibility layer)
- **`check-azure-monitor.sh`** - Verification script (run on VM after setup)
- **`test-azure-upload.sh`** - Test data upload script (run on VM)

## 🚀 Quick Start

- VM credentials (IP, username, password)

### Option A: Interactive Setup (First Time Users)
```bash
cd azure-monitor-deployment
./mac-azure-monitor-setup.sh interactive-setup
```
This will:
1. 🔍 Detect existing Azure Log Analytics workspaces
2. 🎯 Guide you through configuration options
3. 🏗️ Create Azure resources if needed
```bash
cd azure-monitor-deployment
./mac-azure-monitor-setup.sh quick-deploy
```

Use this when you already have a workspace and want to deploy quickly.

# 1. Create Azure resources (if needed)
# 3. Deploy to VM
./mac-azure-monitor-setup.sh deploy-to-vm
```

## 🔧 Manual Deployment (Advanced Users)

If you prefer manual control over the deployment process:

### 1. Copy Files to VM
```bash
# From the azure-monitor-deployment folder:
# Install sshpass if needed: brew install hudochenkov/sshpass/sshpass

sshpass -p 'YOUR_VM_PASSWORD' scp vm-health-monitor.sh azureuser@YOUR_VM_IP:/home/azureuser/
sshpass -p 'YOUR_VM_PASSWORD' scp configure-azure-monitor-agent.sh azureuser@YOUR_VM_IP:/home/azureuser/
sshpass -p 'YOUR_VM_PASSWORD' scp check-azure-monitor.sh azureuser@YOUR_VM_IP:/home/azureuser/
sshpass -p 'YOUR_VM_PASSWORD' scp test-azure-upload.sh azureuser@YOUR_VM_IP:/home/azureuser/
```

### 2. Run Configuration on VM
```bash
sshpass -p 'YOUR_VM_PASSWORD' ssh azureuser@YOUR_VM_IP
chmod +x *.sh
sudo ./configure-azure-monitor-agent.sh
```

## ✅ Verification & Testing

After deployment, verify the installation on your VM:

### 1. Check Service Status
```bash
# Check Azure health monitoring service
sudo systemctl status azure-health-monitor

# Check new Azure Monitor Agent (AMA)
sudo systemctl status azuremonitoragent
# Quick status check
./vm-health-monitor.sh --check
```bash
# Run the verification script
./check-azure-monitor.sh
```

This script will check:
- 📊 Local health data generation
- 🆕 **Azure Monitor Agent (AMA)** status and functionality
- 💓 **Log Analytics Agent** status and heartbeat (compatibility layer)
- ⚙️ Configuration files for both agents
- 📝 Log file permissions and content
- 🌐 Azure connectivity

### 3. Test Data Upload
```bash
# Send a test entry to Azure Monitor
./test-azure-upload.sh
```

Wait 5-10 minutes, then check Azure Portal for the test data.

### 4. Monitor Live Logs
```bash
# View real-time health logs (JSON format)
tail -f /var/log/azure-monitor-health.json

# View service logs  

# View Log Analytics Agent logs (compatibility)
## 🏥 Health Monitoring Features

### Enhanced Health Checks
- **🔒 Non-privileged**: Uses `systemctl` and port connectivity (no database passwords needed)
- ⏱️ **Timeout protection**: 5-second timeouts prevent hanging
- 🎯 **Reliable detection**: `systemctl is-active` for service status
- 🔌 **Port connectivity**: `netcat` verifies services accept connections  
- 📋 **Structured logging**: JSON format for better parsing

### Monitored Services
- **Database Services**: MySQL, Redis
- **Message Queues**: RabbitMQ
- **Microservices**: Gateway (8200), Auth (8300), System (8400), Business (8500), Job (8600), Report (8700)
| Service | Status Check | Port Check |
|---------|-------------|------------|

## 📊 Azure Portal Access

### 1. Log Analytics Workspace
After successful deployment, access your monitoring data:
- **Logs Query Interface**: Workspace → Logs

### 2. Sample KQL Queries

#### Recent Health Status
```kql
```

| extend Service = tostring(split(RawData, '"service":"')[1])
| extend Status = tostring(split(RawData, '"status":"')[1])
| extend Status = tostring(split(Status, '"')[0])
| where isnotempty(Service) and isnotempty(Status)
| summarize 
    HealthyCount = countif(Status == "UP"),
    FailureCount = countif(Status == "DOWN"),
    TotalChecks = count()
    by Service
#### System Performance Trends
```kql
AuditSystemHealth_CL
| where TimeGenerated > ago(6h)
| extend CPU = todouble(CPUData), Memory = todouble(MemData), Disk = todouble(DiskData)
| summarize 
| order by TimeGenerated desc
| render timechart
```

#### Find Test Data
```kql
AuditSystemHealth_CL
| where RawData contains "test"
| project TimeGenerated, Computer, RawData
| order by TimeGenerated desc
```

## 🛠️ Configuration Management

### Environment Variables
You can customize the setup using environment variables:

```bash
# VM Configuration
export VM_IP="YOUR_VM_IP"
export VM_USER="azureuser"
export VM_PASSWORD="your_password"

# Azure Configuration  
export AZURE_RESOURCE_GROUP="your-rg-name"
export AZURE_LOCATION="eastus"
export LOG_ANALYTICS_WORKSPACE="your-workspace-name"

# Run setup
./mac-azure-monitor-setup.sh quick-deploy
```

### Available Commands
```bash
# Setup commands
./mac-azure-monitor-setup.sh interactive-setup    # First-time interactive setup
./mac-azure-monitor-setup.sh quick-deploy        # Quick deployment with existing workspace
./mac-azure-monitor-setup.sh full-setup          # Automated setup with defaults
./mac-azure-monitor-setup.sh create-workspace    # Create workspace only
./mac-azure-monitor-setup.sh generate-deployment # Generate files only  
./mac-azure-monitor-setup.sh deploy-to-vm        # Deploy to VM only
```

## 🔧 Troubleshooting

### Common Issues

#### 1. No Data in Azure Portal
```bash
# On VM - Check services
sudo systemctl status azure-health-monitor
sudo systemctl status omsagent-e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8

# Check logs
./check-azure-monitor.sh
tail -f /var/log/azure-monitor-health.json
```

#### 2. Agent Not Connecting
```bash
# Check Azure Monitor Agent (AMA) logs
sudo journalctl -u azuremonitoragent -f

# Check Log Analytics agent logs  
sudo tail -f /var/opt/microsoft/omsagent/e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8/log/omsagent.log

# Check all Azure monitoring services
sudo systemctl status azuremonitoragent
sudo systemctl status omsagent-e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8
```

#### 3. Health Monitor Issues
```bash
# Restart health monitor
sudo systemctl restart azure-health-monitor

# Check health monitor logs
sudo journalctl -u azure-health-monitor -f
```

#### 4. Deployment Issues
```bash
# On Mac - Check Azure CLI authentication
az account show

# Test VM connectivity
ssh azureuser@YOUR_VM_IP

# Reinstall sshpass if needed
brew uninstall hudochenkov/sshpass/sshpass
brew install hudochenkov/sshpass/sshpass
```

### Log Locations
- **Health monitoring**: `/var/log/azure-monitor-health.json`
- **Health service logs**: `journalctl -u azure-health-monitor`
- **Azure Monitor Agent (AMA)**: `journalctl -u azuremonitoragent`
- **Log Analytics Agent**: `/var/opt/microsoft/omsagent/e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8/log/`
- **Configuration**: `/etc/opt/microsoft/omsagent/e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8/conf/omsagent.d/`

## ⚠️ Expected Installation Messages

During installation, you may see this deprecation warning:
```
The Log Analytics agent is on a deprecation path and won't be supported after August 31, 2024.
```

**This is expected and handled by our setup:**
- ✅ **Primary monitoring**: Uses new Azure Monitor Agent (AMA)
- ✅ **Custom logs**: Temporarily uses Log Analytics agent for compatibility
- ✅ **Future-proof**: Ready for full AMA migration when Microsoft completes custom log support
- ✅ **No action needed**: The setup handles both agents automatically

## 💰 Cost Information

- **Log Analytics (first 5GB/month)**: **FREE**
- **Estimated data ingestion**: ~10-20MB/month for this setup  
- **Data retention**: 30 days (FREE tier)
- **Azure Monitor Agent**: No additional charges
- **Total estimated monthly cost**: **$0.00** (within free limits)

## 📈 Next Steps

1. **📱 Set up Alerts**: Create Azure Monitor alerts for critical service failures
2. **📊 Build Dashboards**: Create custom Azure dashboards for visualization
3. **📧 Configure Notifications**: Set up email/SMS alerts for incidents  
4. **🔍 Add Application Insights**: Detailed application performance monitoring
5. **📐 Scale Monitoring**: Extend to additional VMs or services

---

**🎯 Success Criteria**: Data should appear in Azure Portal within 5-10 minutes of deployment
**📞 Support**: Run `./check-azure-monitor.sh` for comprehensive diagnostic information
