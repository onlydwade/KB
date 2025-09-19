# Public IP for VM
resource "azurerm_public_ip" "vm" {
  name                = "pip-vm${var.environment}${var.company_short}${var.project_name}001"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  allocation_method   = "Static"
  sku                 = "Standard"
  tags                = var.tags
}

# Network Interface for VM
resource "azurerm_network_interface" "vm" {
  name                = "nic-vm${var.environment}${var.company_short}${var.project_name}001"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  tags                = var.tags

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.app.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.vm.id
  }
}

# Additional Data Disk for VM
resource "azurerm_managed_disk" "vm_data" {
  name                 = "disk-vm${var.environment}${var.company_short}${var.project_name}001-data"
  location             = azurerm_resource_group.main.location
  resource_group_name  = azurerm_resource_group.main.name
  storage_account_type = "Standard_LRS"  # Changed to cheaper storage
  create_option        = "Empty"
  disk_size_gb         = var.additional_disk_size_gb
  tags                 = var.tags
}

# Virtual Machine
resource "azurerm_linux_virtual_machine" "main" {
  name                = "vm${var.environment}${var.company_short}${var.project_name}001"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  size                = var.vm_size
  admin_username      = var.vm_admin_username
  tags                = var.tags

  # Disable password authentication and use fixed password
  disable_password_authentication = false
  admin_password                  = var.vm_admin_password

  network_interface_ids = [
    azurerm_network_interface.vm.id,
  ]

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"  # Changed to cheaper storage
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts-gen2"
    version   = "latest"
  }

  # Cloud-init script for initial setup
  custom_data = base64encode(<<-EOF
    #!/bin/bash
    
    # Log start
    echo "VM setup started at $(date)" > /var/log/vm-setup.log
    
    # Update system first
    apt-get update
    apt-get upgrade -y
    
    # Install git FIRST as requested
    apt-get install -y git
    echo "Git installed successfully at $(date)" >> /var/log/vm-setup.log
    
    # Install basic tools
    apt-get install -y curl wget unzip htop vim nano
    
    # Enable SSH and configure firewall
    ufw --force reset
    ufw default deny incoming
    ufw default allow outgoing
    ufw allow ssh
    ufw allow 22/tcp
    ufw allow 8200/tcp  # API Gateway
    ufw allow 8848/tcp  # Nacos
    ufw allow 80/tcp    # HTTP
    ufw allow 443/tcp   # HTTPS
    ufw --force enable
    echo "UFW firewall configured and SSH enabled at $(date)" >> /var/log/vm-setup.log
    
    # Ensure SSH service is running
    systemctl enable ssh
    systemctl start ssh
    systemctl status ssh >> /var/log/vm-setup.log
    
    # Create mount point and format additional disk
    if [ -b /dev/sdc ]; then
      mkfs.ext4 /dev/sdc
      mkdir -p /data
      mount /dev/sdc /data
      echo '/dev/sdc /data ext4 defaults 0 2' >> /etc/fstab
      chown -R ${var.vm_admin_username}:${var.vm_admin_username} /data
      echo "Additional disk mounted at $(date)" >> /var/log/vm-setup.log
    fi
    
    # Install Docker (optional for testing)
    curl -fsSL https://get.docker.com -o get-docker.sh
    sh get-docker.sh
    usermod -aG docker ${var.vm_admin_username}
    echo "Docker installed at $(date)" >> /var/log/vm-setup.log
    
    # Create deployment directory
    mkdir -p /opt/auditsystem
    chown -R ${var.vm_admin_username}:${var.vm_admin_username} /opt/auditsystem
    
    # Set up git configuration for azureuser
    sudo -u ${var.vm_admin_username} git config --global user.name "Azure User"
    sudo -u ${var.vm_admin_username} git config --global user.email "azureuser@auditsystem.local"
    sudo -u ${var.vm_admin_username} git config --global init.defaultBranch main
    
    # Log completion
    echo "VM setup completed successfully at $(date)" >> /var/log/vm-setup.log
    echo "Services status:" >> /var/log/vm-setup.log
    systemctl is-active ssh >> /var/log/vm-setup.log
    ufw status >> /var/log/vm-setup.log
  EOF
  )
}

# Attach additional data disk to VM
resource "azurerm_virtual_machine_data_disk_attachment" "vm_data" {
  managed_disk_id    = azurerm_managed_disk.vm_data.id
  virtual_machine_id = azurerm_linux_virtual_machine.main.id
  lun                = "10"
  caching            = "ReadWrite"
}
