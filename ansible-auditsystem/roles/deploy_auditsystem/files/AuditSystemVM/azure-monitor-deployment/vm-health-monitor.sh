#!/bin/bash

# VM Health Monitor (No Azure credentials required)
# This script generates health data for Azure Monitor Agent pickup

set -e

LOG_FILE="/var/log/azure-monitor-health.json"
TEMP_FILE="/tmp/health-check-$(date +%s).json"

# Function to check service health (same as your existing logic)
check_service_health() {
    local service=$1
    local start_time=$(date +%s.%3N)
    local status="OK"
    local error_message=""
    
    case $service in
        "mysql")
            # Check if MySQL process is running and port is open
            if systemctl is-active --quiet mysql && nc -z localhost 3306 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="MySQL service not running or port 3306 not accessible"
            fi
            ;;
        "redis-server")
            # Check if Redis process is running and port is open
            if systemctl is-active --quiet redis-server && nc -z localhost 6379 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Redis service not running or port 6379 not accessible"
            fi
            ;;
        "rabbitmq-server")
            # Check if RabbitMQ process is running and port is open
            if systemctl is-active --quiet rabbitmq-server && nc -z localhost 5672 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="RabbitMQ service not running or port 5672 not accessible"
            fi
            ;;
        "nacos")
            # Check Nacos with timeout and better error handling
            if timeout 5 curl -f -s http://localhost:8848/nacos/ > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Nacos web interface not responding on port 8848"
            fi
            ;;
        "auditsystem-gateway")
            # Check Gateway service with timeout
            if timeout 5 curl -f -s http://localhost:8200/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Gateway health endpoint not responding on port 8200"
            fi
            ;;
        "auditsystem-auth")
            # Check Auth service with timeout
            if timeout 5 curl -f -s http://localhost:8300/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Auth service health endpoint not responding on port 8300"
            fi
            ;;
        "auditsystem-system")
            # Check System service with timeout
            if timeout 5 curl -f -s http://localhost:8400/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="System service health endpoint not responding on port 8400"
            fi
            ;;
        "auditsystem-biz")
            # Check Business service with timeout
            if timeout 5 curl -f -s http://localhost:8500/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Business service health endpoint not responding on port 8500"
            fi
            ;;
        "auditsystem-job")
            # Check Job service with timeout
            if timeout 5 curl -f -s http://localhost:8600/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Job service health endpoint not responding on port 8600"
            fi
            ;;
        "auditsystem-report")
            # Check Report service with timeout
            if timeout 5 curl -f -s http://localhost:8700/actuator/health > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Report service health endpoint not responding on port 8700"
            fi
            ;;
        "nginx")
            # Check Nginx with timeout and fallback to port check
            if timeout 5 curl -f -s http://localhost/health > /dev/null 2>&1 || timeout 5 curl -f -s http://localhost > /dev/null 2>&1 || nc -z localhost 80 > /dev/null 2>&1; then
                status="OK"
            else
                status="FAIL"
                error_message="Nginx not responding on port 80"
            fi
            ;;
    esac
    
    local end_time=$(date +%s.%3N)
    local response_time_ms=$(echo "($end_time - $start_time) * 1000" | bc | cut -d. -f1)
    
    cat << EOJSON
{
    "service": "$service",
    "status": "$status",
    "response_time_ms": $response_time_ms,
    "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)",
    "error_message": "$error_message"
}
EOJSON
}

