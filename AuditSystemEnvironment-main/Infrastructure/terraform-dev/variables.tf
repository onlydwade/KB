# Azure Subscription ID
variable "subscription_id" {
  description = "Azure Subscription ID"
  type        = string
  default     = "d4915ef0-d9e7-41cc-80ad-1a6a5ef0aa9d"
}

# Environment Configuration
variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev"
}

variable "project_name" {
  description = "Project name"
  type        = string
  default     = "audit-sys"
}

variable "company_short" {
  description = "Company short name"
  type        = string
  default     = "bk"
}

variable "location" {
  description = "Azure region"
  type        = string
  default     = "East Asia"
}

# Network Configuration
variable "vnet_address_space" {
  description = "Virtual network address space"
  type        = list(string)
  default     = ["10.2.0.0/16"]
}

variable "app_subnet_address_prefix" {
  description = "Application subnet address prefix"
  type        = string
  default     = "10.2.1.0/24"
}

variable "db_subnet_address_prefix" {
  description = "Database subnet address prefix"
  type        = string
  default     = "10.2.2.0/24"
}

# VM Configuration
variable "vm_size" {
  description = "Virtual machine size"
  type        = string
  default     = "Standard_B4as_v2" # 4 vCPU, 16GB RAM
}

variable "vm_admin_username" {
  description = "VM administrator username"
  type        = string
  default     = "azureuser"
}

variable "additional_disk_size_gb" {
  description = "Additional disk size in GB"
  type        = number
  default     = 30
}

# MySQL Configuration
variable "mysql_sku_name" {
  description = "MySQL SKU name"
  type        = string
  default     = "B_Standard_B1ms" # Burstable B1ms
}

variable "mysql_admin_username" {
  description = "MySQL administrator username"
  type        = string
  default     = "mysqladmin"
}

variable "mysql_version" {
  description = "MySQL version"
  type        = string
  default     = "8.0.21"
}

variable "mysql_storage_mb" {
  description = "MySQL storage in MB"
  type        = number
  default     = 20480 # 20GB
}

# Storage Configuration
variable "storage_account_tier" {
  description = "Storage account tier"
  type        = string
  default     = "Standard"
}

variable "storage_account_replication_type" {
  description = "Storage account replication type"
  type        = string
  default     = "LRS"
}

# Allow SSH from specific IP ranges (update as needed)
variable "allowed_ssh_ip_ranges" {
  description = "List of IP ranges allowed for SSH access"
  type        = list(string)
  default     = ["0.0.0.0/0"] # WARNING: This allows access from anywhere. Restrict in production!
}

# Tags
variable "tags" {
  description = "Resource tags"
  type        = map(string)
  default = {
    Environment = "dev"
    Project     = "BuildKing Internal Audit System"
    Owner       = "GTI"
    CostCenter  = "Development"
  }
}
