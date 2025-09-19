#4.1.5	拓展管理新增"标书评审"
#2.修改项目节点原有枚举的排序值
update biz_project_step_menu set sorts = 6 where name = '投标复盘' AND parent_id = 0;
update biz_project_step_menu set sorts = 7 where name = '业绩确认' AND parent_id = 0;
update biz_project_step_menu set sorts = 8 where name = '拓后移交' AND parent_id = 0;
update biz_project_step_menu set sorts = 9 where name = '项目尽调' AND parent_id = 0;
update biz_project_step_menu set sorts = 10 where name = '项目决策' AND parent_id = 0;
#3.新增单一投标项目的项目节点枚举(其中step_menu_id值要与第一点id保持一致)
INSERT INTO `biz_project_step_menu_config`(`project_type`, `step_menu_id`) VALUES ('DAN_YI_TOU_BIAO_XIANG_MU', 38);
INSERT INTO `biz_project_step_menu_config`(`project_type`, `step_menu_id`) VALUES ('DAN_YI_TOU_BIAO_XIANG_MU', 37);
#4.添加项目节点文档模板枚举(其中step_menu_id值要与第一点id保持一致)
INSERT INTO `biz_project_document_template`(`step_menu_id`, `project_type`, `expansion_mode`, `oper_name`, `is_online`, `sorts`, `required`, `code`, `remarks`, `template_documents`, `file_format`, `disabled`) VALUES (37, '', NULL, '会签文件上传（线下审批时需上传）', 0, 2, 1, 'BSPS', '', '[]', NULL, 0);
INSERT INTO `biz_project_document_template`(`step_menu_id`, `project_type`, `expansion_mode`, `oper_name`, `is_online`, `sorts`, `required`, `code`, `remarks`, `template_documents`, `file_format`, `disabled`) VALUES (37, '', NULL, '标书材料上传', 1, 1, 1, 'BSPS', '', '[]', NULL, 0);
#5.添加OA审批模板(其中step_menu_id值要与第一点id保持一致)
#6.新增标书类型,申请类型,关联用印类型,标书审核事项
#6.1标书类型
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '纸质标', 'ZHI_ZHI_BIAO', 'TOU_BIAO_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '纸质标');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '电子标', 'DIAN_ZHI_BIAO', 'TOU_BIAO_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '电子标');
#6.2申请类型
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '总部级印章', 'ZONG_BU_JI_BIE_YIN_ZHANG', 'SHEN_QING_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '总部级印章');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '单位级印章', 'DAN_WEI_JI_BIE_YIN_ZHANG', 'SHEN_QING_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '单位级印章');
#6.3关联用印类型(纸质标)
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '盖法人签章', '1', 'GUAN_LIAN_YONG_YIN_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '盖法人签章');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '盖法人手签章', '2', 'GUAN_LIAN_YONG_YIN_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '盖法人手签章');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '法人签字', '3', 'GUAN_LIAN_YONG_YIN_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '法人签字');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (4, '借公章', '4', 'GUAN_LIAN_YONG_YIN_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '借公章');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, '借法人章', '5', 'GUAN_LIAN_YONG_YIN_LEI_XING', '', '', 'Y', '0', 'admin', now(), '', NULL, '借法人章');
#6.4关联用印类型(电子标)
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 'CA证书盖章', '1', 'GUAN_LIAN_YONG_YIN_LEI_XING_DIAN_ZI', '', '', 'Y', '0', 'admin', now(), '', NULL, 'CA证书盖章');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '投标文件需借CA', '2', 'GUAN_LIAN_YONG_YIN_LEI_XING_DIAN_ZI', '', '', 'Y', '0', 'admin', now(), '', NULL, '投标文件需借CA');
#6.4标书审核事项
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '标书格式', 'BIAO_SHU_GE_SHI', 'BIAO_SHU_SHEN_HE_SHI_XIANG', '', '', 'Y', '0', 'admin', now(), '', NULL, '标书格式');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '资质证件有效性', 'ZI_ZHI_ZHENG_JIAN_YOU_XIAO_XING', 'BIAO_SHU_SHEN_HE_SHI_XIANG', '', '', 'Y', '0', 'admin', now(), '', NULL, '资质证件有效性');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (3, '业绩合同的有效性', 'YE_JI_HE_TONG_DE_YOU_XIAO_XING', 'BIAO_SHU_SHEN_HE_SHI_XIANG', '', '', 'Y', '0', 'admin', now(), '', NULL, '业绩合同的有效性');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (4, '人员证件有效性', 'REN_YUAN_ZHENG_JIAN_DE_YOU_XIAO_XING', 'BIAO_SHU_SHEN_HE_SHI_XIANG', '', '', 'Y', '0', 'admin', now(), '', NULL, '人员证件有效性');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5, '保证金/保函信息', 'BAO_ZHENG_JING/BAO_HAN_XIN_XI', 'BIAO_SHU_SHEN_HE_SHI_XIANG', '', '', 'Y', '0', 'admin', now(), '', NULL, '保证金/保函信息');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (6, '是否完全响应实质性条款', 'SHI_FOU_WAN_QUAN_XIANG_YING_SHI_ZHI_XING_TIAO_KUAN', 'BIAO_SHU_SHEN_HE_SHI_XIANG', '', '', 'Y', '0', 'admin', now(), '', NULL, '是否完全响应实质性条款');
INSERT INTO `sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (7, '是否完全响应符合性条款', 'SHI_FOU_WAN_QUAN_XIANG_YING_FU_HE_XING_TIAO_KUAN', 'BIAO_SHU_SHEN_HE_SHI_XIANG', '', '', 'Y', '0', 'admin', now(), '', NULL, '是否完全响应符合性条款');
#7.增加项目
ALTER TABLE biz_project ADD bid_type  VARCHAR (100) DEFAULT NULL COMMENT '投标类型(枚举类型TOU_BIAO_LEI_XING),纸质标(ZHI_ZHI_BIAO),电子标(DIAN_ZHI_BIAO)';
ALTER TABLE biz_project ADD application_type  VARCHAR (100) DEFAULT NULL COMMENT '申请类型(枚举类型SHEN_QING_LEI_XING),投标类型只有选择纸质标才可以显示选择,总部级印章(ZONG_BU_JI_BIE_YIN_ZHANG),单位级别(DAN_WEI_JI_BIE_YIN_ZHANG)';
ALTER TABLE biz_project ADD related_seal_type  VARCHAR (100) DEFAULT NULL COMMENT '关联用印类型(枚举类型，纸质标GUAN_LIAN_YONG_YIN_LEI_XING，电子标GUAN_LIAN_YONG_YIN_LEI_XING_DIAN_ZI)。当投标类型为纸质标时,可以多选,以逗号分割,1.盖法人签章,2.盖法人手签章，3.法人签字,4.借公章,5.借法人章。当投标类型为电子标时,只能单选,1.CA证书盖章,2.投标文件需借CA。';
#8.处理单一投标项目历史数据,只需增加项目节点即可,无需要显示.


