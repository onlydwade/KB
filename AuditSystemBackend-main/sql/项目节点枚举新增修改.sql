



INSERT INTO `biz_project_step_menu` (`id`, `parent_id`, `name`, `code`, `level`, `create_time`, `sorts`, `is_document`, `oa_approval`, `offline_approval`, `show`, `service_status`, `notify_user_id`, `notify_content`) VALUES (37, 38, '标书评审', 'bsps', 2, NULL, 5, 1, 1, 1, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_menu` (`id`, `parent_id`, `name`, `code`, `level`, `create_time`, `sorts`, `is_document`, `oa_approval`, `offline_approval`, `show`, `service_status`, `notify_user_id`, `notify_content`) VALUES (38, 0, '标书评审', 'bsps', 1, NULL, 5, 0, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_menu` (`id`, `parent_id`, `name`, `code`, `level`, `create_time`, `sorts`, `is_document`, `oa_approval`, `offline_approval`, `show`, `service_status`, `notify_user_id`, `notify_content`) VALUES (39, 0, '结项', 'xmjx', 1, NULL, 11, 1, 1, 1, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_menu` (`id`, `parent_id`, `name`, `code`, `level`, `create_time`, `sorts`, `is_document`, `oa_approval`, `offline_approval`, `show`, `service_status`, `notify_user_id`, `notify_content`) VALUES (40, 39, '结项评审', 'xmjxps', 2, NULL, 1, 1, 1, 1, 1, '{\"DAN_YI_FEI_TOU_BIAO_XIANG_MU\":\"ZAI_GUAN\",\"DAN_YI_TOU_BIAO_XIANG_MU\":\"ZAI_GUAN\",\"GU_QUAN_HE_ZUO_XIANG_MU\":\"ZAI_GUAN\"}', NULL, NULL);
INSERT INTO `biz_project_step_menu` (`id`, `parent_id`, `name`, `code`, `level`, `create_time`, `sorts`, `is_document`, `oa_approval`, `offline_approval`, `show`, `service_status`, `notify_user_id`, `notify_content`) VALUES (41, 7, '运营团队组建', 'yytdzj', 2, NULL, 1, 0, 0, 0, 1, NULL, NULL, NULL);
INSERT INTO `biz_project_step_menu` (`id`, `parent_id`, `name`, `code`, `level`, `create_time`, `sorts`, `is_document`, `oa_approval`, `offline_approval`, `show`, `service_status`, `notify_user_id`, `notify_content`) VALUES (42, 0, '项目授权', 'xmsq', 1, NULL, 9, 1, 1, 1, 1, NULL, NULL, NULL);



UPDATE biz_project_step_menu SET parent_id = 42 where id = 15;