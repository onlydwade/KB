package com.bytefinger.toutuo.biz.operlog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.operlog.domain.ProjectExtensionOperLog;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;

import java.util.List;

/**
 * <p>
 * 业务数据变更记录表 服务类
 * </p>
 *
 * @author ycj
 * @since 2023-04-03
 */
public interface IProjectExtensionOperLogService extends IService<ProjectExtensionOperLog> {

    /**
     * 记录系统变更日志
     *
     * @param modelName
     * @param dataId
     * @param updateLog
     */
    void ruleLogBySystem(String modelName, Long dataId, UpdateLog updateLog,Long projectId);

    /**
     * 记录日志
     *
     * @param modelName
     * @param dataId
     * @param log
     */
    void changeLog(String modelName, Long dataId, String log,Long projectId);

    /**
     * 变更日志
     *
     * @param clazz
     * @param dataId
     * @param fieldName
     * @param valueBefore
     * @param valueAfter
     */
    void changeLog(Class clazz, Long dataId, String fieldName, String valueBefore, String valueAfter,Long projectId);

    /**
     * 通过模块 + 数据id 查询操作日志
     *
     * @param moduleName
     * @param recordId
     * @return
     */
    List<ProjectExtensionOperLog> listByModuleAndRecordId(Long projectId);

    /**
     * 获取数据
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<ProjectExtensionOperLog> pageData(IPage<ProjectExtensionOperLog> page, Wrapper<ProjectExtensionOperLog> queryWrapper);
}
