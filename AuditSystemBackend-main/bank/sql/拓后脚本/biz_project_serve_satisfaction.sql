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

 Date: 16/04/2023 19:53:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_serve_satisfaction
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_serve_satisfaction`;
CREATE TABLE `biz_project_serve_satisfaction`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(21) NULL DEFAULT NULL COMMENT '项目id',
  `satisfaction_type` int(2) NULL DEFAULT NULL COMMENT '满意度类型(1.合同相对方满意度,2.业主服务满意度)',
  `satisfaction_explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '满意度说明',
  `year` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年份',
  `deleted` int(2) NULL DEFAULT NULL COMMENT '是否删除 0-否 2-是',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后运营-服务满意度信息' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
