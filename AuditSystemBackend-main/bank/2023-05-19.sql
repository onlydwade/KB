
#新增OA审批备份表，用于回退节点时保存旧OA审批数据
CREATE TABLE `biz_oa_approval_backup` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `approval_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '流程id',
                                          `approval_status` int DEFAULT NULL COMMENT 'OA 审批状态 1-发起审批 2-审批通过 3-审批驳货',
                                          `submit_user_id` bigint DEFAULT NULL COMMENT '申请人id',
                                          `submit_user_city_dept_id` bigint DEFAULT NULL COMMENT '申请人城市公司id',
                                          `submit_dept_id` bigint DEFAULT NULL COMMENT '申请人部门id',
                                          `submit_user_post_id` bigint DEFAULT NULL COMMENT '申请人职务',
                                          `submit_time` datetime DEFAULT NULL COMMENT '审批提交时间',
                                          `module_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模块标识',
                                          `sub_module_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子模块标识',
                                          `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审批主题',
                                          `detail_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '待审批内容链接',
                                          `main_process` tinyint(1) DEFAULT NULL COMMENT '是否主审批',
                                          `record_id` bigint DEFAULT NULL COMMENT '数据id',
                                          `sub_record_id` bigint DEFAULT NULL COMMENT '子数据id',
                                          `sub_record_action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子数据审批动作',
                                          `oa_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OA审批的人信息',
                                          `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OA审批提交的内容',
                                          `approval_result` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OA审批结果内容',
                                          `approval_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '审批数据',
                                          `create_time` datetime DEFAULT NULL COMMENT '发起时间',
                                          `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
                                          `approval_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OA审批记录url',
                                          `template_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'OA模板id',
                                          `oa_status` bigint DEFAULT '0' COMMENT 'oa对接审批状态',
                                          `expire` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否失效',
                                          `step_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '跳转编码',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1251 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='发起OA审批备份表';

#新增OA记录备份表，用于回退节点时保存旧OA审批记录数据
CREATE TABLE `biz_oa_approval_log_backup` (
                                              `id` bigint NOT NULL AUTO_INCREMENT,
                                              `approval_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审批编号',
                                              `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '审批日志内容',
                                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                              `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=135307 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='发起OA审批备份表';



#修改nc财务数据单元格动态对应字段表
update biz_project_company_finance_excel_field_match set excel_title='D8' where id = 10707

#修改财务数据表字段长度
ALTER TABLE biz_project_company_finance MODIFY total_assets decimal(20,2) COMMENT '总资产';
ALTER TABLE biz_project_company_finance MODIFY net_assets decimal(20,2) COMMENT '净资产';
ALTER TABLE biz_project_company_finance MODIFY total_profit decimal(20,2) COMMENT '总利润';
ALTER TABLE biz_project_company_finance MODIFY net_profit decimal(20,2) COMMENT '净利润';
ALTER TABLE biz_project_company_finance MODIFY accumulated_operating decimal(20,2) COMMENT '营业收入本年累计';
ALTER TABLE biz_project_company_finance MODIFY begin_cash_flow decimal(20,2) COMMENT '期初现金流';
ALTER TABLE biz_project_company_finance MODIFY end_cash_flow decimal(20,2) COMMENT '期末现金流';