package com.bytefinger.toutuo.system.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.system.core.mapper.SysDictDataMapper;
import com.bytefinger.toutuo.system.core.service.ISysDictDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-09
 */
@Service
@AllArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {


    @Override
    public List<SysDictData> ignoreDictDataList(List<SysDictData> sysDictDataList) {
        //目前忽略项目的归档状态枚举,有其他还需要修改
        List<SysDictData> sysDictDataNew = sysDictDataList.stream().filter(sysDictData -> !(sysDictData.getDictType().equals("XIANG_MU_ZHUANG_TAI") && sysDictData.getDictValue().equals("CLOSE_FILE"))).collect(Collectors.toList());
        return sysDictDataNew;
    }
}
