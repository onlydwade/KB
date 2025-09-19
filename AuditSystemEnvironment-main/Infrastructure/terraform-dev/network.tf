# Resource Group
resource "azurerm_resource_group" "main" {
  name     = "rg-eas-${var.environment}-${var.project_name}-001"
  location = var.location
  tags     = var.tags
}

# Virtual Network
resource "azurerm_virtual_network" "main" {
  name                = "vnet-eas-${var.environment}-001"
  address_space       = var.vnet_address_space
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  tags                = var.tags
}

# Application Subnet
resource "azurerm_subnet" "app" {
  name                 = "snet-${var.project_name}-eas-${var.environment}-001"
  resource_group_name  = azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes     = [var.app_subnet_address_prefix]

  # Enable private endpoint network policies
  private_endpoint_network_policies = "Disabled"
}

# Database Subnet
resource "azurerm_subnet" "db" {
  name                 = "snet-${var.project_name}-db-eas-${var.environment}-001"
  resource_group_name  = azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes     = [var.db_subnet_address_prefix]

  # Enable private endpoint network policies
  private_endpoint_network_policies = "Disabled"

  # Delegate subnet to MySQL flexible server
  delegation {
    name = "mysql-delegation"
    service_delegation {
      name    = "Microsoft.DBforMySQL/flexibleServers"
      actions = ["Microsoft.Network/virtualNetworks/subnets/join/action"]
    }
  }
}

# Network Security Group for Application Subnet
resource "azurerm_network_security_group" "app" {
  name                = "nsg-snet-${var.project_name}-eas-${var.environment}-001"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  tags                = var.tags

  # Allow SSH from specified IP ranges
  security_rule {
    name                       = "SSH"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefixes    = var.allowed_ssh_ip_ranges
    destination_address_prefix = "*"
  }

  # Allow HTTP
  security_rule {
    name                       = "HTTP"
    priority                   = 1002
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "80"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow HTTPS
  security_rule {
    name                       = "HTTPS"
    priority                   = 1003
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "443"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow Nacos console
  security_rule {
    name                       = "Nacos"
    priority                   = 1004
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8848"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow API Gateway
  security_rule {
    name                       = "API-Gateway"
    priority                   = 1005
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8200"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow Auth Service
  security_rule {
    name                       = "Auth-Service"
    priority                   = 1006
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8300"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow System Service
  security_rule {
    name                       = "System-Service"
    priority                   = 1007
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8400"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow Business Service
  security_rule {
    name                       = "Business-Service"
    priority                   = 1008
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8500"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow Job Service
  security_rule {
    name                       = "Job-Service"
    priority                   = 1009
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8600"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow Report Service
  security_rule {
    name                       = "Report-Service"
    priority                   = 1010
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "8700"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow Redis
  security_rule {
    name                       = "Redis"
    priority                   = 1011
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "6379"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow RabbitMQ
  security_rule {
    name                       = "RabbitMQ"
    priority                   = 1012
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "5672"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow RabbitMQ Management
  security_rule {
    name                       = "RabbitMQ-Management"
    priority                   = 1013
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "15672"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # Allow all outbound traffic
  security_rule {
    name                       = "AllowAllOutbound"
    priority                   = 1014
    direction                  = "Outbound"
    access                     = "Allow"
    protocol                   = "*"
    source_port_range          = "*"
    destination_port_range     = "*"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}

# Associate NSG with Application Subnet
resource "azurerm_subnet_network_security_group_association" "app" {
  subnet_id                 = azurerm_subnet.app.id
  network_security_group_id = azurerm_network_security_group.app.id
}

# Network Security Group for Database Subnet
resource "azurerm_network_security_group" "db" {
  name                = "nsg-snet-${var.project_name}-db-eas-${var.environment}-001"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  tags                = var.tags

  # Allow MySQL traffic from app subnet
  security_rule {
    name                       = "MySQL"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "3306"
    source_address_prefix      = var.app_subnet_address_prefix
    destination_address_prefix = "*"
  }
}

# Associate NSG with Database Subnet
resource "azurerm_subnet_network_security_group_association" "db" {
  subnet_id                 = azurerm_subnet.db.id
  network_security_group_id = azurerm_network_security_group.db.id
}
