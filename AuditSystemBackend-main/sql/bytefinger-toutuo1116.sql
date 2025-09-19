#4.1.3	拓展管理-续签重新投标流程改造
#1.新增拓展项目库续签业绩增量等字段
ALTER TABLE biz_project ADD is_performance_increment VARCHAR (255) DEFAULT 'FOU' COMMENT '是否有业绩增量(FOU:否,SHI:是),默认否';
ALTER TABLE biz_project ADD annual_conversion_amounts  decimal (20,2) DEFAULT NULL COMMENT '增量当年转化收入';
ALTER TABLE biz_project ADD contract_annual_amounts  decimal (20,2) DEFAULT NULL COMMENT '增量合同年度金额';
ALTER TABLE biz_project ADD contract_amounts  decimal (20,2) DEFAULT NULL COMMENT '增量合同总金额';
#2.记得处理原来的旧数据,重点关注项目类型未设置选项.

#4.1.4	拓展管理-项目评审
#1.增加项目优先级字典(执行完毕后要清理字典缓存)
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '拓运一体化项目', 'TUO_YUN_YI_TI_HUA_XIANG_MU', 'XIANG_MU_YOU_XIAN_JI', '', '', 'Y', '0', 'admin', now(), '', NULL, '拓运一体化项目');
#2.修改字典的原因项目优先级字段的"普通项目"名称为"一般项目"
UPDATE sys_dict_data SET dict_label = '一般项目' WHERE dict_value = 'PU_TONG_XIANG_MU';
#3.增加重点关注项目字典(执行完毕后要清理字典缓存)
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '年饱和收入1000万以上的项目', '1', 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU', '', '', 'Y', '0', 'admin', now(), '', NULL, '年饱和收入1000万以上的项目');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '重点战略项目', '2', 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU', '', '', 'Y', '0', 'admin', now(), '', NULL, '重点战略项目');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '需要前期投资项目', '3', 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU', '', '', 'Y', '0', 'admin', now(), '', NULL, '需要前期投资项目');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (4, '运营测算亏损的战略项目', '4', 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU', '', '', 'Y', '0', 'admin', now(), '', NULL, '运营测算亏损的战略项目');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, '管理层关注的重点项目', '5', 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU', '', '', 'Y', '0', 'admin', now(), '', NULL, '管理层关注的重点项目');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (6, '其他特殊情况', '6', 'ZHONG_DIAN_GUAN_ZHU_XIANG_MU', '', '', 'Y', '0', 'admin', now(), '', NULL, '其他特殊情况');
#4.增加项目重点关注项目选项,以及增加其他特殊情况的内容
ALTER TABLE biz_project ADD key_focus_project  VARCHAR (255) DEFAULT NULL COMMENT '重点关注项目选项(枚举类型TOU_BIAO_WU_XIAO_YUAN_YIN)，可以多选,以逗号分割,1.年饱和收入1000万以上的项目,2.重点战略项目,3.需要前期投资项目,4.运营测算亏损的战略项目,5.管理层关注的重点项目,6.其他特殊情况';
ALTER TABLE biz_project ADD other_description  TEXT  DEFAULT NULL COMMENT '其他特殊情况内容';
#5.删除原有商务标书上传、技术方案上传、报价文件上传 等数据
delete from biz_project_document_template where oper_name = '商务标书上传' AND step_menu_id = 12;
delete from biz_project_document_template where oper_name = '技术方案上传' AND step_menu_id = 12;
delete from biz_project_document_template where oper_name = '报价文件上传' AND step_menu_id = 12;
#6.记得处理原来的旧数据,重点关注项目类型未设置选项.

#4.1.6	新增项目未中标"废标"字段
#1.增加未中标原因枚举(执行完毕后要清理字典缓存)
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '投标有效但排名靠后', 'TOU_BIAO_YOU_XIAO_DAN_PAI_MING_KAO_HOU', 'WEI_ZHONG_BIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '投标有效但排名靠后');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '未参与投标', 'WEI_CAN_YU_TOU_BIAO', 'WEI_ZHONG_BIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '未参与投标');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '投标无效', 'TOU_BIAO_WU_XIAO', 'WEI_ZHONG_BIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '投标无效');
#2.增加投标无效原因枚举(执行完毕后要清理字典缓存)
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '业绩合同问题', '1', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '业绩合同问题');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '资履罢质证书问题', '2', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '资履罢质证书问题');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '保证金问题财务问题', '3', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '保证金问题财务问题');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '格式问题', '4', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '格式问题');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '授权委托书问题', '5', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '授权委托书问题');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '商务文件错误', '6', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '商务文件错误');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '技术文件错误', '7', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '技术文件错误');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '报价错误', '8', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '报价错误');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '封标递交、开标等环节问题', '9', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '封标递交、开标等环节问题');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '提供虚假材料', '10', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '提供虚假材料');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '违规事标', '11', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '违规事标');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '其他问题', '12', 'TOU_BIAO_WU_XIAO_YUAN_YIN', '', '', 'Y', '0', 'admin', now(), '', NULL, '其他问题');
#3.增加未中标原因,投标无效原因,其他无效标原因字段
ALTER TABLE biz_project ADD not_selected_reason  VARCHAR (255) DEFAULT NULL COMMENT '未中标原因(枚举类型WEI_ZHONG_BIAO_YUAN_YIN),投标有效但排名靠后(TOU_BIAO_YOU_XIAO_DAN_PAI_MING_KAO_HOU),未参与投标(WEI_CAN_YU_TOU_BIAO),投标无效(TOU_BIAO_WU_XIAO)';
ALTER TABLE biz_project ADD invalid_bid_reason  VARCHAR (255) DEFAULT NULL COMMENT '投标无效原因选项(枚举类型TOU_BIAO_WU_XIAO_YUAN_YIN)，可以多选,以逗号分割,1.业绩合同问题,2.业绩合同问题,3.保证金问题财务问题,4.格式问题,5.授权委托书问题,6.商务文件错误“,7.技术文件错误,8.报价错误,9.封标递交、开标等环节问题,10.提供虚假材料,11.违规事标,12.其他问题';
ALTER TABLE biz_project ADD other_invalid_bid_beason  TEXT  DEFAULT NULL COMMENT '其他无效原因';
#4.记得处理原来的单一投标项目投标复盘旧数据,未中标原因,投标无效原因,其他无效标原因.