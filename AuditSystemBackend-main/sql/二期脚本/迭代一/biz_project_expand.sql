
CREATE TABLE `biz_project_expand`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) NULL DEFAULT NULL,
  `operation_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '运营模式',
  `outsourcing_unit_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '外包单位名称',
  `revenue_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营收信息',
  `annual_avg_profit_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '年平均利润率',
  `projected_profit_margin` decimal(20, 4) NULL DEFAULT NULL COMMENT '测算利润率',
  `is_branch_office` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否成立分公司',
  `project_management_unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目代管单位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目扩展表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;