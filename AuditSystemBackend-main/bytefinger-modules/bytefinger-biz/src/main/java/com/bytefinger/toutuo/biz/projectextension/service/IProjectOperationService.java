package com.bytefinger.toutuo.biz.projectextension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperateIncome;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectOperation;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectCollectionRateVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectOperationVo;

import java.util.List;

/**
 * 拓后运营管理
 *
 * @author ycj
 * @email 
 * @date 2023-03-14 11:05:23
 */
public interface IProjectOperationService extends IService<ProjectOperation> {
    ProjectCollectionRateVo collectionRateMessage(Long id);

    ProjectOperationVo get(Long id);

    /**
     * 新增经营管理-项目收支
     *
     * @param projectOperation
     * @return
     */
    R<ProjectOperation> add(ProjectOperation projectOperation);

    /**
     * 修改经营管理-项目收支
     *
     * @param projectOperation
     * @return
     */
    R<ProjectOperation> update(ProjectOperation projectOperation);

    /**
     * 删除经营管理-项目收支
     *
     * @param id
     * @return
     */
    R deleteById(Long id);
}

