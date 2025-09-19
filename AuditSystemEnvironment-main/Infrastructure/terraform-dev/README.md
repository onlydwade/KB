# AuditSystem DEV Environment - Terraform Infrastructure

This Terraform configuration creates a complete Azure infrastructure for the AuditSystem DEV environment, following the same architecture pattern as UAT and Production but optimized for development use.

## 🏗️ Architecture Overview

```
Azure Subscription: Microsoft Azure Sponsorship (12) - 2026
├── Resource Group: rg-eas-dev-audit-sys-001
├── Virtual Network: vnet-eas-dev-001 (10.2.0.0/16)
│   ├── App Subnet: snet-audit-sys-eas-dev-001 (10.2.1.0/24)
│   │   ├── VM: vmdevbkauditsys001 (B4as v2 - 4 vCPU, 16GB)
│   │   ├── Additional Disk: 30GB Premium SSD
│   │   ├── Public IP: For SSH access
│   │   └── Private Endpoint → Storage Account
│   │
│   └── DB Subnet: snet-audit-sys-db-eas-dev-001 (10.2.2.0/24)
│       └── MySQL Flexible Server: mysql-eas-dev-audit-sys-001 (B1ms)
│
├── Storage Account: sadevbkauditsys001 (LRS)
└── Private DNS Zones: For internal name resolution
```

## 📋 Resources Created

### **Compute Resources**
- **Virtual Machine**: `vmdevbkauditsys001`
  - Size: Standard_B4as_v2 (4 vCPU, 16GB RAM)
  - OS: Ubuntu 22.04 LTS
  - Public IP: Yes (for SSH access)
  - Additional Disk: 30GB Premium SSD mounted at `/data`

### **Network Resources**
- **Virtual Network**: `vnet-eas-dev-001`
- **Application Subnet**: `snet-audit-sys-eas-dev-001`
- **Database Subnet**: `snet-audit-sys-db-eas-dev-001`
- **Network Security Groups**: With rules for SSH, HTTP, HTTPS, Nacos, API Gateway
- **Public IP**: For VM external access

### **Database Resources**
- **MySQL Flexible Server**: `mysql-eas-dev-audit-sys-001`
  - SKU: B_Standard_B1ms (1 vCore, 2GB RAM)
  - Version: MySQL 8.0.21
  - Storage: 20GB
  - Databases: `auditsystem`, `nacos`
  - Private connectivity via delegated subnet

### **Storage Resources**
- **Storage Account**: `sadevbkauditsys001`
  - Type: General Purpose v2, Standard LRS
  - Access Tier: Hot
  - Private endpoint in app subnet
  - Containers: `app-data`, `backups`, `logs`

### **Security Resources**
- **Private DNS Zones**: For MySQL and Storage Account
- **Private Endpoints**: Secure connectivity to PaaS services
- **Network Security Groups**: Traffic filtering
- **Generated Passwords**: Secure random passwords for VM and MySQL

## 🚀 Deployment Instructions

### Prerequisites
1. **Azure CLI**: Install and login to your Azure account
2. **Terraform**: Install Terraform >= 1.0
3. **Subscription Access**: Ensure you have Owner role on the subscription

### Step 1: Setup Azure CLI
```bash
# Login to Azure
az login

# Set the subscription
az account set --subscription "d4915ef0-d9e7-41cc-80ad-1a6a5ef0aa9d"

# Verify subscription
az account show
```

### Step 2: Configure Terraform
```bash
# Navigate to terraform directory
cd Infrastructure/terraform-dev

# Copy example variables file
cp terraform.tfvars.example terraform.tfvars

# Edit variables if needed (optional)
nano terraform.tfvars
```

### Step 3: Deploy Infrastructure
```bash
# Initialize Terraform
terraform init

# Review the deployment plan
terraform plan

# Deploy the infrastructure
terraform apply
```

### Step 4: Save Outputs
```bash
# Get deployment outputs
terraform output

# Save sensitive outputs to file
terraform output -json > deployment-outputs.json
```

## 🔑 Access Information

After deployment, Terraform will output:

### **VM Access**
```bash
# SSH Connection
ssh azureuser@<public-ip>
# Password will be in terraform output
```

