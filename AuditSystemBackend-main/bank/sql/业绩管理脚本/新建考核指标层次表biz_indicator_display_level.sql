CREATE TABLE `biz_indicator_display_level` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '指标的标识',
  `display_level_id` bigint NOT NULL COMMENT '展示层级ID',
  `create_user_id` bigint NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=336 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='指标展示层级';