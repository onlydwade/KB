package com.bytefinger.toutuo.system.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.api.system.core.domain.SysDictType;
import com.bytefinger.toutuo.system.dto.GetEditDicData;
import com.bytefinger.toutuo.system.dto.GetEditDicType;
import com.bytefinger.toutuo.system.dto.GetYeTaiDicData;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
public interface ISysDictTypeService extends IService<SysDictType> {

    /**
     * 查询字典是否唯一
     *
     * @param dict
     * @return
     */
    String checkDictTypeUnique(SysDictType dict);

    /**
     * 重置字典
     */
    void resetDictCache();


    List<GetEditDicType> getEditTypeList(String dictName);

    List<GetEditDicData> getDataListByType(String dictType);

    String addDataInfo(GetEditDicData getEditDicData);

    List<GetYeTaiDicData> getYeTaiDic();

    /**
     * 添加字典类型数据*
     *
     * @param getEditDicType
     */
    String addDictType(GetEditDicType getEditDicType);

}
