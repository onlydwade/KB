# Storage Account
resource "azurerm_storage_account" "main" {
  name                     = "sa${var.environment}${var.company_short}auditsys001"
  resource_group_name      = azurerm_resource_group.main.name
  location                 = azurerm_resource_group.main.location
  account_tier             = var.storage_account_tier
  account_replication_type = var.storage_account_replication_type
  account_kind             = "StorageV2"
  access_tier              = "Hot"

  # Enable public access temporarily for container creation, will be disabled after private endpoint setup
  public_network_access_enabled = true
  
  # Enable blob properties
  blob_properties {
    delete_retention_policy {
      days = 7
    }
    container_delete_retention_policy {
      days = 7
    }
  }

  tags = var.tags
}

# Private DNS Zone for Storage Account
resource "azurerm_private_dns_zone" "storage" {
  name                = "privatelink.blob.core.windows.net"
  resource_group_name = azurerm_resource_group.main.name
  tags                = var.tags
}

# Link Private DNS Zone to VNet
resource "azurerm_private_dns_zone_virtual_network_link" "storage" {
  name                  = "vnet-link-storage"
  resource_group_name   = azurerm_resource_group.main.name
  private_dns_zone_name = azurerm_private_dns_zone.storage.name
  virtual_network_id    = azurerm_virtual_network.main.id
  registration_enabled  = false
  tags                  = var.tags
}

# Private Endpoint for Storage Account (in App Subnet)
resource "azurerm_private_endpoint" "storage" {
  name                = "pe-storage-eas-${var.environment}-${var.project_name}-001"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  subnet_id           = azurerm_subnet.app.id
  tags                = var.tags

  private_service_connection {
    name                           = "psc-storage"
    private_connection_resource_id = azurerm_storage_account.main.id
    subresource_names              = ["blob"]
    is_manual_connection           = false
  }

  private_dns_zone_group {
    name                 = "pdz-group-storage"
    private_dns_zone_ids = [azurerm_private_dns_zone.storage.id]
  }
}

# Storage Container for application data
resource "azurerm_storage_container" "app_data" {
  name                  = "app-data"
  storage_account_id    = azurerm_storage_account.main.id
  container_access_type = "private"
}

# Storage Container for backups
resource "azurerm_storage_container" "backups" {
  name                  = "backups"
  storage_account_id    = azurerm_storage_account.main.id
  container_access_type = "private"
}

# Storage Container for logs
resource "azurerm_storage_container" "logs" {
  name                  = "logs"
  storage_account_id    = azurerm_storage_account.main.id
  container_access_type = "private"
}
