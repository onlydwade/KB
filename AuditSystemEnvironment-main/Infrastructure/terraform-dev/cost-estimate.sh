#!/bin/bash

# Azure Cost Estimation for DEV Environment
# Prices as of August 2025 in USD (East Asia region)

echo "=========================================="
echo "  AuditSystem DEV Environment Cost Analysis"
echo "=========================================="
echo ""

# VM Cost (B4as v2 - 4 vCPU, 16GB RAM)
VM_HOURS_PER_DAY=15  # 9:00 to 23:59 = ~15 hours
VM_HOURS_PER_MONTH=$((VM_HOURS_PER_DAY * 30))
VM_HOURLY_RATE=0.1584  # USD per hour for B4as v2 in East Asia
VM_COST_PER_MONTH=$(echo "$VM_HOURS_PER_MONTH * $VM_HOURLY_RATE" | bc -l)

echo "💻 Virtual Machine (Standard_B4as_v2):"
echo "   Running: $VM_HOURS_PER_DAY hours/day (auto start/stop)"
echo "   Monthly hours: $VM_HOURS_PER_MONTH hours"
echo "   Hourly rate: \$$VM_HOURLY_RATE"
echo "   Monthly cost: \$$(printf "%.2f" $VM_COST_PER_MONTH)"
echo ""

# OS Disk Cost (30GB Standard LRS)
OS_DISK_SIZE=30
OS_DISK_RATE=0.048  # USD per GB per month for Standard LRS
OS_DISK_COST=$(echo "$OS_DISK_SIZE * $OS_DISK_RATE" | bc -l)

echo "💾 OS Disk (30GB Standard LRS):"
echo "   Size: ${OS_DISK_SIZE}GB"
echo "   Monthly rate: \$$OS_DISK_RATE per GB"
echo "   Monthly cost: \$$(printf "%.2f" $OS_DISK_COST)"
echo ""

# Additional Data Disk Cost (30GB Standard LRS)
DATA_DISK_SIZE=30
DATA_DISK_RATE=0.048  # USD per GB per month for Standard LRS
DATA_DISK_COST=$(echo "$DATA_DISK_SIZE * $DATA_DISK_RATE" | bc -l)

echo "💿 Additional Data Disk (30GB Standard LRS):"
echo "   Size: ${DATA_DISK_SIZE}GB"
echo "   Monthly rate: \$$DATA_DISK_RATE per GB"
echo "   Monthly cost: \$$(printf "%.2f" $DATA_DISK_COST)"
echo ""

# MySQL Cost (B1ms - 1 vCore, 2GB RAM)
MYSQL_HOURS_PER_MONTH=$VM_HOURS_PER_MONTH  # Same schedule as VM
MYSQL_HOURLY_RATE=0.0347  # USD per hour for B1ms
MYSQL_COST_PER_MONTH=$(echo "$MYSQL_HOURS_PER_MONTH * $MYSQL_HOURLY_RATE" | bc -l)

echo "🗄️  MySQL Flexible Server (B_Standard_B1ms):"
echo "   Running: $VM_HOURS_PER_DAY hours/day (auto start/stop)"
echo "   Monthly hours: $MYSQL_HOURS_PER_MONTH hours"
echo "   Hourly rate: \$$MYSQL_HOURLY_RATE"
echo "   Monthly cost: \$$(printf "%.2f" $MYSQL_COST_PER_MONTH)"
echo ""

# MySQL Storage Cost (20GB)
MYSQL_STORAGE_SIZE=20
MYSQL_STORAGE_RATE=0.115  # USD per GB per month
MYSQL_STORAGE_COST=$(echo "$MYSQL_STORAGE_SIZE * $MYSQL_STORAGE_RATE" | bc -l)

echo "🗄️  MySQL Storage (20GB):"
echo "   Size: ${MYSQL_STORAGE_SIZE}GB"
echo "   Monthly rate: \$$MYSQL_STORAGE_RATE per GB"
echo "   Monthly cost: \$$(printf "%.2f" $MYSQL_STORAGE_COST)"
echo ""

# Storage Account Cost
STORAGE_ACCOUNT_COST=5.00  # Estimated for Standard LRS with low usage

echo "💾 Storage Accounts (App + Terraform State):"
echo "   Type: Standard LRS"
echo "   Estimated monthly cost: \$$(printf "%.2f" $STORAGE_ACCOUNT_COST)"
echo ""

# Public IP Cost
PUBLIC_IP_COST=3.65  # USD per month for Standard Static IP

echo "🌐 Public IP Address:"
echo "   Type: Standard Static"
echo "   Monthly cost: \$$(printf "%.2f" $PUBLIC_IP_COST)"
echo ""

# Network (VNet, NSG, etc.)
NETWORK_COST=2.00  # Estimated for basic networking

