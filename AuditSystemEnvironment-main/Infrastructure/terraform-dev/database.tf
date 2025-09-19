# MySQL Flexible Server
resource "azurerm_mysql_flexible_server" "main" {
  name                   = "mysql-eas-${var.environment}-${var.project_name}-001"
  resource_group_name    = azurerm_resource_group.main.name
  location              = azurerm_resource_group.main.location
  administrator_login    = var.mysql_admin_username
  administrator_password = random_password.mysql_admin_password.result
  
  delegated_subnet_id    = azurerm_subnet.db.id
  private_dns_zone_id    = azurerm_private_dns_zone.mysql.id
  
  sku_name = var.mysql_sku_name
  version  = var.mysql_version
  
  storage {
    size_gb = var.mysql_storage_mb / 1024
    iops    = 360
  }

  backup_retention_days = 7
  geo_redundant_backup_enabled = false

  tags = var.tags

  depends_on = [azurerm_private_dns_zone_virtual_network_link.mysql]
}

# Private DNS Zone for MySQL
resource "azurerm_private_dns_zone" "mysql" {
  name                = "privatelink.mysql.database.azure.com"
  resource_group_name = azurerm_resource_group.main.name
  tags                = var.tags
}

# Link Private DNS Zone to VNet
resource "azurerm_private_dns_zone_virtual_network_link" "mysql" {
  name                  = "vnet-link-mysql"
  resource_group_name   = azurerm_resource_group.main.name
  private_dns_zone_name = azurerm_private_dns_zone.mysql.name
  virtual_network_id    = azurerm_virtual_network.main.id
  registration_enabled  = false
  tags                  = var.tags
}

# MySQL Database for AuditSystem
resource "azurerm_mysql_flexible_database" "audit_system" {
  name                = "auditsystem"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.main.name
  charset             = "utf8mb4"
  collation           = "utf8mb4_unicode_ci"
}

# MySQL Database for Nacos
resource "azurerm_mysql_flexible_database" "nacos" {
  name                = "nacos"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.main.name
  charset             = "utf8mb4"
  collation           = "utf8mb4_unicode_ci"
}

# MySQL Firewall Rule to allow access from VNet
resource "azurerm_mysql_flexible_server_firewall_rule" "vnet" {
  name                = "AllowVNet"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.main.name
  start_ip_address    = cidrhost(var.vnet_address_space[0], 0)
  end_ip_address      = cidrhost(var.vnet_address_space[0], pow(2, 32 - split("/", var.vnet_address_space[0])[1]) - 1)
}

# MySQL Configuration for optimized performance
resource "azurerm_mysql_flexible_server_configuration" "innodb_buffer_pool_size" {
  name                = "innodb_buffer_pool_size"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.main.name
  value               = "134217728" # 128MB for B1ms
}

resource "azurerm_mysql_flexible_server_configuration" "max_connections" {
  name                = "max_connections"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.main.name
  value               = "50" # Suitable for development
}

resource "azurerm_mysql_flexible_server_configuration" "sql_mode" {
  name                = "sql_mode"
  resource_group_name = azurerm_resource_group.main.name
  server_name         = azurerm_mysql_flexible_server.main.name
  value               = "STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO"
}
