#!/bin/bash

# Azure Monitor Data Verification Script
# This script helps you check if data is being sent to Azure Monitor

set -e

WORKSPACE_ID="e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8"

echo "🔍 Azure Monitor Data Verification"
echo "================================="
echo ""

# Check 1: Local Health Data Generation
echo "1️⃣  Checking Local Health Data Generation..."
if [ -f "/var/log/azure-monitor-health.json" ]; then
    LAST_ENTRY=$(tail -1 /var/log/azure-monitor-health.json 2>/dev/null)
    if [ -n "$LAST_ENTRY" ]; then
        echo "   ✅ Health data file exists and has content"
        echo "   📅 Last entry timestamp: $(echo "$LAST_ENTRY" | jq -r '.timestamp' 2>/dev/null || echo 'Could not parse timestamp')"
        echo "   📊 File size: $(ls -lh /var/log/azure-monitor-health.json | awk '{print $5}')"
    else
        echo "   ❌ Health data file exists but is empty"
    fi
else
    echo "   ❌ Health data file not found"
fi
echo ""

# Check 2: Azure Monitor Agents Status
echo "2️⃣  Checking Azure Monitor Agents Status..."

# Check new Azure Monitor Agent (AMA)
if systemctl is-active --quiet azuremonitoragent; then
    echo "   ✅ Azure Monitor Agent (AMA) is running"
    echo "   📅 AMA Status: $(sudo systemctl show azuremonitoragent --property=ActiveState --value)"
else
    echo "   ⚠️  Azure Monitor Agent (AMA) is not running"
    echo "   📊 AMA Status: $(sudo systemctl show azuremonitoragent --property=ActiveState --value 2>/dev/null || echo 'not installed')"
fi

# Check Log Analytics Agent (for custom log compatibility)
if systemctl is-active --quiet omsagent-$WORKSPACE_ID; then
    echo "   ✅ Log Analytics Agent is running"
    
    # Check recent heartbeat
    LOG_FILE="/var/opt/microsoft/omsagent/$WORKSPACE_ID/log/omsagent.log"
    if [ -f "$LOG_FILE" ]; then
        HEARTBEAT=$(sudo tail -50 "$LOG_FILE" | grep "Sending OMS Heartbeat succeeded" | tail -1)
        if [ -n "$HEARTBEAT" ]; then
            echo "   ✅ Log Analytics Agent is communicating with Azure"
            echo "   💓 Last heartbeat: $(echo "$HEARTBEAT" | grep -o '[0-9]\{4\}-[0-9]\{2\}-[0-9]\{2\}T[0-9]\{2\}:[0-9]\{2\}:[0-9]\{2\}\.[0-9]\{3\}Z')"
        else
            echo "   ⚠️  No recent heartbeat found in logs"
        fi
    else
        echo "   ⚠️  Log Analytics Agent log file not found"
    fi
else
    echo "   ❌ Log Analytics Agent is not running"
fi
echo ""

# Check 3: Configuration Verification
echo "3️⃣  Checking Configuration..."
CONFIG_FILE="/etc/opt/microsoft/omsagent/$WORKSPACE_ID/conf/omsagent.d/auditsystem-health.conf"
if [ -f "$CONFIG_FILE" ]; then
    echo "   ✅ Custom log configuration exists"
    echo "   📁 Monitoring: $(sudo grep "path" "$CONFIG_FILE" | awk '{print $2}')"
    echo "   🏷️  Log tag: $(sudo grep "tag" "$CONFIG_FILE" | awk '{print $2}')"
else
    echo "   ❌ Custom log configuration not found"
fi
echo ""

# Check 4: Recent OMS Agent Errors
echo "4️⃣  Checking for Recent OMS Agent Errors..."
RECENT_ERRORS=$(sudo journalctl -u omsagent-e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8 --since "10 minutes ago" | grep -i error | wc -l)
if [ "$RECENT_ERRORS" -eq 0 ]; then
    echo "   ✅ No errors in the last 10 minutes"
