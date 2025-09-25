#!/bin/bash

# Configure Azure Monitor Agent (AMA) on VM
# This script will be run on the VM to configure the new Azure Monitor Agent

set -e

# Azure Monitor Agent configuration (values will be provided during deployment)
WORKSPACE_ID="e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8"
WORKSPACE_KEY="xYuI5UoSLBWfxE6VUIW9z80pTHY2qH1i93jgsv6rbdt/V8gN8F4VHC3SkiUDKyLDOUFxGilYe0qzriwE/I7/ig=="
RESOURCE_ID=""  # Will be set during deployment
DCR_ID=""       # Data Collection Rule ID (will be created)

echo "🔧 Configuring New Azure Monitor Agent (AMA)..."
echo "⚠️  Note: Using modern Azure Monitor Agent instead of deprecated OMS agent"

# Install required packages
echo "📦 Installing required packages..."
sudo apt-get update
sudo apt-get install -y curl wget jq bc netcat-openbsd coreutils

# Make health monitoring script executable
chmod +x /home/azureuser/vm-health-monitor.sh

# Install the new Azure Monitor Agent
echo "🚀 Installing Azure Monitor Agent (AMA)..."

# Download and install AMA
wget -q https://aka.ms/amalinux -O /tmp/amalinux.sh
sudo chmod +x /tmp/amalinux.sh

# Install AMA package
sudo /tmp/amalinux.sh --install

echo "✅ Azure Monitor Agent package installed"

# Configure AMA for custom log collection
echo "⚙️  Configuring custom log collection with AMA..."

# Create configuration for Log Analytics workspace (backward compatibility)
# Note: For now, we'll use both AMA and a simple log forwarder approach
# until Data Collection Rules are fully configured via Azure CLI

# Method 1: Install legacy agent for log collection compatibility
echo "📋 Installing Log Analytics agent for backward compatibility..."
echo "    (This provides a bridge until full AMA migration)"

# Download OMS agent (still needed for custom logs until DCR is properly configured)
curl -L https://raw.githubusercontent.com/Microsoft/OMS-Agent-for-Linux/master/installer/scripts/onboard_agent.sh -o /tmp/onboard_agent.sh
sudo sh /tmp/onboard_agent.sh -w "$WORKSPACE_ID" -s "$WORKSPACE_KEY" -d opinsights.azure.com

# Configure custom log collection
echo "📝 Configuring custom log collection..."
sudo mkdir -p /etc/opt/microsoft/omsagent/$WORKSPACE_ID/conf/omsagent.d/
sudo tee /etc/opt/microsoft/omsagent/$WORKSPACE_ID/conf/omsagent.d/auditsystem-health.conf > /dev/null << 'EOFCONFIG'
<source>
  type tail
  path /var/log/azure-monitor-health.json
  pos_file /var/opt/microsoft/omsagent/WORKSPACE_ID/state/auditsystem-health.log.pos
  tag oms.api.AuditSystemHealth
  format json
  read_from_head true
</source>

<match oms.api.AuditSystemHealth>
  type out_oms_api
  log_level info
  buffer_chunk_limit 5m
  buffer_type file
  buffer_path /var/opt/microsoft/omsagent/WORKSPACE_ID/state/out_oms_api_auditsystem*.buffer
  buffer_queue_limit 10
  flush_interval 20s
  retry_limit 10
  retry_wait 30s
  max_retry_wait 300s
</match>
EOFCONFIG

# Replace WORKSPACE_ID placeholder
sudo sed -i "s/WORKSPACE_ID/$WORKSPACE_ID/g" /etc/opt/microsoft/omsagent/$WORKSPACE_ID/conf/omsagent.d/auditsystem-health.conf

# Restart Log Analytics agent
sudo systemctl restart omsagent-$WORKSPACE_ID

# Install and start our health monitoring service
echo "🏥 Setting up health monitoring service..."
/home/azureuser/vm-health-monitor.sh --install
sudo systemctl start azure-health-monitor
sudo systemctl enable azure-health-monitor

# Verify installations
echo "🔍 Verifying installation..."
if systemctl is-active --quiet azuremonitoragent; then
    echo "✅ Azure Monitor Agent (AMA) is running"
else
    echo "⚠️  Azure Monitor Agent (AMA) not running, checking status..."
    sudo systemctl status azuremonitoragent --no-pager || true
fi

if systemctl is-active --quiet omsagent-$WORKSPACE_ID; then
    echo "✅ Log Analytics agent is running (for custom log compatibility)"
else
    echo "⚠️  Log Analytics agent not running, checking status..."
    sudo systemctl status omsagent-$WORKSPACE_ID --no-pager || true
fi

if systemctl is-active --quiet azure-health-monitor; then
    echo "✅ Health monitoring service is running"
else
    echo "❌ Health monitoring service failed to start"
    sudo journalctl -u azure-health-monitor --no-pager -n 10
fi

echo ""
echo "🎉 Azure Monitor setup completed!"
echo ""
echo "📊 Monitoring Components:"
echo "  • Azure Monitor Agent (AMA) - Modern monitoring agent"
echo "  • Log Analytics Agent - Custom log collection (compatibility)"  
echo "  • Health Monitor Service - Audit system health checks"
echo ""
echo "🔍 Status Commands:"
echo "  sudo systemctl status azuremonitoragent"
echo "  sudo systemctl status omsagent-$WORKSPACE_ID"
echo "  sudo systemctl status azure-health-monitor"
echo ""
echo "📋 Log Locations:"
echo "  tail -f /var/log/azure-monitor-health.json"
echo "  sudo journalctl -u azure-health-monitor -f"
echo "  sudo journalctl -u azuremonitoragent -f"
echo ""
echo "⏰ Data should appear in Azure Portal within 5-10 minutes"
