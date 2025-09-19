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

 Date: 16/04/2023 19:50:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_check
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_check`;
CREATE TABLE `biz_project_check`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(21) NOT NULL COMMENT '项目id',
  `check_state` int(11) NULL DEFAULT NULL COMMENT '查验状态(0未完成,1进行中,2已完成)',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `person_charge_id` int(11) NULL DEFAULT NULL COMMENT '责任id',
  `person_charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '责任名称',
  `problem_num` int(11) NULL DEFAULT NULL COMMENT '发现问题数',
  `rectify_finished_num` int(11) NULL DEFAULT NULL COMMENT '已整改数量',
  `rectify_await_num` int(11) NULL DEFAULT NULL COMMENT '待整改数量',
  `rectify_deadline` datetime(0) NULL DEFAULT NULL COMMENT '整改期限',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '承接查验' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
