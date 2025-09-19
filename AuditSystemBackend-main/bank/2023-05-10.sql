
		drop table IF EXISTS biz_project_team_outside

		CREATE TABLE `biz_project_team_outside` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `record_id` bigint(21) DEFAULT NULL COMMENT '项目id',
  `user_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `post_name` varchar(100) DEFAULT NULL COMMENT '职务',
	 `contact` varchar(100) DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=427 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='项目团队信息(外部人员)';