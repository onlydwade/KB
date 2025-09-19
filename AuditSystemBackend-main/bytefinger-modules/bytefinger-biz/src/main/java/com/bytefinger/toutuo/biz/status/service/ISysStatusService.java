package com.bytefinger.toutuo.biz.status.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.status.domain.SysStatus;

import java.util.List;

/**
 * <p>
 * 状态配置基础表 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
public interface ISysStatusService extends IService<SysStatus> {

    List<SysStatus> listByGroupId(Integer groupId);
}