else
    echo "   ⚠️  Found $RECENT_ERRORS error(s) in the last 10 minutes"
    echo "   🔍 Recent errors:"
    sudo journalctl -u omsagent-e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8 --since "10 minutes ago" | grep -i error | tail -3 | sed 's/^/      /'
fi
echo ""

# Check 5: Health Monitor Status
echo "5️⃣  Checking Health Monitor Service..."
if systemctl is-active --quiet azure-health-monitor; then
    echo "   ✅ Health Monitor service is running"
    LAST_CHECK=$(sudo journalctl -u azure-health-monitor --since "5 minutes ago" | grep "Health check completed" | tail -1)
    if [ -n "$LAST_CHECK" ]; then
        echo "   ✅ Recent health check completed successfully"
        echo "   ⏰ Last check: $(echo "$LAST_CHECK" | grep -o '\[.*\]' | sed 's/\[//g' | sed 's/\]//g')"
    else
        echo "   ⚠️  No recent health check completion found"
    fi
else
    echo "   ❌ Health Monitor service is not running"
fi
echo ""

# Check 6: Data Upload Status
echo "6️⃣  Checking Data Upload Status..."
UPLOAD_SUCCESS=$(sudo tail -100 /var/opt/microsoft/omsagent/e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8/log/omsagent.log 2>/dev/null | grep "AuditSystemHealth" | grep -c "succeeded" || echo "0")
UPLOAD_ERRORS=$(sudo tail -100 /var/opt/microsoft/omsagent/e56a5a7c-ebf1-4aea-b13d-7cbf3287e7a8/log/omsagent.log 2>/dev/null | grep "AuditSystemHealth" | grep -c "failed\|error" || echo "0")

if [ "$UPLOAD_SUCCESS" -gt 0 ]; then
    echo "   ✅ Found $UPLOAD_SUCCESS successful data uploads in recent logs"
elif [ "$UPLOAD_ERRORS" -gt 0 ]; then
    echo "   ❌ Found $UPLOAD_ERRORS upload errors in recent logs"
else
    echo "   ℹ️  No specific upload status found in recent logs (this is normal for new setups)"
fi
echo ""

echo "🌐 How to Check Data in Azure Portal:"
echo "======================================"
echo "1. Go to Azure Portal: https://portal.azure.com"
echo "2. Navigate to 'Monitor' service"
echo "3. Click on 'Logs' in the left menu"
echo "4. Select your Log Analytics Workspace: $WORKSPACE_ID"
echo "5. Run this KQL query to see your health data:"
echo ""
echo "   AuditSystemHealth_CL"
echo "   | where TimeGenerated > ago(1h)"
echo "   | order by TimeGenerated desc"
echo "   | take 20"
echo ""
echo "Alternative queries:"
echo "   # See overall health status"
echo "   AuditSystemHealth_CL"
echo "   | where TimeGenerated > ago(30m)"
echo "   | extend OverallStatus = tostring(overall_health_status_s)"
echo "   | summarize count() by OverallStatus"
echo ""
echo "   # See service health trends"
echo "   AuditSystemHealth_CL"
echo "   | where TimeGenerated > ago(2h)"
echo "   | extend HealthyServices = toint(overall_health_healthy_services_d)"
echo "   | extend TotalServices = toint(overall_health_total_services_d)"
echo "   | project TimeGenerated, HealthyServices, TotalServices"
echo "   | render timechart"
echo ""

echo "📝 Notes:"
echo "--------"
echo "• Data typically appears in Azure Monitor within 3-15 minutes"
echo "• Custom log table name: AuditSystemHealth_CL"
echo "• Column names have '_s' (string), '_d' (double), '_t' (datetime) suffixes"
echo "• If no data appears after 30 minutes, check the OMS agent configuration"
echo ""
