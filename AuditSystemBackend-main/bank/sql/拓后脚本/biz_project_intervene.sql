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

 Date: 16/04/2023 19:52:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_intervene
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_intervene`;
CREATE TABLE `biz_project_intervene`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(21) NULL DEFAULT NULL COMMENT '项目id',
  `assess_id` bigint(21) NULL DEFAULT NULL COMMENT '评估id',
  `intervene_scheme_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '干预方案名称',
  `intervene_deadline` datetime(0) NULL DEFAULT NULL COMMENT '干预实施期限',
  `scheme_isnot_embodiment` int(2) NULL DEFAULT NULL COMMENT '方案是否被实施（0否，2是)',
  `scheme_user_id` bigint(21) NULL DEFAULT NULL COMMENT '方案实施人',
  `intervene_state` int(2) NULL DEFAULT NULL COMMENT '干预执行状态',
  `examine_level` int(2) NULL DEFAULT NULL COMMENT '(0未检查，1一级检查，2二级检查)',
  `feedback_time` datetime(0) NULL DEFAULT NULL COMMENT '实施反馈日期',
  `close_time` datetime(0) NULL DEFAULT NULL COMMENT '关闭日期',
  `execute_not_opinion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '执行不通过意见',
  `execute_condition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '执行情况',
  `manage_intervene` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '经营干预',
  `risk_intervene` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '风险干预',
  `customer_service_intervene` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '对客服务干预',
  `quality_intervene` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '品质干预',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '干预下达时间（创建时间）',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后运营项目干预' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
