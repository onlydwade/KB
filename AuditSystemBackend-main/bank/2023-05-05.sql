
-- 单一投标项目父级类型
INSERT INTO `sys_dict_type` ( `dict_name`, `dict_type`, `dict_ptype`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('单一投标项目', 'DAN_YI_TOU_BIAO_XIANG_MU', 'XIANG_MU_LEI_XING', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '单一投标项目');

-- 单一非投标项目父级类型
INSERT INTO `sys_dict_type` ( `dict_name`, `dict_type`, `dict_ptype`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('单一非投标项目', 'DAN_YI_FEI_TOU_BIAO_XIANG_MU', 'XIANG_MU_LEI_XING', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '单一非投标项目');

-- 单一投标项目
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 1, '中石油投标', 'ZHONG_SHI_YOU_TOU_BIAO', 'DAN_YI_TOU_BIAO_XIANG_MU', '', '', 'Y', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '中石油投标');
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 2, '外部投标', 'WAI_BU_TOU_BIAO', 'DAN_YI_TOU_BIAO_XIANG_MU', '', '', 'Y', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '外部投标');

-- 单一非投标项目
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '中石油承接', 'ZHONG_SHI_YOU_CHENG_BIAO', 'DAN_YI_FEI_TOU_BIAO_XIANG_MU', '', '', 'Y', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '中石油承标');
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, '外部承接', 'WAI_BU_CHENG_BIAO', 'DAN_YI_FEI_TOU_BIAO_XIANG_MU', '', '', 'Y', '0', 'admin', '2022-10-04 03:10:12', '', NULL, '外部承标');

