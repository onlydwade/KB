package com.bytefinger.toutuo.system.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.constant.UserConstants;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;
import com.bytefinger.toutuo.system.config.DicTypeProperties;
import com.bytefinger.toutuo.system.core.mapper.SysDictDataMapper;
import com.bytefinger.toutuo.system.core.mapper.SysDictTypeMapper;
import com.bytefinger.toutuo.system.core.service.ISysDictTypeService;
import com.bytefinger.toutuo.system.dto.GetEditDicData;
import com.bytefinger.toutuo.system.dto.GetEditDicType;
import com.bytefinger.toutuo.system.dto.GetYeTaiDicData;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
@Service
@AllArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final SysDictTypeMapper sysDictTypeMapper;

    private final SysDictDataMapper sysDictDataMapper;

    @Resource
    private DicTypeProperties properties;

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict) {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        SysDictType sysDictType = sysDictTypeMapper.selectOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getDictType, dict.getDictType()).last(" limit 1 "));
        if (null != sysDictType && !sysDictType.getDictId().equals(dictId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 加载字典缓存数据
     */
    public void loadingDictCache() {
        List<SysDictData> sysDictData = sysDictDataMapper.selectList(Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getStatus, "0").orderByAsc(SysDictData::getDictSort));

        // 设置字典
        Map<String, List<SysDictData>> dictDataMap = sysDictData.stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }

        // 设置有父级类型的字典
        List<SysDictType> sysDictType = sysDictTypeMapper.selectList(Wrappers.<SysDictType>lambdaQuery().isNotNull(SysDictType::getDictPtype).eq(SysDictType::getStatus, "0").orderByAsc(SysDictType::getDictId));
        Map<String, List<SysDictType>> dictPdataMap = sysDictType.stream().collect(Collectors.groupingBy(SysDictType::getDictPtype));
        for (Map.Entry<String, List<SysDictType>> entry : dictPdataMap.entrySet()) {
            DictUtils.setDictPtypeCache((entry.getKey()), entry.getValue().stream().sorted(Comparator.comparing(SysDictType::getDictId)).collect(Collectors.toList()));
        }

        // 设置整体key
        DictUtils.setDictCacheAll(dictDataMap);
    }

    /**
     * 清空字典缓存数据
     */
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    @Override
    public List<GetEditDicType> getEditTypeList(String dictName) {
        String defaultDictType = "XIANG_MU_YE_TAI";
        List<String> dicTypeList = Lists.newArrayList();
        if (!StringUtils.isEmpty(properties.getValue())) {
            dicTypeList = Arrays.asList(properties.getValue().split(",").clone()).stream().filter(dicTypeListStr->!dicTypeListStr.equals(defaultDictType)).collect(Collectors.toList());
        }

        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        if (CollectionUtil.isNotEmpty(dicTypeList)) {
            queryWrapper.in("dict_type", dicTypeList);
        }
        if (!StringUtils.isEmpty(dictName)) {
            queryWrapper.like("dict_name", dictName);
        }
        List<SysDictType> sysDictTypeList = this.list(queryWrapper);
        //特殊处理项目业态的字典信息
        List<SysDictType> projectFormatDictList = this.list(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictPtype, defaultDictType).like(StrUtil.isNotBlank(dictName), SysDictType::getDictName, dictName));
        if (CollectionUtil.isNotEmpty(projectFormatDictList)){
            sysDictTypeList.addAll(projectFormatDictList);
        }

        List<GetEditDicType> dicList = Lists.newArrayList();
        sysDictTypeList.forEach(o -> {
            GetEditDicType item = new GetEditDicType();
            item.setDictType(o.getDictType());
            item.setDictName(o.getDictName());
            item.setDictId(o.getDictId());
            item.setRemark(o.getRemark());
            if (dicList.stream().filter(m->m.getDictId().equals(o.getDictId())).count() == 0){
                dicList.add(item);
            }
        });
        return dicList;
    }

    @Override
    public List<GetEditDicData> getDataListByType(String dictType) {

        if (StringUtils.isEmpty(dictType)) {
            return Lists.newArrayList();
        }

        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);

        List<SysDictData> dictDataList = sysDictDataMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(dictDataList)) {
            return Lists.newArrayList();
        }

        List<GetEditDicData> result = new ArrayList<>();

        dictDataList.forEach(o -> {
            GetEditDicData item = new GetEditDicData();
            item.setDictCode(o.getDictCode());
            item.setDictSort(o.getDictSort());
            item.setDictLabel(o.getDictLabel());
            item.setDictValue(o.getDictValue());
            item.setDictType(o.getDictType());
            result.add(item);
        });

        return result.stream().sorted(Comparator.comparing(GetEditDicData::getDictSort)).collect(Collectors.toList());
    }

    @Override
    public String addDataInfo(GetEditDicData getEditDicData) {

        if (StringUtils.isEmpty(getEditDicData.getDictType())) {
            return "字典类型不能为空";
        }
        if (getEditDicData.getDictSort() == null) {
            return "字典排序不能为空";
        }
        if (StringUtils.isEmpty(getEditDicData.getDictLabel())) {
            return "字典名称不能为空";
        }
        if (StringUtils.isEmpty(getEditDicData.getDictValue())) {
            return "字典值不能为空";
        }

        List<GetEditDicData> dicDataList = getAllDataListByType(getEditDicData.getDictType());
        if (!CollectionUtils.isEmpty(dicDataList)) {
            if (dicDataList.stream().filter(o -> getEditDicData.getDictLabel().equals(o.getDictLabel())).count() > 0) {
                return "字典名称不能重复";
            }
            if (dicDataList.stream().filter(o -> getEditDicData.getDictValue().equals(o.getDictValue())).count() > 0) {
                return "字典值不能重复";
            }
        }

        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictCode(getEditDicData.getDictCode());
        sysDictData.setDictSort(getEditDicData.getDictSort());
        sysDictData.setDictLabel(getEditDicData.getDictLabel());
        sysDictData.setDictValue(getEditDicData.getDictValue());
        sysDictData.setDictType(getEditDicData.getDictType());
        sysDictData.setStatus("0");
        sysDictData.setIsDefault("1");
        sysDictData.setCreateBy(SecurityUtils.getUsername());
        sysDictData.setCreateTime(new Date());
        int result = sysDictDataMapper.insert(sysDictData);

        //业态统计字典添加数据
        if (getEditDicData.getDictType().contains("XIANG_MU_YE_TAI")) {
            SysDictData pSysDictData = new SysDictData();
            pSysDictData.setDictCode(getEditDicData.getDictCode());
            pSysDictData.setDictSort(getEditDicData.getDictSort());
            pSysDictData.setDictLabel(getEditDicData.getDictLabel());
            pSysDictData.setDictValue(getEditDicData.getDictValue());
            pSysDictData.setDictType("XIANG_MU_YE_TAI");
            pSysDictData.setStatus("0");
            pSysDictData.setIsDefault("1");
            pSysDictData.setCreateBy(SecurityUtils.getUsername());
            pSysDictData.setCreateTime(new Date());
            int pResult = sysDictDataMapper.insert(pSysDictData);
        }

        if (result > 0) {
            return "";
        }
        return "保存失败";
    }

    public List<GetEditDicData> getAllDataListByType(String dictType) {

        if (StringUtils.isEmpty(dictType)) {
            return Lists.newArrayList();
        }

        List<String> dictTypes = Lists.newArrayList();
        dictTypes.add(dictType);

        QueryWrapper<SysDictType> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.eq("dict_type", dictType);
        List<SysDictType> childList = this.list(typeQueryWrapper);
        if (CollectionUtils.isEmpty(childList)) {
            return Lists.newArrayList();
        }

        //往上查一级后，查询所有
        System.out.println(childList.get(0).getDictPtype());
        if (!StringUtils.isEmpty(childList.get(0).getDictPtype())) {
            System.out.println("查询1");
            QueryWrapper<SysDictType> allTypeQueryWrapper = new QueryWrapper<>();
            allTypeQueryWrapper.eq("dict_ptype", childList.get(0).getDictPtype());
            List<SysDictType> allList = this.list(allTypeQueryWrapper);
            dictTypes.addAll(allList.stream().map(SysDictType::getDictType).collect(Collectors.toList()));
        }


        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dict_type", dictTypes.stream().distinct().collect(Collectors.toList()));

        List<SysDictData> dictDataList = sysDictDataMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(dictDataList)) {
            return Lists.newArrayList();
        }

        List<GetEditDicData> result = new ArrayList<>();

        dictDataList.forEach(o -> {
            GetEditDicData item = new GetEditDicData();
            item.setDictCode(o.getDictCode());
            item.setDictSort(o.getDictSort());
            item.setDictLabel(o.getDictLabel());
            item.setDictValue(o.getDictValue());
            item.setDictType(o.getDictType());
            result.add(item);
        });

        return result;
    }

    @Override
    public List<GetYeTaiDicData> getYeTaiDic() {
        List<GetYeTaiDicData> yeTaiDicDataList = Lists.newArrayList();

        String typeYt = "XIANG_MU_YE_TAI";
        QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_ptype", typeYt);
        List<SysDictType> dictTypeList = this.list(queryWrapper);

        if (CollectionUtils.isEmpty(dictTypeList)) {
            return Lists.newArrayList();
        }

        QueryWrapper<SysDictData> queryDataWrapper = new QueryWrapper<>();
        queryDataWrapper.in("dict_type", dictTypeList.stream().map(SysDictType::getDictType).collect(Collectors.toList()));
        queryDataWrapper.orderByAsc("dict_sort");
        List<SysDictData> dictDataList = sysDictDataMapper.selectList(queryDataWrapper);

        dictTypeList.forEach(o -> {
            GetYeTaiDicData item = new GetYeTaiDicData();
            item.setValue(o.getDictType());
            item.setLabel(o.getDictName());
            item.setChildren(toYeTaiTto(dictDataList.stream().filter(s -> o.getDictType().equals(s.getDictType())).collect(Collectors.toList())));
            yeTaiDicDataList.add(item);
        });

        return yeTaiDicDataList;
    }

    @Override
    public String addDictType(GetEditDicType getEditDicType) {
        //默认父级字典类型值
        String defaultDictPtype = "XIANG_MU_YE_TAI";
        if (StringUtils.isEmpty(getEditDicType.getDictType())) {
            return "字典类型值不能为空";
        }
        if (StringUtils.isEmpty(getEditDicType.getDictName())) {
            return "字典类型名称不能为空";
        }
        List<SysDictType> sysDictPtypes = sysDictTypeMapper.selectList(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictPtype, defaultDictPtype));
        long dictTypeCount = sysDictPtypes.stream().filter(s -> s.getDictType().equals(getEditDicType.getDictType())).count();
        long dictNameCount = sysDictPtypes.stream().filter(s -> s.getDictName().equals(getEditDicType.getDictName())).count();
        if (dictTypeCount > 0) {
            return "字典类型值不能重复";
        }
        if (dictNameCount > 0) {
            return "字典类型名称不能重复";
        }
        SysDictType sysDictType = new SysDictType();
        sysDictType.setDictName(getEditDicType.getDictName());
        //判断是否包含项目业态,不包含就自动添加
        if (!getEditDicType.getDictType().contains(defaultDictPtype)){
            getEditDicType.setDictType(defaultDictPtype+"_"+getEditDicType.getDictType());
        }
        sysDictType.setDictType(getEditDicType.getDictType());
        sysDictType.setDictPtype(defaultDictPtype);
        sysDictType.setStatus(0 + "");
        sysDictType.setCreateBy(SecurityUtils.getUsername());
        sysDictType.setCreateTime(new Date());
        sysDictType.setRemark(getEditDicType.getRemark());
        sysDictTypeMapper.insert(sysDictType);
        return "";
    }

    public List<GetYeTaiDicData> toYeTaiTto(List<SysDictData> sysDictDatas) {

        List<GetYeTaiDicData> result = Lists.newArrayList();
        sysDictDatas.forEach(sysDictData -> {
            GetYeTaiDicData item = new GetYeTaiDicData();
            item.setLabel(sysDictData.getDictLabel());
            item.setValue(sysDictData.getDictValue());
            result.add(item);
        });
        return result;
    }

}
