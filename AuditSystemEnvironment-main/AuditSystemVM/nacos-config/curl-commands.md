# Nacos Configuration Setup - Raw curl Commands

This file contains all the raw curl commands used to configure Nacos for the AuditSystem microservices.

## Database Setup
```bash
mysql -u root -proot123 -e "CREATE DATABASE IF NOT EXISTS bytefinger_toutuo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

## 1. Shared Application Configuration
**Data ID:** `application-local.yml`  
**Group:** `DEFAULT_GROUP`  
**Namespace:** `public`

```bash
curl -X POST "http://localhost:8848/nacos/v1/cs/configs" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "dataId=application-local.yml&group=DEFAULT_GROUP&content=spring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20auditsystem%0A%20%20datasource%3A%0A%20%20%20%20url%3A%20jdbc%3Amysql%3A//localhost%3A3306/bytefinger_toutuo%3FuseUnicode%3Dtrue%26characterEncoding%3Dutf8%26zeroDateTimeBehavior%3DconvertToNull%26useSSL%3Dfalse%26serverTimezone%3DGMT%252B8%0A%20%20%20%20username%3A%20auditsystem%0A%20%20%20%20password%3A%20audit123%0A%20%20%20%20driver-class-name%3A%20com.mysql.cj.jdbc.Driver%0A%20%20redis%3A%0A%20%20%20%20host%3A%20localhost%0A%20%20%20%20port%3A%206379%0A%20%20%20%20timeout%3A%2010000ms%0A%20%20%20%20lettuce%3A%0A%20%20%20%20%20%20pool%3A%0A%20%20%20%20%20%20%20%20max-active%3A%20200%0A%20%20rabbitmq%3A%0A%20%20%20%20host%3A%20localhost%0A%20%20%20%20port%3A%205672%0A%20%20%20%20username%3A%20guest%0A%20%20%20%20password%3A%20guest%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always"
```

## 2. Auth Service Configuration
**Data ID:** `bytefinger-auth`  
**Group:** `bytefinger-audit-local`  
**Namespace:** `public`

```bash
curl -X POST "http://localhost:8848/nacos/v1/cs/configs" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "dataId=bytefinger-auth&group=bytefinger-audit-local&content=server%3A%0A%20%20port%3A%208300%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-auth%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always"
```

## 3. System Service Configuration
**Data ID:** `bytefinger-system`  
**Group:** `bytefinger-audit-local`  
**Namespace:** `public`

```bash
curl -X POST "http://localhost:8848/nacos/v1/cs/configs" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "dataId=bytefinger-system&group=bytefinger-audit-local&content=server%3A%0A%20%20port%3A%208400%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-system%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always"
```

## 4. Biz Service Configuration
**Data ID:** `bytefinger-biz`  
**Group:** `bytefinger-audit-local`  
**Namespace:** `public`

```bash
curl -X POST "http://localhost:8848/nacos/v1/cs/configs" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "dataId=bytefinger-biz&group=bytefinger-audit-local&content=server%3A%0A%20%20port%3A%208500%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-biz%0Auser%3A%0A%20%20deptName%3A%20%E8%BF%90%E8%90%A5%E7%AE%A1%E7%90%86%E9%83%A8%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always"
```

## 5. Job Service Configuration
**Data ID:** `bytefinger-job`  
**Group:** `bytefinger-audit-local`  
**Namespace:** `public`

```bash
curl -X POST "http://localhost:8848/nacos/v1/cs/configs" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "dataId=bytefinger-job&group=bytefinger-audit-local&content=server%3A%0A%20%20port%3A%208600%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-job%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always"
```

## 6. Report Service Configuration
**Data ID:** `bytefinger-report`  
**Group:** `bytefinger-audit-local`  
**Namespace:** `public`

```bash
curl -X POST "http://localhost:8848/nacos/v1/cs/configs" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "dataId=bytefinger-report&group=bytefinger-audit-local&content=server%3A%0A%20%20port%3A%208700%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-report%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always"
```

## 7. Gateway Service Configuration
**Data ID:** `bytefinger-gateway`  
**Group:** `bytefinger-audit-local`  
**Namespace:** `public`

```bash
curl -X POST "http://localhost:8848/nacos/v1/cs/configs" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "dataId=bytefinger-gateway&group=bytefinger-audit-local&content=server%3A%0A%20%20port%3A%208200%0Aspring%3A%0A%20%20application%3A%0A%20%20%20%20name%3A%20bytefinger-gateway%0A%20%20cloud%3A%0A%20%20%20%20gateway%3A%0A%20%20%20%20%20%20routes%3A%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-auth%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-auth%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/auth/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-system%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-system%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/system/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-biz%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-biz%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/biz/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-job%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-job%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/job/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0A%20%20%20%20%20%20%20%20-%20id%3A%20bytefinger-report%0A%20%20%20%20%20%20%20%20%20%20uri%3A%20lb%3A//bytefinger-report%0A%20%20%20%20%20%20%20%20%20%20predicates%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20Path%3D/report/**%0A%20%20%20%20%20%20%20%20%20%20filters%3A%0A%20%20%20%20%20%20%20%20%20%20%20%20-%20StripPrefix%3D1%0Amanagement%3A%0A%20%20endpoints%3A%0A%20%20%20%20web%3A%0A%20%20%20%20%20%20exposure%3A%0A%20%20%20%20%20%20%20%20include%3A%20%27*%27%0A%20%20endpoint%3A%0A%20%20%20%20health%3A%0A%20%20%20%20%20%20show-details%3A%20always"
```

## Service Restart Commands
```bash
# Restart all microservices
sudo systemctl restart auditsystem-auth auditsystem-system auditsystem-biz auditsystem-job auditsystem-report auditsystem-gateway

# Wait for services to start
sleep 30

# Check service health
curl -I localhost:8300/actuator/health  # auth service
curl -I localhost:8400/actuator/health  # system service  
curl -I localhost:8500/actuator/health  # biz service
curl -I localhost:8600/actuator/health  # job service
curl -I localhost:8700/actuator/health  # report service
curl -I localhost:8200/actuator/health  # gateway service
```

## Service Registration Check
```bash
# Check registered services in Nacos
curl "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=bytefinger-auth"
curl "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=bytefinger-system"
curl "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=bytefinger-biz"
curl "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=bytefinger-job"
curl "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=bytefinger-report"
curl "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=bytefinger-gateway"
```
