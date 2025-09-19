# Resource Group Information
output "resource_group_name" {
  description = "Name of the created resource group"
  value       = azurerm_resource_group.main.name
}

output "resource_group_location" {
  description = "Location of the resource group"
  value       = azurerm_resource_group.main.location
}

# Network Information
output "virtual_network_name" {
  description = "Name of the virtual network"
  value       = azurerm_virtual_network.main.name
}

output "app_subnet_id" {
  description = "ID of the application subnet"
  value       = azurerm_subnet.app.id
}

output "db_subnet_id" {
  description = "ID of the database subnet"
  value       = azurerm_subnet.db.id
}

# VM Information
output "vm_name" {
  description = "Name of the virtual machine"
  value       = azurerm_linux_virtual_machine.main.name
}

output "vm_public_ip" {
  description = "Public IP address of the VM"
  value       = azurerm_public_ip.vm.ip_address
}

output "vm_private_ip" {
  description = "Private IP address of the VM"
  value       = azurerm_network_interface.vm.private_ip_address
}

output "vm_admin_username" {
  description = "VM administrator username"
  value       = var.vm_admin_username
}

output "vm_admin_password" {
  description = "VM administrator password"
  value       = var.vm_admin_password
  sensitive   = true
}

# Storage Account Information
output "storage_account_name" {
  description = "Name of the storage account"
  value       = azurerm_storage_account.main.name
}

output "storage_account_primary_endpoint" {
  description = "Primary blob endpoint of the storage account"
  value       = azurerm_storage_account.main.primary_blob_endpoint
}

output "storage_private_endpoint_ip" {
  description = "Private IP address of storage private endpoint"
  value       = azurerm_private_endpoint.storage.private_service_connection[0].private_ip_address
}

# MySQL Information
output "mysql_server_name" {
  description = "Name of the MySQL flexible server"
  value       = azurerm_mysql_flexible_server.main.name
}

output "mysql_server_fqdn" {
  description = "FQDN of the MySQL flexible server"
  value       = azurerm_mysql_flexible_server.main.fqdn
}

output "mysql_admin_username" {
  description = "MySQL administrator username"
  value       = var.mysql_admin_username
}

output "mysql_admin_password" {
  description = "MySQL administrator password"
  value       = random_password.mysql_admin_password.result
  sensitive   = true
}

output "mysql_database_names" {
  description = "Names of created MySQL databases"
  value = [
    azurerm_mysql_flexible_database.audit_system.name,
    azurerm_mysql_flexible_database.nacos.name
  ]
}

# SSH Connection Information
output "ssh_connection_command" {
  description = "SSH command to connect to the VM"
  value       = "ssh ${var.vm_admin_username}@${azurerm_public_ip.vm.ip_address}"
}

# Application Access URLs
output "application_urls" {
  description = "URLs to access the application services"
  value = {
    frontend      = "http://${azurerm_public_ip.vm.ip_address}"
    nacos_console = "http://${azurerm_public_ip.vm.ip_address}:8848/nacos"
    api_gateway   = "http://${azurerm_public_ip.vm.ip_address}:8200"
  }
}

# Deployment Summary
output "deployment_summary" {
  description = "Summary of deployed resources"
  value = {
    environment      = var.environment
    vm_size         = var.vm_size
    mysql_sku       = var.mysql_sku_name
    additional_disk = "${var.additional_disk_size_gb}GB"
    location        = var.location
    subscription_id = var.subscription_id
    storage_type    = "Standard_LRS"  # Updated for cost savings
  }
}

# Remote State Information
output "terraform_state_storage" {
  description = "Terraform remote state storage information"
  value = {
    storage_account_name = azurerm_storage_account.terraform_state.name
    container_name       = azurerm_storage_container.terraform_state_dev.name
    resource_group_name  = azurerm_resource_group.main.name
  }
}

# Automation Information
output "automation_schedule" {
  description = "VM and MySQL auto start/stop schedule information"
  value = {
    automation_account = azurerm_automation_account.main.name
    start_time_hk     = "09:00 HK Time (Daily)"
    stop_time_hk      = "23:59 HK Time (Daily)"
    timezone          = "Asia/Hong_Kong"
  }
}

# Cost Optimization Features
output "cost_optimization" {
  description = "Cost optimization features enabled"
  value = {
    auto_start_stop     = "Enabled (9:00-23:59 HK Time)"
    storage_type        = "Standard LRS (Cost Optimized)"
    vm_schedule         = "Auto shutdown to save costs"
    mysql_schedule      = "Auto stop/start with VM"
  }
}
