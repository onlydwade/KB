# Remote State Storage Account
resource "azurerm_storage_account" "terraform_state" {
  name                     = "tfstate${var.environment}${var.company_short}auditsys001"
  resource_group_name      = azurerm_resource_group.main.name
  location                 = azurerm_resource_group.main.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  account_kind             = "StorageV2"
  access_tier              = "Hot"

  # Enable versioning for state file protection
  blob_properties {
    versioning_enabled = true
    delete_retention_policy {
      days = 30
    }
  }

  tags = merge(var.tags, {
    Purpose = "Terraform Remote State"
  })
}

# Container for Terraform state files
resource "azurerm_storage_container" "terraform_state" {
  name                  = "terraform-state"
  storage_account_id    = azurerm_storage_account.terraform_state.id
  container_access_type = "private"
}

# Container for environment-specific state
resource "azurerm_storage_container" "terraform_state_dev" {
  name                  = "dev-environment"
  storage_account_id    = azurerm_storage_account.terraform_state.id
  container_access_type = "private"
}
