package com.bytefinger.toutuo.system.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.utils.bean.BeanUtils;
import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.utils.CnareaUtils;
import com.bytefinger.toutuo.api.system.core.domain.vo.AreaListVo;
import com.bytefinger.toutuo.api.system.core.domain.vo.CnareaVO;
import com.bytefinger.toutuo.system.core.domain.SysCnarea;
import com.bytefinger.toutuo.system.core.mapper.SysCnareaMapper;
import com.bytefinger.toutuo.system.core.service.ISysCnareaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 中国行政地区表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-14
 */
@Service
@AllArgsConstructor
public class SysCnareaServiceImpl extends ServiceImpl<SysCnareaMapper, SysCnarea> implements ISysCnareaService {

    @Override
    public List<AreaListVo> listProvinceCity() {
        List<AreaListVo> listProvinceCity = CnareaUtils.getMapCache("listProvinceCity");
        if (null == listProvinceCity) {
            List<SysCnarea> dbAreas = this.list(new LambdaQueryWrapper<SysCnarea>().in(SysCnarea::getLevel, Arrays.asList(0, 1)));

            // 获取省份列表
            listProvinceCity = dbAreas.stream().filter(e -> e.getParentCode().equals(0L)).map(e -> {
                AreaListVo province = new AreaListVo();
                BeanUtils.copyBeanProp(province, e);

                // 获取城市列表
                province.setSubAreaList(dbAreas.stream().filter(s -> s.getParentCode().equals(e.getAreaCode())).map(s -> {
                    AreaListVo city = new AreaListVo();
                    BeanUtils.copyBeanProp(city, s);
                    return city;
                }).collect(Collectors.toList()));

                return province;
            }).collect(Collectors.toList());


            CnareaUtils.setMapCache("listProvinceCity", listProvinceCity);
        }

        return listProvinceCity;
    }

    @Override
    public List<AreaListVo> listProvinceCityArea() {
        List<AreaListVo> listProvinceCityArea = CnareaUtils.getMapCache("listProvinceCityArea");
        if (null == listProvinceCityArea) {
            List<SysCnarea> dbAreas = this.list(new LambdaQueryWrapper<SysCnarea>().in(SysCnarea::getLevel, Arrays.asList(0, 1, 2)));

            // 获取省份列表
            listProvinceCityArea = dbAreas.stream().filter(a -> a.getParentCode().equals(0L)).map(a -> {
                AreaListVo province = new AreaListVo();
                BeanUtils.copyBeanProp(province, a);

                // 获取城市列表
                province.setSubAreaList(dbAreas.stream().filter(b -> b.getParentCode().equals(a.getAreaCode())).map(b -> {
                    AreaListVo city = new AreaListVo();
                    BeanUtils.copyBeanProp(city, b);

                    // 获取区域列表
                    city.setSubAreaList(dbAreas.stream().filter(c -> c.getParentCode().equals(b.getAreaCode())).map(c -> {
                        AreaListVo area = new AreaListVo();
                        BeanUtils.copyBeanProp(area, c);
                        return area;
                    }).collect(Collectors.toList()));

                    return city;
                }).collect(Collectors.toList()));

                return province;
            }).collect(Collectors.toList());

            CnareaUtils.setMapCache("listProvinceCityArea", listProvinceCityArea);
        }

        return listProvinceCityArea;
    }

    @Override
    public List<AreaListVo> listStreet(Long parentCode) {
        List<SysCnarea> dbAreas = this.list(new LambdaQueryWrapper<SysCnarea>().eq(SysCnarea::getLevel, 3).eq(SysCnarea::getParentCode, parentCode));

        return dbAreas.stream().map(a -> {
            AreaListVo street = new AreaListVo();
            BeanUtils.copyBeanProp(street, a);
            return street;
        }).collect(Collectors.toList());
    }

    @Override
    public void setCache() {
        if (!CnareaUtils.exists()) {
            CnareaUtils.clearCache();

            List<SysCnarea> dbAreas = this.list(new LambdaQueryWrapper<SysCnarea>().in(SysCnarea::getLevel, Arrays.asList(0, 1, 2, 3)));
            Map<Integer, List<SysCnarea>> collect = dbAreas.stream().collect(Collectors.groupingBy(SysCnarea::getLevel));
            CnareaEnum[] values = CnareaEnum.values();

            if (CollectionUtil.isNotEmpty(collect)){
                int index = 0;
                for (CnareaEnum value : values) {
                    List<CnareaVO> areaList = collect.get(index++).stream().map(v -> new CnareaVO(v.getAreaCode(), v.getName())).collect(Collectors.toList());
                    for (CnareaVO cnareaVO : areaList) {
                        CnareaUtils.setCache(value.name() + cnareaVO.getCode(), cnareaVO);
                    }
                }
            }
        }
    }

    /**
     public void add() {
     List<SysCnarea> dbAreas = this.list(new LambdaQueryWrapper<SysCnarea>().in(SysCnarea::getLevel, Arrays.asList(2)));
     for (SysCnarea dbArea : dbAreas) {
     SysCnarea sysCnarea = new SysCnarea();
     sysCnarea.setLevel(3);
     sysCnarea.setParentCode(dbArea.getAreaCode());
     sysCnarea.setAreaCode(dbArea.getAreaCode() * 1000L + 999L);
     sysCnarea.setZipCode(99999);
     sysCnarea.setCityCode("999999");
     sysCnarea.setName("其他");
     sysCnarea.setShortName("其他");
     sysCnarea.setMergerName(dbArea.getMergerName() + ",其他");
     sysCnarea.setPinyin("QiTa");
     sysCnarea.setLat(BigDecimal.ZERO);
     sysCnarea.setLng(BigDecimal.ZERO);

     System.out.println(JSON.toJSONString(sysCnarea));

     super.save(sysCnarea);
     }
     }
     **/

}
