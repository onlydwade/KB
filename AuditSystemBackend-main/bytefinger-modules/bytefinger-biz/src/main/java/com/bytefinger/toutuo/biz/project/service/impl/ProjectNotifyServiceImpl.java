package com.bytefinger.toutuo.biz.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.project.domain.ProjectNotify;
import com.bytefinger.toutuo.biz.project.domain.ProjectNotifyLog;
import com.bytefinger.toutuo.biz.project.enums.NotifyTypeEnums;
import com.bytefinger.toutuo.biz.project.mapper.ProjectNotifyLogMapper;
import com.bytefinger.toutuo.biz.project.mapper.ProjectNotifyMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectNotifyLogService;
import com.bytefinger.toutuo.biz.project.service.IProjectNotifyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 通告撤销日志 服务实现类
 * </p>
 *
 * @author lin
 * @since 2023-01-31
 */
@Service
public class ProjectNotifyServiceImpl extends ServiceImpl<ProjectNotifyMapper, ProjectNotify> implements IProjectNotifyService {

 }
