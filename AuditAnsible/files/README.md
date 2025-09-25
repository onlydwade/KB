# 關於 `files` 目錄

這個目錄用於存放需要被 Ansible 複製到遠端主機的靜態文件。

## 操作指南

請將您本地 `AuditSystemEnvironment-main/AuditSystemVM` **整個目錄**的內容完整地複製到此處。Ansible Playbook 將會自動將 `AuditSystemVM` 目錄同步到目標伺服器上進行部署。

最終的目錄結構應如下所示：

AuditAnsible/
├── files/
│   └── AuditSystemVM/
│       ├── README.md
│       ├── install-ubuntu-setup.sh
│       ├── setup-nacos.sh
│       ├── ... (所有其他腳本和子目錄)
│
└── ... (其他 Ansible 文件)