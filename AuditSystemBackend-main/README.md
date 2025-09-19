#### 系统模块 

~~~
com.bytefinger
├── bytefinger-gateway         // 网关模块 [8200]
├── bytefinger-auth            // 认证中心 [8201]
├── bytefinger-api             // 内部模块接口模块
├── bytefinger-common          // 通用模块
│       └── bytefinger-common-core                         // 核心模块
│       └── bytefinger-common-datascope                    // 权限范围
│       └── bytefinger-common-datasource                   // 多数据源
│       └── bytefinger-common-log                          // 日志记录
│       └── bytefinger-common-redis                        // 缓存服务
│       └── bytefinger-common-security                     // 安全模块
│       └── bytefinger-common-swagger                      // 系统接口
├── bytefinger-modules         // 业务模块
│       └── bytefinger-system                              // 配置管理模块 [8202]
│       └── bytefinger-biz                                 // 业务模块 [8203]
│       └── bytefinger-report                              // 报表模块 [8205]
│       └── bytefinger-job                                 // 定时任务
├──pom.xml                // 公共依赖
~~~



