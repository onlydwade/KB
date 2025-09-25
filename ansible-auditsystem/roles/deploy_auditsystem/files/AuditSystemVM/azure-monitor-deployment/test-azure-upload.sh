#!/bin/bash

echo "Testing Azure Monitor Integration"
echo "================================"

# Generate a simple test entry
TEST_LOG='{"test": "true", "timestamp": "'$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)'", "message": "Azure Monitor test entry", "hostname": "'$(hostname)'"}'

echo "Adding test entry to log file..."
echo "$TEST_LOG" >> /var/log/azure-monitor-health.json

echo "Test entry added. Check Azure Monitor in 5-10 minutes for:"
echo "  Table: AuditSystemHealth_CL"
echo "  Query: AuditSystemHealth_CL | where test_s == 'true'"
echo ""

echo "Recent log entries (last 2):"
tail -2 /var/log/azure-monitor-health.json | head -1 | jq '.' 2>/dev/null || echo "JSON formatting issue detected"
echo ""
echo "Latest entry:"
tail -1 /var/log/azure-monitor-health.json | jq '.' 2>/dev/null || echo "Latest entry (raw):" && tail -1 /var/log/azure-monitor-health.json
