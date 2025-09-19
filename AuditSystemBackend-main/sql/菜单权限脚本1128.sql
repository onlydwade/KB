-- 拓展管理-续签重新投标权限
INSERT sys_menu  (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `can_del`)
select `menu_name`, (select menu_id from sys_menu where parent_id in (select menu_id from sys_menu where  menu_name = '拓展管理') and menu_name = '续签/重新投标'), `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `can_del` from sys_menu where parent_id in (select menu_id from sys_menu where parent_id in (select menu_id from sys_menu where  menu_name = '拓展管理') and menu_name = '新拓管理')


-- 拓展管理-在管项目库权限
INSERT sys_menu  (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `can_del`)
select `menu_name`, (select menu_id from sys_menu where parent_id in (select menu_id from sys_menu where  menu_name = '拓展管理') and menu_name = '在管项目库'), `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `can_del` from sys_menu 
where parent_id in (select menu_id from sys_menu where parent_id in (select menu_id from sys_menu where  menu_name = '拓后运营') and menu_name = '拓后权限集合')