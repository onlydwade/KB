package com.bytefinger.toutuo.biz.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.project.domain.ProjectNotifyLog;
import com.bytefinger.toutuo.biz.project.dto.ProjectNotifyDto;
import com.bytefinger.toutuo.biz.project.enums.NotifyTypeEnums;
import com.bytefinger.toutuo.biz.project.mapper.ProjectNotifyLogMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectNotifyLogService;
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
public class ProjectNotifyLogServiceImpl extends ServiceImpl<ProjectNotifyLogMapper, ProjectNotifyLog> implements IProjectNotifyLogService {

    @Override
    public IPage page(QueryPage queryPage) {
        if (ObjectUtil.isNotEmpty(queryPage.getLikeParams().get("projectName"))) {
            queryPage.getDbParams().put("projectName", queryPage.getLikeParams().get("projectName"));
        }
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get("notifyType"))) {
            queryPage.getDbParams().put("notifyType", queryPage.getParams().get("notifyType"));
        }
        IPage<ProjectNotifyLog> page = this.baseMapper.page(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
        List<ProjectNotifyLog> list = page.getRecords();
        if(ObjectUtil.isNotEmpty(list)){
            for(ProjectNotifyLog dto: list){
                dto.setNotifyTypeName(NotifyTypeEnums.getDescByCode(dto.getNotifyType()));
            }
            page.setRecords(list);
        }
        return page;
    }
}
