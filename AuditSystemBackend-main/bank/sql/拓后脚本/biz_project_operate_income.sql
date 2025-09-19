
CREATE TABLE `biz_project_operate_income`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(21) NOT NULL COMMENT '项目id',
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业态',
  `quarter_stage` int(11) NULL DEFAULT NULL COMMENT '季度阶段(1.第一季2.第二季,3.第三季,4.第四季)',
  `operate_year` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年份',
  `takeover_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '收缴率%',
  `close_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '结算率%',
  `business_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '主营收入(元)',
  `outer_business_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '主营外收入(元)',
  `practical_close_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际结算金额（元）',
  `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '成本(元)',
  `profit` decimal(10, 2) NULL DEFAULT NULL COMMENT '利润(元)',
  `deleted` int(2) NULL DEFAULT NULL COMMENT '是否删除(0否,2是)',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `rest_file_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '其他附件id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '经营管理-项目收支' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
