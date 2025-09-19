package com.bytefinger.toutuo.biz.followlog.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bytefinger.toutuo.biz.followlog.domain.BizFollowLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 线索跟踪记录 Mapper 接口
 * </p>
 *
 * @author patrick
 * @since 2022-11-17
 */
@Mapper
public interface FollowLogMapper {


    /**
     * 获取一条记录
     *
     * @param tableName
     * @param id
     * @return
     */
    BizFollowLog getById(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 获取一条记录
     *
     * @param tableName
     * @param recordId
     * @return
     */
    BizFollowLog lastData(@Param("tableName") String tableName, @Param("recordId") Long recordId);

    /**
     * 新增 跟踪记录
     *
     * @param tableName
     * @param followLogEntity
     * @return
     */
    int add(@Param("tableName") String tableName, @Param("fe") BizFollowLog followLogEntity);

    /**
     * 修改 跟踪记录
     *
     * @param tableName
     * @param followLogEntity
     * @return
     */
    int update(@Param("tableName") String tableName, @Param("fe") BizFollowLog followLogEntity);

    /**
     * 删除 跟踪记录
     *
     * @param tableName
     * @param ids
     * @return
     */
    void deleteByIds(@Param("tableName") String tableName, @Param("ids") List<Long> ids);

    /**
     * 分页 跟踪记录
     *
     * @param page
     * @param tableName
     * @param wrapper
     * @return
     */
    IPage list(IPage page, @Param("tableName") String tableName, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 分页 跟踪记录
     *
     * @param tableName
     * @param recordId
     * @return
     */
    List<String> getFiles(@Param("tableName") String tableName, @Param("recordId") Long recordId);

    /**
     * 修改实体时间
     *
     * @param tableName
     * @param id
     * @return
     */
    int updateEntityFollowTime(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 获取实体
     *
     * @param tableName
     * @param id
     * @return
     */
    Date getEntityById(@Param("tableName") String tableName, @Param("id") Long id);

}
