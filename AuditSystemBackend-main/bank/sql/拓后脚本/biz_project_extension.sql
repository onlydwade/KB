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

 Date: 16/04/2023 19:51:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_extension
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_extension`;
CREATE TABLE `biz_project_extension`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(21) NOT NULL COMMENT '项目id',
  `relevance_project_id` bigint(21) NULL DEFAULT NULL COMMENT '关联新建项目',
  `process_state` int(2) NULL DEFAULT NULL COMMENT '处理状态(0未处理,1审批中,2已处理，3:处理驳回)',
  `process_mode` int(2) NULL DEFAULT NULL COMMENT '处理方式(0未续签,1续签,2重新投标,3退场)',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后管理续签状态' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