### **MySQL Access** (from VM)
```bash
# Connect to MySQL
mysql -h mysql-eas-dev-audit-sys-001.mysql.database.azure.com -u mysqladmin -p
# Password will be in terraform output
```

### **Application URLs**
- **Frontend**: http://\<public-ip\>
- **Nacos Console**: http://\<public-ip\>:8848/nacos
- **API Gateway**: http://\<public-ip\>:8200

## 💾 Post-Deployment Setup

### 1. Deploy AuditSystem Application
```bash
# SSH to VM
ssh azureuser@<public-ip>

# Copy AuditSystemVM deployment package to VM
scp -r ../AuditSystemVM azureuser@<public-ip>:/home/azureuser/

# SSH to VM and deploy
ssh azureuser@<public-ip>
cd AuditSystemVM

# Configure for Azure MySQL
# Edit deployment scripts to use Azure MySQL instead of local MySQL
# Update connection strings to point to mysql-eas-dev-audit-sys-001.mysql.database.azure.com

# Run deployment
./install-ubuntu-setup.sh && ./deploy-backend.sh && ./deploy-frontend.sh && ./configure-nginx.sh
```

### 2. Configure MySQL Connection
```bash
# Update application.yml files to use Azure MySQL
# Connection string: jdbc:mysql://mysql-eas-dev-audit-sys-001.mysql.database.azure.com:3306/auditsystem
# Username: mysqladmin
# Password: <from terraform output>
```

## 💰 Cost Estimation

| Resource | SKU | Monthly Cost (USD) |
|----------|-----|-------------------|
| VM B4as v2 | 4 vCPU, 16GB | ~$120 |
| MySQL B1ms | 1 vCore, 2GB | ~$25 |
| Storage 20GB | Premium SSD | ~$3 |
| Storage Account | Standard LRS | ~$5 |
| Network | Public IP, egress | ~$5 |
| **Total** | | **~$158/month** |

## 🛠️ Management Commands

### View Resources
```bash
# List all resources
terraform state list

# Show specific resource
terraform show azurerm_linux_virtual_machine.main
```

### Update Infrastructure
```bash
# After modifying .tf files
terraform plan
terraform apply
```

### Destroy Infrastructure
```bash
# WARNING: This will delete all resources
terraform destroy
```

## 🔧 Customization

### Change VM Size
```bash
# Edit terraform.tfvars
vm_size = "Standard_B2as_v2"  # Smaller: 2 vCPU, 8GB

# Apply changes
terraform apply
```

### Add More Storage
```bash
# Edit terraform.tfvars
additional_disk_size_gb = 50  # Increase to 50GB

# Apply changes
terraform apply
```

### Restrict SSH Access
```bash
# Edit terraform.tfvars - Replace with your IP
allowed_ssh_ip_ranges = ["203.198.x.x/32"]

# Apply changes
terraform apply
```

## 🚨 Security Considerations

### ⚠️ Development Environment Warnings
1. **Public SSH Access**: Enabled for development convenience
2. **Password Authentication**: Used instead of SSH keys
3. **Open Firewall Rules**: Allow access from 0.0.0.0/0

### 🔒 Security Best Practices
1. **Restrict SSH Access**: Update `allowed_ssh_ip_ranges` with your IP
2. **Use SSH Keys**: Consider switching to SSH key authentication
3. **Enable Monitoring**: Set up Azure Monitor for security alerts
4. **Regular Updates**: Keep VM and applications updated

## 📞 Troubleshooting

### Common Issues

#### 1. Terraform Init Fails
```bash
# Clear cache and retry
rm -rf .terraform
terraform init
```

#### 2. MySQL Connection Issues
```bash
# Check if VM can reach MySQL
nslookup mysql-eas-dev-audit-sys-001.mysql.database.azure.com
telnet mysql-eas-dev-audit-sys-001.mysql.database.azure.com 3306
```

#### 3. Storage Access Issues
```bash
# Check private endpoint resolution
nslookup sadevbkauditsys001.blob.core.windows.net
```

### Support
- **Azure Issues**: Check Azure Portal → Resource Health
- **Terraform Issues**: Review terraform.log
- **Application Issues**: Check VM logs in `/var/log/`

---

**📝 Note**: This is a development environment. For production deployment, implement additional security measures, monitoring, and backup strategies.
