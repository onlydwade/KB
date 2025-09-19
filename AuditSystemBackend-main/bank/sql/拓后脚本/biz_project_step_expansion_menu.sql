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

 Date: 16/04/2023 19:53:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_project_step_expansion_menu
-- ----------------------------
DROP TABLE IF EXISTS `biz_project_step_expansion_menu`;
CREATE TABLE `biz_project_step_expansion_menu`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(11) NULL DEFAULT NULL COMMENT '上级节点',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单编码',
  `level` int(2) NULL DEFAULT NULL COMMENT '层级',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `sorts` int(2) NULL DEFAULT NULL COMMENT '排序',
  `is_document` int(2) NULL DEFAULT 0 COMMENT '是否有文件(0:否,1:是)',
  `oa_approval` int(2) NULL DEFAULT NULL COMMENT '是否OA审批(0:否,1:是)',
  `offline_approval` int(2) NULL DEFAULT NULL COMMENT '是否线下审批(0:否,1:是)',
  `show` int(1) NULL DEFAULT 1 COMMENT '显示',
  `service_status` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '变更项目目标状态',
  `notify_user_id` bigint(21) NULL DEFAULT NULL COMMENT '特殊通知用户的id',
  `notify_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '通知文案',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '拓后项目步骤菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_project_step_expansion_menu
-- ----------------------------
INSERT INTO `biz_project_step_expansion_menu` VALUES (37, 0, '项目运营', 'thyy', 1, NULL, 1, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (38, 0, '承接查验', 'thcjcy', 1, NULL, 2, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (39, 0, '项目评估', 'thxmpg', 1, NULL, 3, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (40, 0, '项目干预', 'thxmgy', 1, NULL, 4, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (41, 0, '退场管理', 'thtcgl', 1, NULL, 5, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (42, 37, '项目信息', 'thxmxx', 2, NULL, 1, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (43, 37, '运营管理', 'thyygl', 2, NULL, 2, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (44, 37, '经营管理', 'thjygl', 2, NULL, 3, 1, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (45, 37, '风险管理', 'thfxgl', 2, NULL, 4, 1, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (46, 37, '项目团队', 'thxmtd', 2, NULL, 5, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (47, 37, '变更记录', 'thbgjl', 2, NULL, 6, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (48, 38, '承接查验', 'thcy', 2, NULL, 1, 1, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (49, 39, '项目评估', 'thpg', 2, NULL, 1, 1, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (50, 40, '项目干预', 'thgy', 2, NULL, 1, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_expansion_menu` VALUES (51, 41, '退场管理', 'thtc', 2, NULL, 1, 1, 1, 0, 1, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
