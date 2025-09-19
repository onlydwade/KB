#4.1.8.	拓运移交
#1.修改拓后移交为拓运移交
update biz_project_step_menu set name = '拓运移交' where code = 'thyj' and name = '拓后移交';
#2.清除固定单一投标和单一非投标项目的菜单节点配置列表
delete from biz_project_step_menu_config where step_menu_id = (select id from biz_project_step_menu where name = '项目授权委托');
#3.新增运营团队组建菜单(需要知道上级拓运移交的菜单ID作为父级ID)
#4.新增运营团队菜单固定到单一投标和单一非投标项目的菜单节点配置列表
INSERT INTO `biz_project_step_menu_config`(`project_type`, `step_menu_id`) VALUES ('DAN_YI_TOU_BIAO_XIANG_MU', 41);
INSERT INTO `biz_project_step_menu_config`(`project_type`, `step_menu_id`) VALUES ('DAN_YI_FEI_TOU_BIAO_XIANG_MU', 41);
#5.注意处理原有单一投标和单一非投标项目的历史数据

