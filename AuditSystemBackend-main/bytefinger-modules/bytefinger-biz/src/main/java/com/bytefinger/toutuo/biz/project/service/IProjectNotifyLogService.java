package com.bytefinger.toutuo.biz.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.ProjectNotifyLog;

/**
 * <p>
 * 通告撤销日志 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-31
 */
public interface IProjectNotifyLogService extends IService<ProjectNotifyLog> {

    /**
     * 分页查询通告撤销日志
     *
     * @param queryPage 传参
     * @return
     */
    IPage page(QueryPage queryPage);

}
