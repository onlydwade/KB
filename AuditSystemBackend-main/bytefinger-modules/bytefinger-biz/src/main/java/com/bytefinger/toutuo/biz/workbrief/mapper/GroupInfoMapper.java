package com.bytefinger.toutuo.biz.workbrief.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.workbrief.domain.GroupInfo;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBrief;
import com.bytefinger.toutuo.biz.workbrief.dto.GroupUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 工作简报 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-01-30
 */
@Mapper
public interface GroupInfoMapper extends BaseMapper<GroupInfo> {

    Page<GroupInfo> list(IPage<GroupInfo> page, @Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    List<GroupUserDto> groupUserList(@Param("ids")List<Long> groupIdList);
}
