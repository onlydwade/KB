package com.bytefinger.toutuo.biz.status.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.status.domain.SysStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 状态配置基础表 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Mapper
public interface SysStatusMapper extends BaseMapper<SysStatus> {

    /**
     * 通过group id 查询列表
     *
     * @param groupId
     * @return
     */
    List<SysStatus> listByGroupId(Integer groupId);

}
