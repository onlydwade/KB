/*
 Navicat Premium Data Transfer

 Source Server         : toutuo-sit
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 10.0.1.180:3306
 Source Schema         : toutuo_biz_sit

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 18/04/2023 22:21:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


CREATE TABLE `biz_project_company_appraise_report`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` bigint(21) NOT NULL COMMENT '公司id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '报告名称',
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告所属年份',
  `cycle_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告周期类型',
  `cycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告周期',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '报告说明',
  `report_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报告方式',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评价报告' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
