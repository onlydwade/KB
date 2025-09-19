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

 Date: 16/04/2023 19:51:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_expansion_document_template
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_expansion_document_template`;
CREATE TABLE `biz_project_expansion_document_template`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `step_menu_id` bigint(11) NULL DEFAULT NULL COMMENT '文件步骤id',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '节点类型',
  `oper_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作名字',
  `is_online` int(2) NULL DEFAULT 1 COMMENT '是否线上(0:线下,1:线上)',
  `sorts` int(11) NULL DEFAULT NULL COMMENT '排序',
  `required` int(11) NULL DEFAULT 1 COMMENT '是否必填 0-非必填 1-必填',
  `code` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单code',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `template_documents` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '模板文件 JSON',
  `file_format` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件限制格式',
  `disabled` int(11) NULL DEFAULT 0 COMMENT '禁止上传 0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后文件模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_project_expansion_document_template
-- ----------------------------
INSERT INTO `biz_project_expansion_document_template` VALUES (1, 44, 'BASE', '附件', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (2, 45, 'BASE', '相关附件', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (3, 48, 'ZHENGGAI_QINGDAN', '查验整改清单', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (4, 48, 'JIAOJIE_GUANLI', '承接查验协议', 1, 2, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (5, 48, 'JIAOJIE_GUANLI', '承接查验移交手续', 1, 3, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (6, 48, 'JIAOJIE_GUANLI', '风险切割附件', 1, 4, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (7, 48, 'ZILIAO_GUANLI', '承接查验方案', 1, 5, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (8, 48, 'ZILIAO_GUANLI', '移交图纸资料', 1, 6, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (9, 48, 'ZILIAO_GUANLI', '现场影像资料', 1, 7, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (10, 49, 'JINYIN_PINGU', '经营评估表上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (11, 49, 'QITA', '其他附件上传', 1, 2, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (12, 50, 'ZHIXING_QINGKUANG', '执行情况上传', 1, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (13, 50, 'QITA', '其他附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `biz_project_expansion_document_template` VALUES (14, 51, 'BASE', '附件上传', 1, 1, 0, NULL, NULL, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
