package com.bytefinger.toutuo.biz.workbrief.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkDataBoardPushInfo;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkDataBoardPushUser;
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
public interface WorkDataBoardPushInfoMapper extends BaseMapper<WorkDataBoardPushInfo> {

}