echo "🌐 Networking (VNet, NSG, Private Endpoints):"
echo "   Estimated monthly cost: \$$(printf "%.2f" $NETWORK_COST)"
echo ""

# Automation Account
AUTOMATION_COST=5.00  # Basic tier

echo "⚙️  Automation Account (Auto Start/Stop):"
echo "   Tier: Basic"
echo "   Monthly cost: \$$(printf "%.2f" $AUTOMATION_COST)"
echo ""

# Calculate total
TOTAL_COST=$(echo "$VM_COST_PER_MONTH + $OS_DISK_COST + $DATA_DISK_COST + $MYSQL_COST_PER_MONTH + $MYSQL_STORAGE_COST + $STORAGE_ACCOUNT_COST + $PUBLIC_IP_COST + $NETWORK_COST + $AUTOMATION_COST" | bc -l)

echo "=========================================="
echo "📊 TOTAL MONTHLY COST BREAKDOWN:"
echo "=========================================="
printf "VM (15h/day):           \$%.2f\n" $VM_COST_PER_MONTH
printf "OS Disk (30GB):         \$%.2f\n" $OS_DISK_COST
printf "Data Disk (30GB):       \$%.2f\n" $DATA_DISK_COST
printf "MySQL (15h/day):        \$%.2f\n" $MYSQL_COST_PER_MONTH
printf "MySQL Storage (20GB):   \$%.2f\n" $MYSQL_STORAGE_COST
printf "Storage Accounts:       \$%.2f\n" $STORAGE_ACCOUNT_COST
printf "Public IP:              \$%.2f\n" $PUBLIC_IP_COST
printf "Networking:             \$%.2f\n" $NETWORK_COST
printf "Automation:             \$%.2f\n" $AUTOMATION_COST
echo "----------------------------------------"
printf "TOTAL:                  \$%.2f/month\n" $TOTAL_COST
echo "=========================================="
echo ""

# Cost savings calculation
ALWAYS_ON_VM_COST=$(echo "730 * $VM_HOURLY_RATE" | bc -l)  # 730 hours = full month
ALWAYS_ON_MYSQL_COST=$(echo "730 * $MYSQL_HOURLY_RATE" | bc -l)
ALWAYS_ON_TOTAL=$(echo "$ALWAYS_ON_VM_COST + $ALWAYS_ON_MYSQL_COST + $OS_DISK_COST + $DATA_DISK_COST + $MYSQL_STORAGE_COST + $STORAGE_ACCOUNT_COST + $PUBLIC_IP_COST + $NETWORK_COST + $AUTOMATION_COST" | bc -l)
SAVINGS=$(echo "$ALWAYS_ON_TOTAL - $TOTAL_COST" | bc -l)

echo "💰 COST SAVINGS WITH AUTO START/STOP:"
echo "=========================================="
printf "Without auto start/stop: \$%.2f/month\n" $ALWAYS_ON_TOTAL
printf "With auto start/stop:    \$%.2f/month\n" $TOTAL_COST
printf "Monthly savings:          \$%.2f (%.0f%%)\n" $SAVINGS $(echo "($SAVINGS / $ALWAYS_ON_TOTAL) * 100" | bc -l)
echo ""

# Annual cost
ANNUAL_COST=$(echo "$TOTAL_COST * 12" | bc -l)
echo "📅 ANNUAL COST: \$$(printf "%.2f" $ANNUAL_COST)"
echo ""

# Azure Sponsorship limit check
SPONSORSHIP_MONTHLY_LIMIT=200  # Assuming $200/month limit
REMAINING_BUDGET=$(echo "$SPONSORSHIP_MONTHLY_LIMIT - $TOTAL_COST" | bc -l)

echo "🎯 AZURE SPONSORSHIP BUDGET:"
echo "=========================================="
printf "Monthly limit:          \$%d\n" $SPONSORSHIP_MONTHLY_LIMIT
printf "Estimated usage:        \$%.2f\n" $TOTAL_COST
printf "Remaining budget:       \$%.2f\n" $REMAINING_BUDGET
printf "Budget utilization:     %.1f%%\n" $(echo "($TOTAL_COST / $SPONSORSHIP_MONTHLY_LIMIT) * 100" | bc -l)

if (( $(echo "$TOTAL_COST < $SPONSORSHIP_MONTHLY_LIMIT" | bc -l) )); then
    echo "✅ Within sponsorship budget!"
else
    echo "⚠️  Exceeds sponsorship budget!"
fi

echo ""
echo "📝 Notes:"
echo "- Prices are estimates based on East Asia region (August 2025)"
echo "- Auto start/stop runs 9:00-23:59 HK time (15 hours/day)"
echo "- Weekends included in the schedule"
echo "- Additional costs may apply for data transfer and API calls"
echo "- Prices may vary based on actual usage patterns"
