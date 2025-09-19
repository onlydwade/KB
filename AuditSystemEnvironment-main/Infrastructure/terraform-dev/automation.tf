# Automation Account for VM and MySQL auto start/stop
resource "azurerm_automation_account" "main" {
  name                = "aa-${var.environment}-${var.project_name}-001"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  sku_name           = "Basic"
  
  tags = var.tags
}

# Managed Identity for Automation Account
resource "azurerm_user_assigned_identity" "automation" {
  name                = "id-automation-${var.environment}-${var.project_name}"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  tags                = var.tags
}

# Assign permissions to automation identity
resource "azurerm_role_assignment" "automation_contributor" {
  scope                = azurerm_resource_group.main.id
  role_definition_name = "Contributor"
  principal_id         = azurerm_user_assigned_identity.automation.principal_id
}

# PowerShell Runbook for VM Start
resource "azurerm_automation_runbook" "vm_start" {
  name                    = "Start-DevVM"
  location                = azurerm_resource_group.main.location
  resource_group_name     = azurerm_resource_group.main.name
  automation_account_name = azurerm_automation_account.main.name
  log_verbose             = true
  log_progress            = true
  runbook_type           = "PowerShell"

  content = <<CONTENT
param(
    [string]$ResourceGroupName = "${azurerm_resource_group.main.name}",
    [string]$VMName = "${azurerm_linux_virtual_machine.main.name}",
    [string]$MySQLServerName = "${azurerm_mysql_flexible_server.main.name}"
)

# Connect using managed identity
Connect-AzAccount -Identity

Write-Output "Starting VM: $VMName"
Start-AzVM -ResourceGroupName $ResourceGroupName -Name $VMName
Write-Output "VM $VMName started successfully"

Write-Output "Starting MySQL Server: $MySQLServerName"
Start-AzMySqlFlexibleServer -ResourceGroupName $ResourceGroupName -Name $MySQLServerName
Write-Output "MySQL Server $MySQLServerName started successfully"
CONTENT

  tags = var.tags
}

# PowerShell Runbook for VM Stop
resource "azurerm_automation_runbook" "vm_stop" {
  name                    = "Stop-DevVM"
  location                = azurerm_resource_group.main.location
  resource_group_name     = azurerm_resource_group.main.name
  automation_account_name = azurerm_automation_account.main.name
  log_verbose             = true
  log_progress            = true
  runbook_type           = "PowerShell"

  content = <<CONTENT
param(
    [string]$ResourceGroupName = "${azurerm_resource_group.main.name}",
    [string]$VMName = "${azurerm_linux_virtual_machine.main.name}",
    [string]$MySQLServerName = "${azurerm_mysql_flexible_server.main.name}"
)

# Connect using managed identity
Connect-AzAccount -Identity

Write-Output "Stopping VM: $VMName"
Stop-AzVM -ResourceGroupName $ResourceGroupName -Name $VMName -Force
Write-Output "VM $VMName stopped successfully"

Write-Output "Stopping MySQL Server: $MySQLServerName"
Stop-AzMySqlFlexibleServer -ResourceGroupName $ResourceGroupName -Name $MySQLServerName
Write-Output "MySQL Server $MySQLServerName stopped successfully"
CONTENT

  tags = var.tags
}

# Schedule for VM Start (9:00 AM HK Time = 1:00 AM UTC)
resource "azurerm_automation_schedule" "vm_start" {
  name                    = "Start-DevVM-Schedule"
  resource_group_name     = azurerm_resource_group.main.name
  automation_account_name = azurerm_automation_account.main.name
  frequency               = "Day"
  interval                = 1
  timezone                = "Asia/Hong_Kong"
  start_time              = formatdate("YYYY-MM-DD'T'09:00:00+08:00", timeadd(timestamp(), "24h"))
  description             = "Daily start schedule for DEV environment at 9:00 AM HK time"
}

# Schedule for VM Stop (11:59 PM HK Time = 3:59 PM UTC)
resource "azurerm_automation_schedule" "vm_stop" {
  name                    = "Stop-DevVM-Schedule"
  resource_group_name     = azurerm_resource_group.main.name
  automation_account_name = azurerm_automation_account.main.name
  frequency               = "Day"
  interval                = 1
  timezone                = "Asia/Hong_Kong"
  start_time              = formatdate("YYYY-MM-DD'T'23:59:00+08:00", timeadd(timestamp(), "24h"))
  description             = "Daily stop schedule for DEV environment at 11:59 PM HK time"
}

# Link schedule to start runbook
resource "azurerm_automation_job_schedule" "vm_start" {
  resource_group_name     = azurerm_resource_group.main.name
  automation_account_name = azurerm_automation_account.main.name
  schedule_name           = azurerm_automation_schedule.vm_start.name
  runbook_name            = azurerm_automation_runbook.vm_start.name
}

# Link schedule to stop runbook
resource "azurerm_automation_job_schedule" "vm_stop" {
  resource_group_name     = azurerm_resource_group.main.name
  automation_account_name = azurerm_automation_account.main.name
  schedule_name           = azurerm_automation_schedule.vm_stop.name
  runbook_name            = azurerm_automation_runbook.vm_stop.name
}
