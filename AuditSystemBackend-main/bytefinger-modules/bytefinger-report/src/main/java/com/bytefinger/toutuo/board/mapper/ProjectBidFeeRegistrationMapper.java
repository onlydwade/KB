package com.bytefinger.toutuo.board.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.board.domain.ProjectBidFeeRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ProjectBidFeeRegistrationMapper {
    Page<ProjectBidFeeRegistration> page (IPage<ProjectBidFeeRegistration> page, @Param(Constants.WRAPPER) Wrapper<ProjectBidFeeRegistration> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params, @Param("bfr") Map<String, Object> bfrParams);
}
