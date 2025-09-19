package com.bytefinger.toutuo.biz.workbrief.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkBrief;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface WorkBriefMapper extends BaseMapper<WorkBrief> {

    /**
     * 查询工作简报列表数据
     *
     * @param page
     * @param wrapper
     * @param params
     * @return
     */
    Page<WorkBrief> list(IPage<WorkBrief> page, @Param(Constants.WRAPPER) Wrapper<WorkBrief> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    /**
     * 查询最后一条工作简报数据
     * */
    WorkBrief initTitle();

    /**
     * 根据工作简报标题查找数据
     * */
    WorkBrief getOneByTitle(@Param("title")String title);

    /**
     * 更改工作简报的审批状态
     * */
    void updateApprovalStatus(@Param("id")Long id,@Param("approveStatus")Integer approveStatus);

    /**
     * 分页查询工作简报-通告
     *
     * @param page
     * @param wrapper
     * @param params
     * @return
     */
    Page<WorkBrief> pageNotify(IPage<WorkBrief> page, @Param(Constants.WRAPPER) Wrapper<WorkBrief> wrapper, @Param(Constants.COLUMN_MAP) Map<String, Object> params);

    Integer getGroupConfig();

    void setGroupConfig(@Param("configValue") Integer configValue);

    void updateReadInfo(@Param("moduleId")Long moduleId,@Param("userId")Long userId,@Param("title")String title);
}
