ALTER table biz_project_company_high_level_meeting   add COLUMN    `meeting_time` datetime DEFAULT NULL COMMENT '会议时间';
ALTER table biz_project_company_high_level_meeting_approval   add COLUMN    `meeting_time` datetime DEFAULT NULL COMMENT '会议时间';

alter table sys_dept add COLUMN    `dept_type` varchar(50)  DEFAULT NULL COMMENT '组织部门类型';


update  sys_dept   set dept_type='CENG_JI' where dept_id IN  ( select a.* from (select DISTINCT A.dept_id from  sys_dept A ,sys_dept B  where A.dept_id =B.parent_id) a );
update sys_dept set dept_type='BU_MEN' where dept_type is null;

alter table biz_project add COLUMN    `stamp_category` varchar(100)  DEFAULT NULL COMMENT '盖章类型';


