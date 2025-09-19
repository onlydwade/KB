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

 Date: 16/04/2023 19:51:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_extension_exit
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_extension_exit`;
CREATE TABLE `biz_project_extension_exit`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_id` bigint(21) NOT NULL COMMENT '项目id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主题',
  `approval_status` int(2) NULL DEFAULT NULL COMMENT 'OA 审批状态 1-发起审批 2-审批通过 3-审批驳货',
  `approval_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'oa审批url',
  `approval_user_id` bigint(21) NULL DEFAULT 0 COMMENT '申请人',
  `approval_user_name` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '申请人名称',
  `approval_affiliation` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '申请人归属',
  `approval_send_time` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '申请时间',
  `approval_sponsor_time` datetime(0) NULL DEFAULT NULL,
  `relation_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `whether_involve_money` int(11) NULL DEFAULT NULL COMMENT '是否涉及金额',
  `involve_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '涉及金额',
  `majuscule_money` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '大写金额',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '退场描述',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后项目退场' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
