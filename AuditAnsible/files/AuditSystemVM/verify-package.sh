#!/bin/bash

# AuditSystem VM Package Verification Script
# This script verifies that all required files are present and executable

echo "=== AuditSystem VM Package Verification ==="
echo "$(date)"
echo "==========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PACKAGE_DIR="/Users/luiswing/Desktop/GTI/AuditSystemEnvironment/AuditSystemVM"
ERRORS=0
WARNINGS=0

check_file() {
    local file=$1
    local description=$2
    local should_be_executable=$3
    
    if [ -f "$PACKAGE_DIR/$file" ]; then
        echo -e "${GREEN}✅ $file${NC} - $description"
        
        if [ "$should_be_executable" = "yes" ]; then
            if [ -x "$PACKAGE_DIR/$file" ]; then
                echo -e "   ${GREEN}   Executable: YES${NC}"
            else
                echo -e "   ${YELLOW}   Executable: NO (run: chmod +x $file)${NC}"
                ((WARNINGS++))
            fi
        fi
    else
        echo -e "${RED}❌ $file${NC} - $description (MISSING)"
        ((ERRORS++))
    fi
}

check_directory() {
    local dir=$1
    local description=$2
    
    if [ -d "$PACKAGE_DIR/$dir" ]; then
        echo -e "${GREEN}✅ $dir/${NC} - $description"
    else
        echo -e "${RED}❌ $dir/${NC} - $description (MISSING)"
        ((ERRORS++))
    fi
}

echo "📋 Checking documentation files..."
check_file "README.md" "Main documentation and setup guide" "no"
check_file "QUICK-START.md" "Quick deployment guide" "no"

echo ""
echo "🛠️  Checking deployment scripts..."
check_file "install-ubuntu-setup.sh" "System setup and dependencies installation" "yes"
check_file "setup-nacos.sh" "Nacos service discovery configuration" "yes"
check_file "deploy-backend.sh" "Backend microservices deployment" "yes"
check_file "deploy-frontend.sh" "Frontend Vue.js application deployment" "yes"
check_file "configure-nginx.sh" "Nginx web server configuration" "yes"
check_file "manage-services.sh" "Service lifecycle management" "yes"

echo ""
echo "📁 Checking troubleshooting directory..."
check_directory "troubleshooting" "Troubleshooting guides and documentation"

if [ -d "$PACKAGE_DIR/troubleshooting" ]; then
    echo "   📄 Checking troubleshooting files..."
    check_file "troubleshooting/common-issues.md" "Common problems and solutions" "no"
    check_file "troubleshooting/service-logs.md" "Service logging guide" "no"
    check_file "troubleshooting/port-conflicts.md" "Port conflict resolution" "no"
fi

echo ""
echo "🔍 Package completeness check..."

# Count files
total_scripts=$(ls -1 "$PACKAGE_DIR"/*.sh 2>/dev/null | wc -l)
total_docs=$(ls -1 "$PACKAGE_DIR"/*.md 2>/dev/null | wc -l)
total_troubleshooting=$(ls -1 "$PACKAGE_DIR/troubleshooting"/*.md 2>/dev/null | wc -l)

echo "   📊 Scripts found: $total_scripts/6"
echo "   📊 Documentation files: $total_docs/2"
echo "   📊 Troubleshooting guides: $total_troubleshooting/3"

echo ""
echo "💡 Usage verification..."

if [ -f "$PACKAGE_DIR/install-ubuntu-setup.sh" ] && [ -x "$PACKAGE_DIR/install-ubuntu-setup.sh" ]; then
    echo -e "${GREEN}✅ Ready for deployment${NC}"
    echo "   To deploy: cd AuditSystemVM && ./install-ubuntu-setup.sh"
else
    echo -e "${RED}❌ Not ready for deployment${NC}"
    echo "   Missing or non-executable install-ubuntu-setup.sh"
    ((ERRORS++))
fi

echo ""
echo "📋 Prerequisites check (for reference)..."
echo "   • Ubuntu 24.04 LTS"
echo "   • 8GB+ RAM"
echo "   • 20GB+ free disk space"
echo "   • Internet access"
echo "   • Non-root user with sudo privileges"
echo "   • Access to GitHub repositories"

echo ""
echo "==========================================="
echo "📊 Verification Summary:"

if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    echo -e "${GREEN}✅ PACKAGE COMPLETE - Ready for deployment!${NC}"
    echo ""
    echo "🚀 Quick deployment command:"
    echo "   cd AuditSystemVM"
    echo "   chmod +x *.sh"
    echo "   ./install-ubuntu-setup.sh && ./setup-nacos.sh && ./deploy-backend.sh && ./deploy-frontend.sh && ./configure-nginx.sh && ./manage-services.sh start"
elif [ $ERRORS -eq 0 ]; then
    echo -e "${YELLOW}⚠️  PACKAGE MOSTLY COMPLETE - ${WARNINGS} warnings${NC}"
    echo "   Run: chmod +x *.sh to fix executable permissions"
else
    echo -e "${RED}❌ PACKAGE INCOMPLETE - ${ERRORS} errors, ${WARNINGS} warnings${NC}"
    echo "   Please check missing files and fix issues before deployment"
fi

echo ""
echo "📖 For detailed instructions, see README.md"
echo "⚡ For quick start, see QUICK-START.md"

exit $ERRORS
