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

 Date: 16/04/2023 19:53:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_step_expansion
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_step_expansion`;
CREATE TABLE `biz_project_step_expansion`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) NOT NULL COMMENT '项目id',
  `step_menu_id` bigint(11) NOT NULL COMMENT '步骤菜单id',
  `status` int(1) NULL DEFAULT 0 COMMENT '状态 0-未完成 1-已完成',
  `approval_status` int(2) NULL DEFAULT NULL COMMENT '审批状态（0  未发起审批\n1  审批中 \n2  审批通过\n3 审批驳回\n8 线下审批通过  \n9无需审批 ）',
  PRIMARY KEY (`id`, `project_id`, `step_menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3354 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后项目步骤完成状态' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
