# Example terraform.tfvars file
# Copy this file to terraform.tfvars and modify as needed

# Azure Configuration
subscription_id = "d4915ef0-d9e7-41cc-80ad-1a6a5ef0aa9d"

# Environment Configuration
environment   = "dev"
project_name  = "audit-sys"
company_short = "bk"
location      = "East Asia"

# Network Configuration
vnet_address_space         = ["10.2.0.0/16"]
app_subnet_address_prefix  = "10.2.1.0/24"
db_subnet_address_prefix   = "10.2.2.0/24"

# VM Configuration
vm_size                = "Standard_B4as_v2"  # 4 vCPU, 16GB RAM
vm_admin_username      = "azureuser"
additional_disk_size_gb = 30

# MySQL Configuration
mysql_sku_name        = "B_Standard_B1ms"  # Burstable B1ms
mysql_admin_username  = "mysqladmin"
mysql_version         = "8.0.21"
mysql_storage_mb      = 20480  # 20GB

# Storage Configuration
storage_account_tier             = "Standard"
storage_account_replication_type = "LRS"

# Security Configuration
# WARNING: 0.0.0.0/0 allows SSH from anywhere. Restrict this in production!
# Replace with your specific IP ranges for better security
allowed_ssh_ip_ranges = [
  "0.0.0.0/0"  # Allow from anywhere (DEV only)
  # "203.198.x.x/32",  # Example: Your office IP
  # "192.168.1.0/24",  # Example: Your home network
]

# Resource Tags
tags = {
  Environment = "dev"
  Project     = "BuildKing Internal Audit System"
  Owner       = "GTI"
  CostCenter  = "Development"
  CreatedBy   = "Terraform"
  CreatedDate = "2025-08-28"
}
