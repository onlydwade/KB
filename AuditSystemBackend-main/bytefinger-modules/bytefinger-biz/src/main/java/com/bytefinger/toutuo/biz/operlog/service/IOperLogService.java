package com.bytefinger.toutuo.biz.operlog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.operlog.domain.OperLog;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;

import java.util.List;

/**
 * <p>
 * 业务数据变更记录表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-12
 */
public interface IOperLogService extends IService<OperLog> {

    /**
     * 记录系统变更日志
     *
     * @param modelName
     * @param dataId
     * @param updateLog
     */
    void ruleLogBySystem(String modelName, Long dataId, UpdateLog updateLog);

    /**
     * 记录日志
     *
     * @param modelName
     * @param dataId
     * @param log
     */
    void changeLog(String modelName, Long dataId, String log);

    /**
     * 变更日志
     *
     * @param clazz
     * @param dataId
     * @param fieldName
     * @param valueBefore
     * @param valueAfter
     */
    void changeLog(Class clazz, Long dataId, String fieldName, String valueBefore, String valueAfter);

    /**
     * 通过模块 + 数据id 查询操作日志
     *
     * @param moduleName
     * @param recordId
     * @return
     */
    List<OperLog> listByModuleAndRecordId(String moduleName, Long recordId);

    /**
     * 获取数据
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<OperLog> pageData(IPage<OperLog> page, Wrapper<OperLog> queryWrapper);
}
