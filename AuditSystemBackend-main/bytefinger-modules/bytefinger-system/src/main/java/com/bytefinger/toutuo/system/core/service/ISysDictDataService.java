package com.bytefinger.toutuo.system.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
public interface ISysDictDataService extends IService<SysDictData> {


    /**
     * 执行忽略字典数据*
     * @param sysDictDataList
     * @return
     */
    List<SysDictData> ignoreDictDataList(List<SysDictData> sysDictDataList);
}
