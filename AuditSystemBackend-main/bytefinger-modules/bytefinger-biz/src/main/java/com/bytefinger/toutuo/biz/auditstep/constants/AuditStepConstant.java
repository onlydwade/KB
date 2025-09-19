package com.bytefinger.toutuo.biz.auditstep.constants;

/**
 * @Author Jone Ying
 * @Date 2023/2/1
 **/
public interface AuditStepConstant {

    //审计节点完成情况
    int AUDIT_STEP_DONE = 1;
    int AUDIT_STEP_UNDONE = 0;

    //项目节点是否需要上传文件
    int AUDIT_STEP_DOCUMENT_YES = 1;
    int AUDIT_STEP_DOCUMENT_NO = 0;

    String YJQRSP = "yjqrsp";
//项目立项
    String XMLX = "lxsp";


    String BCCLSP = "bcclsp";

    String GU_QUAN_HE_ZUO_XIANG_MU = "GU_QUAN_HE_ZUO_XIANG_MU";

    String DAN_YI_TOU_BIAO_XIANG_MU = "DAN_YI_TOU_BIAO_XIANG_MU";

    String DAN_YI_FEI_TOU_BIAO_XIANG_MU = "DAN_YI_FEI_TOU_BIAO_XIANG_MU";
}
