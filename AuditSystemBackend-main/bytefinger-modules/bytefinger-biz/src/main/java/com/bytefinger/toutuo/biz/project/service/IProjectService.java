package com.bytefinger.toutuo.biz.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.biz.performance.domain.vo.ProjectListStepVo;
import com.bytefinger.toutuo.biz.performance.domain.vo.ProjectOpenMarkExportVo;
import com.bytefinger.toutuo.biz.performance.domain.vo.SelectCommonVo;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.dto.SaveClosureInfoDto;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目（立项）基础信息 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-10-06
 */
public interface IProjectService extends IService<Project> {

    /**
     * 转项目
     * 1、新增项目
     * 2、新增参与人
     * 3、新增参与团队
     * 4、初始化项目状态
     * 5、记录日志
     *
     * @param project
     * @return
     */
    R<Project> add(Project project);

    /**
     * 转项目
     * 1、新增项目
     * 2、新增参与人
     * 3、新增参与团队
     * 4、初始化项目状态
     * 5、记录日志
     *
     * @param project
     * @return
     */
    R<Project> add2(Project project);

    /**
     * 修改项目基础资料
     *
     * @param project
     * @return
     */
    R<Project> update(Project project);


    Project getProjectExpand(Project project);

    /**
     * 设置项目评审重点关注项目选项枚举中文返回,后续可以使用其他*
     * @param project
     * @param DictDataTypeList
     * @return
     */
    Project getEnumCodeToStr(Project project,List<String> DictDataTypeList);


    //R<Boolean> saveClosureInfo(SaveClosureInfoDto saveClosureInfoDto);

    SaveClosureInfoDto getClosureInfo(Long id);

    /**
     * 查询全部数据
     * 拼装用户信息
     *
     * @param queryPage
     * @return
     */
    IPage<Project> page(QueryPage queryPage);



    /**
     * 查询全部数据
     * 拼装用户信息
     *
     * @param queryPage
     * @return
     */
    IPage<Project> projectPageFromCustomer(QueryPage queryPage);

    List<Project> getYtProjetList(List<Project> projectList);

    Project getYtProjet(Project project);
    /**
     * 变更归属人
     *
     * @param id
     * @param dispatchUserId
     * @return
     */
    Project changeAttributorUser(Long id, Long dispatchUserId);


    Project changeBidingAmount(Long projectId, String bidingAmount);

    Project changeExpire(Long projectId, String expireReason);

    Project changeClose(Project project);

    Project changeServiceStatus(Long projectId, String serviceStatus);

    void projectDaoQiStatusChange();

    void projectDaoqiChange();

    void projectCircularChange();

    void syncProjectCompany();
    /**
     * 转项目
     * 1、克隆项目
     * 4、初始化项目状态
     * 5、记录日志
     *
     * @param project
     * @return
     */
    R<Project> clone(Project project);

    IPage pageTwo(QueryPage queryPage);

    /**
     * 分页查询项目通告
     *
     * @param queryPage 传参
     * @return
     */
    IPage pageNotify(QueryPage queryPage);

    /**
     * 分页查询项目丢盘监控
     *
     * @param queryPage 传参
     * @return
     */
    IPage pageEndNotify(QueryPage queryPage);

    /**
     * 将40天未维护（最后更新时间40天没改过），并且项目评审未完成或者没有到项目评审这个节点的项目，标记失效
     *
     * @return
     */
    void handleExpireProject();

    /**
     * 将项目标记为有效
     *
     * @return
     */
    void updateProjectValid(Long id);

    List<SelectCommonVo> getProjectStepMenuList();

    List<ProjectOpenMarkExportVo> getOpenMarkList(QueryPage queryPage);

    IPage<Project> pageAll(QueryPage queryPage);

    R<List<Project>> matchProjectSimilarity(Project project);

    BigDecimal getCalculateAnnualConversion(Project project);

    BigDecimal calculateAnnualConversion(BigDecimal contractAmount, Integer servicePeriod, Date serviceBeginTime, Date serviceEndTime, Date performanceConfirmTime);
}
