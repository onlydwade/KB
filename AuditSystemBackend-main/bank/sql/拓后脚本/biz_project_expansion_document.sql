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

 Date: 16/04/2023 19:51:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_expansion_document
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_expansion_document`;
CREATE TABLE `biz_project_expansion_document`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `document_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件名',
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年份',
  `month` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '月份',
  `document_ext` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件后缀',
  `document_template_id` bigint(21) NULL DEFAULT NULL COMMENT '文件模板id',
  `project_id` bigint(11) NULL DEFAULT NULL COMMENT '项目id',
  `step_menu_id` bigint(11) NULL DEFAULT NULL COMMENT '节点菜单id',
  `record_id` bigint(11) NULL DEFAULT NULL COMMENT '数据id',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '是否删除 0-否 2-是',
  `docment_object` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '文件对象 json 内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '上传人',
  `meeting_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会议标题',
  `meeting_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会议类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3532 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后项目文件' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
