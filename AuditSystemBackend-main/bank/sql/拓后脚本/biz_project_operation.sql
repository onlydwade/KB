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

 Date: 16/04/2023 19:52:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_operation
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_operation`;
CREATE TABLE `biz_project_operation`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(21) NULL DEFAULT NULL COMMENT '项目id',
  `househoId_sum` int(11) NULL DEFAULT NULL COMMENT '总户数',
  `floor_space` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '占地面积',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后运营管理' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
