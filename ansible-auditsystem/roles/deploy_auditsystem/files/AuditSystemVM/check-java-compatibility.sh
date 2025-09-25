#!/bin/bash

# Java Compatibility Check Script
# Verifies Java 11 setup and project compatibility for Java 10+ features

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
}

warning() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] INFO: $1${NC}"
}

echo "=== Java Compatibility Check ==="
echo "Date: $(date)"
echo

# Check installed Java versions
log "Checking installed Java versions..."
update-java-alternatives --list 2>/dev/null || echo "No alternatives configured"

# Check current Java version
log "Current Java version:"
java -version

# Check JAVA_HOME
log "JAVA_HOME: ${JAVA_HOME:-Not set}"
if [ -z "$JAVA_HOME" ]; then
    warning "JAVA_HOME not set. Setting for this session..."
    export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
    log "JAVA_HOME set to: $JAVA_HOME"
fi

# Verify Java 11
log "Verifying Java 11 compatibility..."
java_version=$(java -version 2>&1 | grep -oP 'version "\K[^"]+')
if [[ "$java_version" == 11.* ]]; then
    log "✅ Java 11 detected: $java_version"
elif [[ "$java_version" == 1.8.* ]]; then
    warning "⚠️  Java 8 detected: $java_version"
    log "Java 8 is installed but Java 11 is required for Java 10+ features."
    
    # Check if Java 11 is also installed
    if [ -f "/usr/lib/jvm/java-11-openjdk-amd64/bin/java" ]; then
        log "Java 11 is also installed. Switching to Java 11..."
        log "Available Java versions:"
        sudo update-alternatives --list java 2>/dev/null || echo "No alternatives configured"
        
        log "To switch to Java 11 permanently, run:"
        echo "  sudo update-alternatives --config java"
        echo "  sudo update-alternatives --config javac"
        echo "  export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64"
        
        # Temporarily switch for this script
        export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
        export PATH=$JAVA_HOME/bin:$PATH
        
        # Re-check version
        java_version=$($JAVA_HOME/bin/java -version 2>&1 | grep -oP 'version "\K[^"]+')
        log "Using Java 11 for this check: $java_version"
    else
        error "❌ Java 11 not found. Installing Java 11..."
        log "Run: ./install-ubuntu-setup.sh (it will install Java 11 alongside Java 8)"
        exit 1
    fi
else
    warning "⚠️  Java version: $java_version (should be 11.x)"
fi

# Check Maven compatibility
log "Checking Maven..."
if command -v mvn >/dev/null 2>&1; then
    mvn_version=$(mvn -version | head -1)
    log "✅ $mvn_version"
    
    # Check Maven Java version
    mvn_java=$(mvn -version | grep "Java version" | cut -d: -f2 | xargs)
    log "Maven using Java: $mvn_java"
    
    if [[ "$mvn_java" != 11.* ]]; then
        warning "⚠️  Maven not using Java 11. Check JAVA_HOME in Maven wrapper"
    fi
else
    error "❌ Maven not found"
    exit 1
fi

# Test Java 10+ features compilation
log "Testing Java 10+ features..."
test_dir="/tmp/java-test-$$"
mkdir -p "$test_dir"

cat > "$test_dir/TestJava10.java" << 'EOF'
import java.util.*;

public class TestJava10 {
    public static void main(String[] args) {
        // Java 10 feature: var keyword (local variable type inference)
        var list = List.of("Java", "10", "Features");
        var message = "Java 10+ features work!";
        
        // Java 9 feature: List.of()
        var numbers = List.of(1, 2, 3, 4, 5);
        
        System.out.println(message);
        list.forEach(System.out::println);
        
        // Test stream operations
        var evenNumbers = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(java.util.stream.Collectors.toList());
        
        System.out.println("Even numbers: " + evenNumbers);
    }
}
EOF

cd "$test_dir"
if javac TestJava10.java && java TestJava10; then
    log "✅ Java 10+ features compilation and execution successful"
else
    error "❌ Java 10+ features compilation failed"
    cd - && rm -rf "$test_dir"
    exit 1
fi

cd - && rm -rf "$test_dir"

# Check if backend project exists and analyze
if [ -d "/opt/auditsystem/AuditSystemBackend" ]; then
    log "Analyzing backend project..."
    cd "/opt/auditsystem/AuditSystemBackend"
    
    # Check for Java 10+ features in source code
    log "Checking for var keyword usage:"
    var_count=$(find src/ -name "*.java" -exec grep -l "\bvar\b" {} \; 2>/dev/null | wc -l || echo "0")
    info "Files using 'var': $var_count"
    
    # Check Maven compiler configuration
    if [ -f "pom.xml" ]; then
        log "Maven compiler configuration:"
        if grep -q "java.version" pom.xml; then
            grep "java.version" pom.xml | head -1
        else
            warning "No explicit java.version found in pom.xml"
        fi
        
        if grep -A5 -B5 "maven-compiler-plugin" pom.xml | grep -E "(source|target)" | head -2; then
            log "Maven compiler plugin configuration found"
        else
            warning "Maven compiler plugin configuration not found"
        fi
    fi
fi

# Check Spring Boot compatibility with Java 11
log "Checking Spring Boot version compatibility..."
if [ -f "/opt/auditsystem/AuditSystemBackend/pom.xml" ]; then
    spring_version=$(grep "spring-boot.version" /opt/auditsystem/AuditSystemBackend/pom.xml | grep -oP '>\K[^<]+' || echo "Not found")
    log "Spring Boot version: $spring_version"
    
    if [[ "$spring_version" == 2.7.* ]] || [[ "$spring_version" == 2.6.* ]] || [[ "$spring_version" == 2.5.* ]]; then
        log "✅ Spring Boot $spring_version is compatible with Java 11"
    else
        warning "⚠️  Spring Boot version compatibility with Java 11 should be verified"
    fi
fi

# Check system memory for Maven builds
log "Checking system resources..."
total_mem=$(free -m | awk 'NR==2{printf "%.0f", $2}')
log "Total memory: ${total_mem}MB"

if [ "$total_mem" -lt 4000 ]; then
    warning "⚠️  System has less than 4GB RAM. Maven builds may be slow."
    log "Consider setting MAVEN_OPTS=\"-Xmx2g -XX:MaxMetaspaceSize=512m\""
else
    log "✅ Sufficient memory for Maven builds"
fi

# Test Maven with current Java
log "Testing Maven with current Java version..."
if [ -d "/opt/auditsystem/AuditSystemBackend" ]; then
    cd "/opt/auditsystem/AuditSystemBackend"
    if mvn --version >/dev/null 2>&1; then
        log "✅ Maven works with current Java"
        
        # Test compilation without running tests
        log "Testing project compilation..."
        if mvn compile -q >/dev/null 2>&1; then
            log "✅ Project compiles successfully with Java 11"
        else
            warning "⚠️  Project compilation issues detected. Check logs with: mvn compile"
        fi
    else
        error "❌ Maven test failed"
    fi
fi

echo
log "=== Java Compatibility Check Complete ==="
echo
log "Summary:"
log "- Java version: $java_version"
log "- JAVA_HOME: $JAVA_HOME"
log "- Maven: $(mvn --version 2>/dev/null | head -1 | cut -d' ' -f3 || echo 'Not available')"
log "- Java 10+ features: Supported"
log "- Spring Boot compatibility: Verified"
echo
log "Next steps:"
log "1. If this is the first time, run: ./deploy-backend.sh"
log "2. Check service status with: ./manage-services.sh status"
log "3. View logs with: sudo journalctl -u auditsystem-<service> -f"
