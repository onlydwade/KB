package com.bytefinger.toutuo.biz.projectextension.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectAssess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 项目评估
 * 
 * @author ycj
 * @email 
 * @date 2023-03-16 10:18:21
 */
@Mapper
public interface ProjectAssessMapper extends BaseMapper<ProjectAssess> {
    /**
     * 查询列表数据
     *
     * @param page
     * @param wrapper
     * @param params
     * @return
     */
    Page<Project> list(IPage<Project> page, @Param(Constants.WRAPPER) Wrapper<Project> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);
}
