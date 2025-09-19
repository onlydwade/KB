# Azure Provider Configuration
terraform {
  required_version = ">= 1.0"
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.41"  # Updated to latest version
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.1"
    }
  }
  
  # Backend configuration for remote state (will be configured after initial deployment)
  # backend "azurerm" {
  #   resource_group_name  = "rg-eas-dev-audit-sys-001"
  #   storage_account_name = "tfstatedevbkauditsys001"
  #   container_name       = "dev-environment"
  #   key                  = "terraform.tfstate"
  # }
}

# Configure the Microsoft Azure Provider
provider "azurerm" {
  features {
    resource_group {
      prevent_deletion_if_contains_resources = false
    }
    virtual_machine {
      delete_os_disk_on_deletion     = true
      skip_shutdown_and_force_delete = false
    }
  }

  subscription_id = var.subscription_id
}

# Fixed password variable for VM
variable "vm_admin_password" {
  description = "Fixed VM admin password"
  type        = string
  default     = "1U}ba%z$[j+t%M10"
  sensitive   = true
}

# Generate random password for MySQL
resource "random_password" "mysql_admin_password" {
  length  = 16
  special = true
  upper   = true
  lower   = true
  numeric = true
}
