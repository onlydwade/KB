package com.bytefinger.toutuo.biz.project.constants;

/**
 * @Author Jone Ying
 * @Date 2023/2/1
 **/
public interface ProjectConstant {

    //竞标方
    int PROJECT_BID_SELF = 1;
    int PROJECT_BID_OPPONENT = 2;

    //中标状态 0 未中标 1中标
    int FAIL_BID = 0;
    int WIN_BID = 1;

    //项目失效状态(有效、已失效)
    String XIANG_MU_SHI_XIAO_ZHUANG_TAI_YOU_XIAO = "YOU_XIAO";
    String XIANG_MU_SHI_XIAO_ZHUANG_TAI_YI_SHI_XIAO = "YI_SHI_XIAO";


    String GU_QUAN_HE_ZUO_XIANG_MU = "GU_QUAN_HE_ZUO_XIANG_MU";
}
