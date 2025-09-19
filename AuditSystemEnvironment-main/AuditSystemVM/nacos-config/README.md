# Nacos Configuration Files

This directory contains all the Nacos configuration files required for the AuditSystem microservices.

## 📁 Structure

```
nacos-config/
├── README.md                           # This documentation
├── setup-commands.sh                   # All curl commands to setup Nacos
├── shared/
│   └── application-local.yml           # Shared configuration for all services
└── services/
    ├── bytefinger-auth.yml             # Auth service configuration
    ├── bytefinger-system.yml           # System service configuration
    ├── bytefinger-biz.yml              # Business service configuration
    ├── bytefinger-job.yml              # Job service configuration
    ├── bytefinger-report.yml           # Report service configuration
    └── bytefinger-gateway.yml          # Gateway service configuration
```

## 🚀 Quick Setup

To apply all configurations to Nacos:

```bash
cd AuditSystemVM/nacos-config
chmod +x setup-commands.sh
./setup-commands.sh
```

## 📋 Configuration Details

### Nacos Structure
- **Namespace**: `bytefinger-audit-local` (for service-specific configs)
- **Group**: 
  - `DEFAULT_GROUP` (for shared configs)
  - `bytefinger-audit-local` (for service-specific configs)

### Services Configuration
Each service configuration includes:
- Server port settings
- Application name
- Management endpoints (health check, metrics)
- Service-specific configurations

### Shared Configuration
The shared `application-local.yml` includes:
- Database connection settings (MySQL)
- Redis configuration
- RabbitMQ settings
- Common management endpoints

## 🔧 Manual Configuration

If you prefer to configure manually through Nacos console:

1. Access Nacos console: http://your-vm-ip:8848/nacos (nacos/nacos)
2. Go to "Configuration Management" → "Configurations"
3. Create each configuration with the Data ID, Group, and Content from the YAML files
4. Use the namespace `bytefinger-audit-local` for service configs
5. Use `DEFAULT_GROUP` for shared configs

## 📝 Notes

- All configurations are designed for the `local` environment
- Database name: `bytefinger_toutuo`
- All services include health check endpoints
- Gateway includes routing configuration for all microservices