# Function to get system metrics
get_system_metrics() {
    # Get CPU usage with fallback methods
    local cpu_usage=0
    if command -v top >/dev/null 2>&1; then
        cpu_usage=$(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | awk -F'%' '{print $1}' | tr -d ' ' 2>/dev/null || echo "0")
    elif [ -f /proc/loadavg ]; then
        # Alternative: use load average as CPU indicator
        cpu_usage=$(awk '{print $1*10}' /proc/loadavg 2>/dev/null || echo "0")
    fi
    
    # Get memory usage with error handling
    local memory_info=0
    if command -v free >/dev/null 2>&1; then
        memory_info=$(free -m | awk 'NR==2{printf "%.2f", $3*100/$2 }' 2>/dev/null || echo "0")
    elif [ -f /proc/meminfo ]; then
        # Alternative method using /proc/meminfo
        local total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2}' 2>/dev/null || echo "1")
        local free_mem=$(grep MemAvailable /proc/meminfo | awk '{print $2}' 2>/dev/null || echo "$total_mem")
        memory_info=$(echo "scale=2; (($total_mem - $free_mem) * 100) / $total_mem" | bc -l 2>/dev/null || echo "0")
    fi
    
    # Get disk usage with error handling
    local disk_usage=0
    if command -v df >/dev/null 2>&1; then
        disk_usage=$(df -h / | awk 'NR==2 {print $5}' | sed 's/%//g' 2>/dev/null || echo "0")
    fi
    
    # Get load average with error handling
    local load_avg=0
    if command -v uptime >/dev/null 2>&1; then
        load_avg=$(uptime | awk -F'load average:' '{print $2}' | awk '{print $1}' | sed 's/,//g' | tr -d ' ' 2>/dev/null || echo "0")
    elif [ -f /proc/loadavg ]; then
        load_avg=$(awk '{print $1}' /proc/loadavg 2>/dev/null || echo "0")
    fi
    
    # Ensure all values are numeric
    cpu_usage=${cpu_usage//[^0-9.]/}
    memory_info=${memory_info//[^0-9.]/}
    disk_usage=${disk_usage//[^0-9]/}
    load_avg=${load_avg//[^0-9.]/}
    
    # Set defaults if empty
    cpu_usage=${cpu_usage:-0}
    memory_info=${memory_info:-0}
    disk_usage=${disk_usage:-0}
    load_avg=${load_avg:-0}
    
    cat << EOJSON
{
    "cpu_usage_percent": $cpu_usage,
    "memory_usage_percent": $memory_info,
    "disk_usage_percent": $disk_usage,
    "load_average_1min": $load_avg,
    "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)"
}
EOJSON
}

# Main health check function
generate_health_report() {
    local services=("mysql" "redis-server" "rabbitmq-server" "nacos" "auditsystem-gateway" "auditsystem-auth" "auditsystem-system" "auditsystem-biz" "auditsystem-job" "auditsystem-report" "nginx")
    
    # Add error handling and logging
    exec 2>/tmp/health-check-error.log
    
    echo "{"
    echo "  \"timestamp\": \"$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)\","
    echo "  \"hostname\": \"$(hostname)\","
    echo "  \"services\": ["
    
    local first=true
    local failed_services=0
    local total_services=${#services[@]}
    
    for service in "${services[@]}"; do
        if [ "$first" = true ]; then
            first=false
        else
            echo ","
        fi
        
        # Capture service health with error handling
        local service_health
        if service_health=$(check_service_health "$service" 2>/dev/null); then
            echo "    $service_health"
            # Check if this service failed
            if echo "$service_health" | grep -q '"status": "FAIL"'; then
                ((failed_services++))
            fi
        else
            # If check_service_health fails completely, create a failure entry
            ((failed_services++))
            cat << EOJSON
    {
        "service": "$service",
        "status": "FAIL",
        "response_time_ms": 0,
        "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)",
        "error_message": "Health check function failed"
    }
EOJSON
        fi
    done
    
    echo "  ],"
    
    # Get system metrics with error handling
    local system_metrics
    if system_metrics=$(get_system_metrics 2>/dev/null); then
        echo "  \"system_metrics\": $system_metrics,"
    else
        echo "  \"system_metrics\": {"
        echo "    \"cpu_usage_percent\": 0,"
        echo "    \"memory_usage_percent\": 0,"
        echo "    \"disk_usage_percent\": 0,"
        echo "    \"load_average_1min\": 0,"
        echo "    \"timestamp\": \"$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)\","
        echo "    \"error\": \"Failed to collect system metrics\""
        echo "  },"
    fi
    
    local health_percentage=$(echo "scale=2; ($total_services - $failed_services) * 100 / $total_services" | bc -l 2>/dev/null || echo "0")
    
    echo "  \"overall_health\": {"
    echo "    \"status\": \"$([ $failed_services -eq 0 ] && echo "HEALTHY" || echo "DEGRADED")\","
    echo "    \"healthy_services\": $(($total_services - $failed_services)),"
    echo "    \"total_services\": $total_services,"
    echo "    \"health_percentage\": $health_percentage"
    echo "  }"
    echo "}"
}

# Execute based on command line argument
case "$1" in
    "--check")
        # One-time health check
        generate_health_report
        ;;
    "--daemon")
        # Continuous monitoring (writes to log file for Azure Monitor Agent)
        echo "[$(date)] Starting health monitoring daemon..."
        
        # Ensure log directory exists and set proper permissions
        sudo mkdir -p /var/log
        sudo touch "$LOG_FILE"
        sudo chown azureuser:azureuser "$LOG_FILE"
        sudo chmod 644 "$LOG_FILE"
        
        # Main monitoring loop with error recovery
        while true; do
            echo "[$(date)] Running health check..."
            
            # Generate health report with error handling
            if generate_health_report > "$TEMP_FILE" 2>&1; then
                if [ -s "$TEMP_FILE" ] && grep -q "timestamp" "$TEMP_FILE"; then
                    # Valid JSON file, compress to single line and append to log file
                    jq -c . "$TEMP_FILE" >> "$LOG_FILE" 2>/dev/null || cat "$TEMP_FILE" >> "$LOG_FILE"
                    
                    # Keep log file size manageable (last 1000 lines)
                    if [ -f "$LOG_FILE" ]; then
                        tail -1000 "$LOG_FILE" > "$TEMP_FILE.tmp" 
                        sudo cp "$TEMP_FILE.tmp" "$LOG_FILE"
                        sudo chown azureuser:azureuser "$LOG_FILE"
                        rm -f "$TEMP_FILE.tmp"
                    fi
                    
                    echo "[$(date)] ✅ Health check completed and logged"
                else
                    echo "[$(date)] ⚠️  WARNING: Invalid health report generated, contents:"
                    cat "$TEMP_FILE" | head -5
                    echo "[$(date)] Continuing with next cycle..."
                fi
            else
                echo "[$(date)] ❌ ERROR: Health report generation failed"
                
                # Log the error for debugging
                cat << EOERROR >> "$LOG_FILE"
{
    "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)",
    "hostname": "$(hostname)",
    "error": "Health check generation failed",
    "details": "$(cat $TEMP_FILE 2>/dev/null | head -3 | tr '\n' ' ')"
}

EOERROR
            fi
            
            # Clean up temp file
            [ -f "$TEMP_FILE" ] && rm -f "$TEMP_FILE"
            
            echo "[$(date)] Sleeping for 2 minutes..."
            sleep 120  # 2 minutes
        done
        ;;
    "--install")
        # Install as systemd service
        echo "Installing health monitoring service..."
        
        # Create systemd service
        sudo tee /etc/systemd/system/azure-health-monitor.service > /dev/null << 'EOSERVICE'
[Unit]
Description=Azure Health Monitor for AuditSystem
After=network.target mysql.service nacos.service

[Service]
Type=simple
User=azureuser
Group=azureuser
WorkingDirectory=/home/azureuser
ExecStart=/home/azureuser/vm-health-monitor.sh --daemon
Restart=always
RestartSec=30
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOSERVICE

        sudo systemctl daemon-reload
        sudo systemctl enable azure-health-monitor.service
        echo "✅ Service installed. Start with: sudo systemctl start azure-health-monitor"
        ;;
    *)
        echo "Usage: $0 [--check|--daemon|--install]"
        echo "  --check    Run one-time health check"
        echo "  --daemon   Run continuous monitoring"
        echo "  --install  Install as systemd service"
        ;;
esac
