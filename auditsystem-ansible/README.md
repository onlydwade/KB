auditsystem-ansible/
├── inventory.ini                # ✅ 主機清單：在這裡指定您要部署的伺服器 IP。
├── site.yml                     # ✅ 主要劇本：執行此檔案可從頭到尾完整部署。
│
├── playbook-01-install-setup.yml  # 對應 install-ubuntu-setup.sh
├── playbook-02-check-java.yml     # 對應 check-java-compatibility.sh
├── playbook-03-setup-nacos.yml    # 對應 setup-nacos.sh
├── playbook-04-deploy-backend.yml # 對應 deploy-backend.sh
├── playbook-05-deploy-frontend.yml# 對應 deploy-frontend.sh
├── playbook-06-configure-nginx.yml# 對應 configure-nginx.sh
├── manage-services.yml            # 對應 manage-services.sh
│
├── group_vars/
│   └── all.yml                    # 全域變數，如資料庫密碼、使用者名稱等。
│
└── roles/                         # 所有實際執行邏輯的「角色」
    ├── common/
    │   └── tasks/
    │       └── main.yml           # 基礎系統設定 (更新、安裝 curl/git 等)
    ├── java/
    │   └── tasks/
    │       └── main.yml           # 安裝 Java 11
    ├── mysql/
    │   └── tasks/
    │       └── main.yml           # 安裝與設定 MySQL
    ├── redis/
    │   └── tasks/
    │       └── main.yml           # 安裝與設定 Redis
    ├── rabbitmq/
    │   └── tasks/
    │       └── main.yml           # 安裝與設定 RabbitMQ
    ├── nodejs/
    │   └── tasks/
    │       └── main.yml           # 安裝 Node.js 與 pnpm
    ├── nginx/
    │   ├── tasks/
    │   │   └── main.yml           # 安裝 Nginx 服務
    │   └── templates/
    │       └── nginx-site.conf.j2 # Nginx 站點設定模板
    ├── nacos_setup/
    │   ├── tasks/
    │   │   └── main.yml           # 執行 Nacos 安裝、資料庫初始化、發布設定等
    │   └── templates/
    │       └── nacos-config/      # ✅ Nacos 設定檔模板存放處
    │           ├── services/
    │           │   ├── bytefinger-auth.yml.j2
    │           │   ├── bytefinger-biz.yml.j2
    │           │   ├── bytefinger-gateway.yml.j2
    │           │   ├── bytefinger-job.yml.j2
    │           │   ├── bytefinger-report.yml.j2
    │           │   └── bytefinger-system.yml.j2
    │           └── shared/
    │               └── application-local.yml.j2
    ├── backend_deploy/
    │   ├── tasks/
    │   │   └── main.yml           # 部署後端服務 (git, maven, copy, systemd)
    │   └── templates/
    │       └── service.unit.j2    # 後端微服務的 systemd 模板
    └── frontend_deploy/
        └── tasks/
            └── main.yml           # 部署前端應用 (git, npm, sync)
