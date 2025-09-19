# Backend Configuration for Remote State
# This file will be used to migrate to remote state after initial deployment

# Step 1: Deploy infrastructure first with local state
# Step 2: Run: terraform init -backend-config=backend.hcl

resource_group_name  = "rg-eas-dev-audit-sys-001"
storage_account_name = "tfstatedevbkauditsys001"
container_name       = "dev-environment"
key                  = "terraform.tfstate"
