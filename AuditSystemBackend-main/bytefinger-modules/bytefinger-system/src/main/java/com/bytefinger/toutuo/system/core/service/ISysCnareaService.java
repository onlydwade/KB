package com.bytefinger.toutuo.system.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.system.core.domain.SysCnarea;
import com.bytefinger.toutuo.api.system.core.domain.vo.AreaListVo;

import java.util.List;

/**
 * <p>
 * 中国行政地区表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-14
 */
public interface ISysCnareaService extends IService<SysCnarea> {

    /**
     * 到省市级别
     *
     * @return
     */
    List<AreaListVo> listProvinceCity();

    /**
     * 到省市区级别
     *
     * @return
     */
    List<AreaListVo> listProvinceCityArea();

    /**
     * 查询街道列表
     *
     * @param parentCode
     * @return
     */
    List<AreaListVo> listStreet(Long parentCode);

    /**
     * 设置缓存
     */
    void setCache();

}
