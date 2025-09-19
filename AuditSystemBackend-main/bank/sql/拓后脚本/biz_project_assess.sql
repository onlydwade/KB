/*
 Navicat Premium Data Transfer

 Source Server         : toutuo_客户sit
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 10.0.1.180:3306
 Source Schema         : toutuo_biz_sit

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 16/04/2023 19:49:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_assess
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_assess`;
CREATE TABLE `biz_project_assess`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(21) NULL DEFAULT NULL COMMENT '项目id',
  `assess_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '评估日期',
  `assess_deadline` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '经营评估期限',
  `assess_user_id` int(11) NULL DEFAULT NULL COMMENT '评估人id',
  `assess_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评估人名称',
  `assess_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评估标题',
  `assess_stage` int(255) NULL DEFAULT NULL COMMENT '评估阶段(1.月份2.季度,3.半年,4.年度)',
  `assess_stage_details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评估阶段详情',
  `assess_year` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评估年份',
  `assess_state` int(10) NULL DEFAULT NULL COMMENT '评估状态(0草稿,1.已完成)',
  `transmit_state` int(10) NULL DEFAULT NULL COMMENT '是否需要下达(0.否,1.是)',
  `transmit_already_state` int(10) NULL DEFAULT NULL COMMENT '是否已下达干预方案(0.否,1.是)',
  `transmit_deadline` datetime(0) NULL DEFAULT NULL COMMENT '方案下达期限',
  `risk_describe_law` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '法律风险描述',
  `risk_describe_hidden` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '隐患风险描述',
  `risk_describe_contract` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同风险描述',
  `improve_suggest_law` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '法律改进建议',
  `improve_suggest_hidden` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '隐患改进建议',
  `improve_suggest_contract` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '合同改进建议',
  `deleted` int(2) NULL DEFAULT NULL COMMENT '是否删除(0否,2是)',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `assess_file_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '经营附件id',
  `rest_file_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '其他附件id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 176 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '项目评估' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
