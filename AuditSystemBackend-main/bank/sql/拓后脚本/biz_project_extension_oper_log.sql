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

 Date: 16/04/2023 19:52:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_extension_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_extension_oper_log`;
CREATE TABLE `biz_project_extension_oper_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '更新人id',
  `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `module_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表名表示',
  `project_id` bigint(11) NULL DEFAULT NULL COMMENT '项目id',
  `record_id` bigint(20) NULL DEFAULT NULL COMMENT '更新数据的id',
  `update_log` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '更新日志',
  `oper_ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作ip',
  `update_dept_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人部门',
  `update_post_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人岗位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1725414 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后业务数据变更记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
