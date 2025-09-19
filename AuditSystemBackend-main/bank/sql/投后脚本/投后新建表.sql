/*
 Navicat Premium Data Transfer

 Source Server         : bsh-sit
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 10.0.1.180:3306
 Source Schema         : toutuo_biz_sit

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 16/04/2023 22:20:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_company_document_template
-- ----------------------------
CREATE TABLE `biz_project_company_document_template`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `step_menu_id` bigint(11) NULL DEFAULT NULL COMMENT '文件步骤id',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '节点类型',
  `oper_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作名字',
  `is_online` int(2) NULL DEFAULT 1 COMMENT '是否线上(0:线下,1:线上)',
  `sorts` int(11) NULL DEFAULT NULL COMMENT '排序',
  `required` int(11) NULL DEFAULT 1 COMMENT '是否必填 0-非必填 1-必填',
  `code` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单code',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `template_documents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '模板文件 JSON',
  `file_format` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件限制格式',
  `disabled` int(11) NULL DEFAULT 0 COMMENT '禁止上传 0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1072 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_project_company_document_template
-- ----------------------------
INSERT INTO `biz_project_company_document_template` VALUES (1038, 37, 'GONGSHAN_XINXI', '章程上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1039, 37, 'GONGSHAN_XINXI', '工商执照上传', 1, 2, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1040, 37, 'GONGSHAN_XINXI', '其它文件上传', 1, 3, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1041, 37, 'XINXI_BIANGEN', '信息变更凭证上传', 1, 4, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1042, 38, 'BASE', '文件上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1043, 40, 'BASE', '外派人员附件', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1044, 42, 'BASE', '附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1045, 43, 'SHENPI_PINZHEN', '审批凭证上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1046, 44, 'BAOGAO_SHANGCHUAN', '经营报告上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1047, 44, 'BAOGAO_SHANGCHUAN', '补充附件上传', 1, 2, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1048, 44, 'CAIWU_FENXI', '财务分析报告上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1049, 44, 'CHENGUO_FUJIAN', '成果附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1050, 45, 'CAIWU_BAOGAO', '财务报告上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1051, 45, 'DIAOYAN_BAOGAO', '调研报告上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1052, 45, 'JINYIN_BAOGAO', '经营报告上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1053, 45, 'BAOGAO_SHANGCHUAN', '评价报告上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1054, 45, 'BAOGAO_SHANGCHUAN', '补充附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1055, 46, 'CHU_LI', '附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1056, 46, 'JIE_CHU', '附件上传', 1, 2, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1057, 52, 'XIE_YI', '协议', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1058, 52, 'ZHANG_CHENG', '章程', 1, 2, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1059, 52, 'SAN_HUI_YI_AN', '\"三会\"议案', 1, 3, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1060, 52, 'TUI_JIAN_HAN', '推荐函', 1, 4, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1061, 52, 'QI_TA_FU_JIAN', '其他附件', 1, 5, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1062, 53, 'BASE', '协议上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1063, 53, 'BASE', '章程上传', 1, 2, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1064, 53, 'BASE', '\"三会\"议案上传', 1, 3, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1065, 53, 'BASE', '推荐函上传', 1, 4, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1066, 53, 'BASE', '其他附件上传', 1, 5, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1067, 50, 'BASE', '会议纪要上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1068, 57, 'BASE', '资产负债信息附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1069, 58, 'BASE', '利润信息附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1070, 59, 'BASE', '现金流信息附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_company_document_template` VALUES (1071, 43, 'CHULI_WENJIAN', '处理文件上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for biz_project_company_exit
-- ----------------------------
CREATE TABLE `biz_project_company_exit`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '子公司id',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司名称',
  `approval_content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '申请内容',
  `approval_status` int(2) NULL DEFAULT NULL COMMENT 'oa审批状态',
  `approval_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'oa审批url',
  `approval_user_id` bigint(21) NULL DEFAULT 0 COMMENT '发起人',
  `approval_send_time` datetime(0) NULL DEFAULT NULL COMMENT '发起时间',
  `exit_time` datetime(0) NULL DEFAULT NULL COMMENT '项目退出时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目退出' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_finance
-- ----------------------------
CREATE TABLE `biz_project_company_finance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '公司id',
  `audit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否审计',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '报告名称',
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告所属年份',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '报告开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '报告结束时间',
  `total_assets` decimal(10, 2) NULL DEFAULT NULL COMMENT '总资产',
  `net_assets` decimal(10, 2) NULL DEFAULT NULL COMMENT '净资产',
  `total_profit` decimal(10, 2) NULL DEFAULT NULL COMMENT '总利润',
  `net_profit` decimal(10, 2) NULL DEFAULT NULL COMMENT '净利润',
  `accumulated_operating` decimal(10, 2) NULL DEFAULT NULL COMMENT '营业收入本年累计',
  `begin_cash_flow` decimal(10, 2) NULL DEFAULT NULL COMMENT '期初现金流',
  `end_cash_flow` decimal(10, 2) NULL DEFAULT NULL COMMENT '期末现金流',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 361 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '财务信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_finance_assets
-- ----------------------------
CREATE TABLE `biz_project_company_finance_assets`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '公司id',
  `finance_id` bigint(21) NULL DEFAULT NULL COMMENT '财务数据id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '报告名称',
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告所属年份',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '报告开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '报告结束时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 152 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '财务资产负债信息' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for biz_project_company_finance_cash_flow
-- ----------------------------
CREATE TABLE `biz_project_company_finance_cash_flow`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '公司id',
  `finance_id` bigint(21) NULL DEFAULT NULL COMMENT '财务数据id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '报告名称',
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告所属年份',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '报告开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '报告结束时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '财务现金流信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_finance_profit
-- ----------------------------
CREATE TABLE `biz_project_company_finance_profit`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '公司id',
  `finance_id` bigint(21) NULL DEFAULT NULL COMMENT '财务数据id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '报告名称',
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告所属年份',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '报告开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '报告结束时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 145 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '财务利润信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_high_level_meeting
-- ----------------------------
CREATE TABLE `biz_project_company_high_level_meeting`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '子公司id',
  `take_id` bigint(21) NULL DEFAULT 0 COMMENT '记录人',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '标题',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会议类型',
  `document_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文档类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '三会记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_high_level_meeting_approval
-- ----------------------------
CREATE TABLE `biz_project_company_high_level_meeting_approval`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '子公司id',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司名称',
  `approval_user_id` bigint(21) NULL DEFAULT 0 COMMENT '审批发起人',
  `approval_send_time` datetime(0) NULL DEFAULT NULL COMMENT '审批发起时间',
  `approval_status` int(2) NULL DEFAULT NULL COMMENT '审批状态',
  `approval_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审批url',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '标题',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会议类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '三会记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_operate_report
-- ----------------------------
CREATE TABLE `biz_project_company_operate_report`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '公司id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '报告名称',
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告所属年份',
  `cycle_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告周期类型',
  `cycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告周期',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '报告说明',
  `report_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告方式',
  `ownership_structure` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '股权结构',
  `organization` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '组织架构',
  `core_team` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '核心团队情况',
  `office_worker` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '职员情况及激励政策',
  `basic_other` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公司其它情况',
  `industry_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '行业基本情况及变化',
  `operate_analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '经营情况及分析',
  `external_situation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '供应商及客户等外部情况',
  `financial_situation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '财务整体分析描述',
  `tax_situation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '税务情况',
  `transaction_situation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '整体交易情况',
  `accounting_firm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会计师事务所',
  `law_firm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '律师事务所',
  `advisory_body` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询机构',
  `supplier_other` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '供应商模块-其它',
  `performance` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公司履约情况',
  `investment_after` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '投后服务',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '经营报告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_operate_report_executives
-- ----------------------------
CREATE TABLE `biz_project_company_operate_report_executives`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(21) NULL DEFAULT NULL COMMENT '报告id',
  `company_id` bigint(21) NULL DEFAULT NULL COMMENT '公司id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '高管姓名',
  `term_begin_time` datetime(0) NULL DEFAULT NULL COMMENT '任期开始时间',
  `term_end_time` datetime(0) NULL DEFAULT NULL COMMENT '任期结束时间',
  `term_status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任职状态',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职位',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2848 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '经营报告-公司高管信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_operate_report_item
-- ----------------------------
CREATE TABLE `biz_project_company_operate_report_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `report_id` bigint(21) NULL DEFAULT NULL COMMENT '经营报表id',
  `company_id` bigint(21) NULL DEFAULT NULL COMMENT '公司id',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事项类型',
  `take_id` bigint(21) NULL DEFAULT 0 COMMENT '记录人',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '事项明细',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '事项描述',
  `process_propose` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '处理建议',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '经营报表-重大事项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_payment
-- ----------------------------
CREATE TABLE `biz_project_company_payment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NULL DEFAULT NULL COMMENT '子公司id',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '付款类型',
  `apply` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '出资申请人',
  `apply_time` datetime(0) NULL DEFAULT NULL COMMENT '出资申请时间',
  `amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '出资金额',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '出资描述',
  `process_status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '处理状态',
  `process_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '处理描述',
  `process_time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  `process_user_id` bigint(21) NULL DEFAULT NULL COMMENT '处理人',
  `take_id` bigint(21) NULL DEFAULT NULL COMMENT '记录人',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '企业联系人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_risk_inspection
-- ----------------------------
CREATE TABLE `biz_project_company_risk_inspection`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(21) NULL DEFAULT NULL COMMENT '子公司id',
  `node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '检查节点',
  `milepost_time` datetime(0) NULL DEFAULT NULL COMMENT '里程碑时间',
  `reminder_time` datetime(0) NULL DEFAULT NULL COMMENT '提醒时间',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '提醒内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2870 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '子公司-风险检查' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_risk_inspection_approval
-- ----------------------------
CREATE TABLE `biz_project_company_risk_inspection_approval`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '子公司id',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司名称',
  `process_approval_user_id` bigint(21) NULL DEFAULT 0 COMMENT '风险处理审批发起人',
  `process_approval_send_time` datetime(0) NULL DEFAULT NULL COMMENT '风险处理审批发起时间',
  `process_approval_status` int(2) NULL DEFAULT NULL COMMENT '风险处理审批状态',
  `process_approval_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '风险处理审批url',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '风险名称',
  `presenter` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '风险提出人',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '风险类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容描述',
  `scheme` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '风险解除方案',
  `scheme_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '风险解除描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  `relieve_approval_user_id` bigint(21) NULL DEFAULT NULL COMMENT '风险解除审批发起人',
  `relieve_approval_send_time` datetime(0) NULL DEFAULT NULL COMMENT '风险解除审批发起时间',
  `relieve_approval_status` int(2) NULL DEFAULT NULL COMMENT '风险解除审批状态',
  `relieve_approval_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '风险解除审批url',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '风险处理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_shareholder_log
-- ----------------------------
CREATE TABLE `biz_project_company_shareholder_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(21) NULL DEFAULT NULL COMMENT '股权合作公司id',
  `change_user_id` bigint(21) NULL DEFAULT NULL COMMENT '变更人id',
  `change_time` datetime(0) NULL DEFAULT NULL COMMENT '变更日期',
  `change_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '变更类型',
  `change_before` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '变更前',
  `change_after` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '变更后',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2852 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '股权信息变更记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_project_company_step_menu
-- ----------------------------
CREATE TABLE `biz_project_company_step_menu`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(11) NULL DEFAULT NULL COMMENT '上级节点',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单编码',
  `level` int(2) NULL DEFAULT NULL COMMENT '层级',
  `sorts` int(2) NULL DEFAULT NULL COMMENT '排序',
  `is_document` int(2) NULL DEFAULT 0 COMMENT '是否有文件(0:否,1:是)',
  `oa_approval` int(2) NULL DEFAULT NULL COMMENT '是否OA审批(0:否,1:是)',
  `offline_approval` int(2) NULL DEFAULT NULL COMMENT '是否线下审批(0:否,1:是)',
  `is_show` int(1) NULL DEFAULT 1 COMMENT '显示',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '子公司菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_project_company_step_menu
-- ----------------------------
INSERT INTO `biz_project_company_step_menu` VALUES (37, 0, '企业信息', 'qyxx', 1, 1, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (38, 0, '股权信息', 'gqxx', 1, 2, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (39, 0, '关联企业', 'glqy', 1, 3, 0, NULL, NULL, 0, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (40, 0, '人员信息', 'ryxx', 1, 4, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (41, 0, '财务信息', 'cwxx', 1, 5, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (42, 0, '日常协调', 'rcxt', 1, 6, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (43, 0, '项目付款', 'xmfk', 1, 7, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (44, 0, '经营报告', 'jybg', 1, 8, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (45, 0, '评价报告', 'pjbg', 1, 9, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (46, 0, '风险提示', 'fxts', 1, 10, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (47, 0, '公司治理', 'gszl', 1, 11, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (48, 0, '文档归集', 'wdgj', 1, 12, 0, NULL, NULL, 0, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (49, 0, '变更记录', 'bgjl', 1, 13, 0, NULL, NULL, 0, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (50, 0, '项目退出', 'xmtc', 1, 14, 0, NULL, NULL, 1, '2022-10-20 11:30:19');
INSERT INTO `biz_project_company_step_menu` VALUES (52, 47, '三会文档记录', 'shwdjl', 2, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `biz_project_company_step_menu` VALUES (53, 47, '三会审批记录', 'shspjl', 2, 2, 0, NULL, NULL, 1, NULL);
INSERT INTO `biz_project_company_step_menu` VALUES (57, 41, '资产负债信息', 'zcfzxx', 2, 1, 0, NULL, NULL, 1, NULL);
INSERT INTO `biz_project_company_step_menu` VALUES (58, 41, '利润信息', 'lrxx', 2, 2, 0, NULL, NULL, 1, NULL);
INSERT INTO `biz_project_company_step_menu` VALUES (59, 41, '现金流信息', 'xjlxx', 2, 3, 0, NULL, NULL, 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
