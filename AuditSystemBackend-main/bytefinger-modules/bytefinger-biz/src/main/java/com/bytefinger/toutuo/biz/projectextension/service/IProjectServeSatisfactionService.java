package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectServeSatisfaction;

import java.util.Map;

/**
 * 拓后运营-服务满意度信息
 *
 * @author ycj
 * @email 
 * @date 2023-03-20 11:40:08
 */
public interface IProjectServeSatisfactionService extends IService<ProjectServeSatisfaction> {
    /**
     * 新增服务满意度信息
     *
     * @param projectServeSatisfaction
     * @return
     */
    R<ProjectServeSatisfaction> add(ProjectServeSatisfaction projectServeSatisfaction);

    /**
     * 修改服务满意度信息
     *
     * @param projectServeSatisfaction
     * @return
     */
    R<ProjectServeSatisfaction> update(ProjectServeSatisfaction projectServeSatisfaction);

    /**
     * 删除服务满意度信息
     *
     * @param id
     * @return
     */
    R deleteById(Long id);
}

