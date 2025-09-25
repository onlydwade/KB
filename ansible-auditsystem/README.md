ansible-auditsystem/
├── ansible.cfg
├── inventory.ini
├── deploy_auditsystem.yml
├── group_vars/
│   └── all/
│       └── main.yml
└── roles/
    └── deploy_auditsystem/
        ├── tasks/
        │   └── main.yml
        └── files/
            └── AuditSystemVM/
                ├── install-ubuntu-setup.sh
                ├── check-java-compatibility.sh
                ├── setup-nacos.sh
                ├── deploy-backend.sh
                ├── deploy-frontend.sh
                ├── configure-nginx.sh
                └── manage-services.sh
                # ... 以及其他所有相關腳本





如何使用
1. 準備檔案：將您所有的 .sh 腳本和相關檔案放入 ansible-auditsystem/roles/deploy_auditsystem/files/AuditSystemVM/ 目錄。
2. 配置變數：
    修改 inventory.ini，填寫正確的伺服器 IP 和使用者名稱。
    修改 group_vars/all/main.yml，設定正確的 Git URL 和安全的密碼。
3. 執行完整部署

# 執行完整部署
ansible-playbook deploy_auditsystem.yml


4. 選擇性執行：您可以使用 tags 來執行特定階段的任務：

# 只執行系統安裝與環境檢查
ansible-playbook deploy_auditsystem.yml --tags "setup"

# 只執行應用程式部署 (Nacos, 後端, 前端等)
ansible-playbook deploy_auditsystem.yml --tags "deploy"

# 只管理服務 (例如重啟所有服務)
ansible-playbook deploy_auditsystem.yml --tags "manage" -e "service_action=restart"