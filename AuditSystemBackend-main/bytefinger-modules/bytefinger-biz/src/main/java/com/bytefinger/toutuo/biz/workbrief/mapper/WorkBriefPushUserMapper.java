package com.bytefinger.toutuo.biz.workbrief.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBriefPushUser;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkPushUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 工作简报推送对象 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-01-30
 */
@Mapper
public interface WorkBriefPushUserMapper extends BaseMapper<WorkBriefPushUser> {

    /**
     * 根据工作简报id删除工作简报推送对象-逻辑删除
     *
     * @param briefId
     * @return
     */
    void deleteByBriefId(@Param("briefId")Long briefId);

    /**
     * 根据工作简报id查找推送对象
     *
     * @param ids
     * @return
     */
    List<WorkPushUserDto> getListByBriefId(@Param("ids")List<Long> ids);
}
