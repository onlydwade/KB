package com.bytefinger.toutuo.biz.projectextension.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCorrelation;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExtension;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 拓后运营项目关联
 * 
 * @author ycj
 * @email 
 * @date 2023-03-09
 */
@Mapper
public interface ProjectCorrelationMapper extends BaseMapper<ProjectCorrelation> {


}
