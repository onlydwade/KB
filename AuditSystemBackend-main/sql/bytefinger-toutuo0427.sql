#规则消息提醒
ALTER TABLE biz_rules_message_log ADD association_table VARCHAR(255) DEFAULT NULL COMMENT '关联的表';
ALTER TABLE biz_rules_message_log ADD association_business_id bigint(20) DEFAULT NULL COMMENT '关联表的主键ID';