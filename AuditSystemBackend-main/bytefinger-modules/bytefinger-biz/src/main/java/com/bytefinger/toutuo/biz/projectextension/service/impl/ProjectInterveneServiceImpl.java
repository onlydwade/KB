package com.bytefinger.toutuo.biz.projectextension.service.impl;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.enums.ShiFou;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.core.web.domain.vo.DeptVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeam;
import com.bytefinger.toutuo.biz.project.mapper.ProjectTeamMapper;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectAssess;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectIntervene;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectRiskManagement;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectAssessLabel;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectNotify;
import com.bytefinger.toutuo.biz.projectextension.mapper.ProjectInterveneMapper;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectAssessService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectInterveneService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessSuggestVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectAssessVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectInterveneVo;
import com.bytefinger.toutuo.biz.user.service.UserService;
import com.bytefinger.toutuo.common.constants.MessageTypeConstant;
import com.bytefinger.toutuo.common.enums.ModuleType;
import com.bytefinger.toutuo.common.service.BizService;
import com.bytefinger.toutuo.common.service.SendMessageService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProjectInterveneServiceImpl extends BizService<ProjectInterveneMapper, ProjectIntervene> implements IProjectInterveneService {

    @Autowired
    private IProjectAssessService projectAssessService;

    @Autowired
    private  ProjectInterveneMapper projectInterveneMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private IProjectService projectService;

    private final SendMessageService sendMessageService;

    private final ProjectTeamMapper projectTeamMapper;

    /**
     * 查询全部数据
     * 拼装用户信息
     *
     * @param queryWrapper
     * @return
     */
    @DataFill
    @Override
    public List<ProjectIntervene> listQuery(LambdaQueryWrapper<ProjectIntervene> queryWrapper) {
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult add(ProjectIntervene projectIntervene) {
        List<ProjectIntervene> projectRiskManagements = super.list(Wrappers.<ProjectIntervene>lambdaQuery().eq(ProjectIntervene::getInterveneSchemeName, projectIntervene.getInterveneSchemeName()).eq(ProjectIntervene::getProjectId, projectIntervene.getProjectId()));
        if (projectRiskManagements.size() > 0) {
            return R.fail("该方案已经存在").toAjaxResult();
        }
        if (ObjectUtils.isNotEmpty(projectIntervene.getAssessId())){
            //有关联的项目评估，修改状态
            this.updateAssessStrte(projectIntervene);
        }
        if (ObjectUtils.isNotEmpty(projectIntervene.getSchemeUserId())){
            //项目干预，干预待执行消息通知
            sendMessageService.sendMessage(projectIntervene.getSchemeUserId(), projectIntervene.getProjectId(), ProjectNotify.Interven.getCode(),
                    ProjectNotify.Interven.getDesc(), ModuleType.PROJECT_EXTEXSION.getCode(), "XI_TONG_TONG_ZHI" + "," + "DAI_BAN_TONG_ZHI");
        }
        projectIntervene.setSchemeIsnotEmbodiment(0);
        projectIntervene.setExamineLevel(0);
        projectIntervene.setInterveneState(0);
        return R.ok(super.add4Log(projectIntervene)).toAjaxResult();
    }


    private void updateAssessStrte(ProjectIntervene projectIntervene){
        //方案下达后，修改项目评估是否已下达干预方案状态为是
        ProjectAssess  projectAssess =  projectAssessService.getById(projectIntervene.getAssessId());
//        ProjectAssess projectAssess = new ProjectAssess();
        projectAssess.setId(projectIntervene.getAssessId());
        projectAssess.setTransmitAlreadyState(1);
        projectAssessService.updateById(projectAssess);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult update(ProjectIntervene projectIntervene) {
        List<ProjectIntervene> projectRiskManagements = super.list(Wrappers.<ProjectIntervene>lambdaQuery().eq(ProjectIntervene::getInterveneSchemeName, projectIntervene.getInterveneSchemeName()).eq(ProjectIntervene::getProjectId, projectIntervene.getProjectId()).notIn(ProjectIntervene::getId, Lists.newArrayList(projectIntervene.getId())));
        if (projectRiskManagements.size() > 0) {
            return R.fail("该方案已经存在").toAjaxResult();
        }
        projectIntervene.setFeedbackTime(new Date());
        projectIntervene.setSchemeIsnotEmbodiment(2);
        projectIntervene.setExamineLevel(0);
        Project project = projectService.getById(projectIntervene.getProjectId());
        if (ObjectUtils.isNotEmpty(projectIntervene.getProjectId())){
            List<Long> ids = new ArrayList<>();
            //如果归属单位
            if (ObjectUtils.isNotEmpty(project.getCompanyId()))
            {
                DeptVO deptVO = new DeptVO();
                deptVO.setDeptId(project.getCompanyId());
                List<UserVO>  userVOS = userService.getUserPrincipalByDeptId(deptVO);
                if (ObjectUtils.isNotEmpty(userVOS) && userVOS.size()>0){
                    ids.addAll(userVOS.stream().filter(v->ObjectUtils.isNotEmpty(v.getUserId())).map(v->v.getUserId()).collect(Collectors.toList()));
                }
            }
            //如果归属单位没有负责人，直接给总部负责人发送消息
            if (ObjectUtils.isEmpty(ids)){
                DeptVO deptVO = new DeptVO();
                deptVO.setDeptName("宝石花总部");
                List<UserVO>  userVOS = userService.getUserPrincipalByDeptId(deptVO);
                if (ObjectUtils.isNotEmpty(userVOS) && userVOS.size()>0){
                    //如果直接给总部发送的审批，无需经过二次通过
//                    projectIntervene.setExamineLevel(1);
                    ids.addAll(userVOS.stream().map(v->v.getUserId()).collect(Collectors.toList()));
                }
            }
            //项目干预，干预待检查消息通知
            ids.forEach(v -> {
            sendMessageService.sendMessage(v, projectIntervene.getProjectId(), ProjectNotify.IntervenExamineStair.getCode(),
                    ProjectNotify.IntervenExamineStair.getDesc(), ModuleType.PROJECT_EXTEXSION.getCode(), "XI_TONG_TONG_ZHI" + "," + "DAI_BAN_TONG_ZHI");
            });
        }
        return R.ok(super.updateExtension4Log(projectIntervene,projectIntervene.getProjectId())).toAjaxResult();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public R<ProjectIntervene> isNotPass(ProjectIntervene projectIntervene) {
        ProjectIntervene projectInterveneOne = this.getById(projectIntervene.getId());
        //执行不通过 给实施人发消息
        if (projectIntervene.getInterveneState()==2){
            if (ObjectUtils.isNotEmpty(projectInterveneOne.getSchemeUserId())){
                //项目干预，给方案实施人干预不通过消息通知
                sendMessageService.sendMessage(projectInterveneOne.getSchemeUserId(), projectInterveneOne.getProjectId(), ProjectNotify.NotPass.getCode(),
                        ProjectNotify.NotPass.getDesc(), ModuleType.PROJECT_EXTEXSION.getCode(), "XI_TONG_TONG_ZHI" + "," + "DAI_BAN_TONG_ZHI");
            }
        }else {
            List<Long> ids = new ArrayList<>();
            String title= "";
            String message ="";
            if (projectInterveneOne.getExamineLevel()== 0){
                //执行通过 一级通过 状态不变 给总部负责人发二级检查提示
                projectIntervene.setInterveneState(null);
                //第一级检查通过
                projectIntervene.setExamineLevel(1);
                projectIntervene.setInterveneState(1);
                title = ProjectNotify.IntervenExaminesecond.getCode();
                message = ProjectNotify.IntervenExaminesecond.getDesc();
                UserVO userVO = new UserVO();
                userVO.setUserId(SecurityUtils.getUserId());
                List<UserVO>  userVOS = userService.getUserPrincipalId(userVO);
                //如果当前人是宝石花总部运营管理部负责人
                if (ObjectUtils.isNotEmpty(userVOS) && userVOS.size()>0){
                    //二级通过
                    title = ProjectNotify.Pass.getCode();
                    message = ProjectNotify.Pass.getDesc();
                    projectIntervene.setExamineLevel(2);
                    projectIntervene.setInterveneState(3);
                    ids.add(projectInterveneOne.getSchemeUserId());
                    projectIntervene.setCloseTime(new Date());
                }else {
                    DeptVO deptVO = new DeptVO();
                    deptVO.setDeptName("宝石花总部");
                    List<UserVO>  userPrincipaVOS = userService.getUserPrincipalByDeptId(deptVO);
                    if (ObjectUtils.isNotEmpty(userPrincipaVOS) && userPrincipaVOS.size()>0){
                        ids.addAll(userPrincipaVOS.stream().map(v->v.getUserId()).collect(Collectors.toList()));
                    }
                }

            }else {
                //执行通过 二级通过 给方案实施人干预通过消息通知
                title = ProjectNotify.Pass.getCode();
                message = ProjectNotify.Pass.getDesc();
                ids.add(projectInterveneOne.getSchemeUserId());
                //二级通过
                projectIntervene.setExamineLevel(2);
                projectIntervene.setInterveneState(3);
                projectIntervene.setCloseTime(new Date());
            }

            String finalTitle = title;
            String finalMessage = message;
            ids.forEach(v -> {
                sendMessageService.sendMessage(v, projectInterveneOne.getProjectId(), finalTitle, finalMessage, ModuleType.PROJECT_EXTEXSION.getCode(), "XI_TONG_TONG_ZHI" + "," + "DAI_BAN_TONG_ZHI");
            });
        }
        return R.ok(super.updateExtension4Log(projectIntervene,projectInterveneOne.getProjectId()));
    }

    @DataFill
    @Override
    public IPage<ProjectIntervene> page(QueryPage queryPage,Long stepMenuId){
        IPage<ProjectIntervene> page = this.page(queryPage.toPage(), queryPage.getWrapper());
        for (ProjectIntervene projectIntervene : page.getRecords()){
            if (ObjectUtils.isNotEmpty(projectIntervene.getAssessId())){
                projectIntervene.setProjectAssess(projectAssessService.getById(projectIntervene.getAssessId()));
            }

        }
        super.dataProjectFillDcoument(stepMenuId,Lists.newArrayList(page.getRecords()),null);
        return page;
    }

    @DataFill
    @Override
    public IPage<Project> projectPage(QueryPage queryPage) {
        Map map = queryPage.getParams();
        String interveneState = "intervene.interveneState";
        if (ObjectUtil.isNotEmpty(queryPage.getParams().get(interveneState))) {
            queryPage.getDbParams().put("interveneState", queryPage.getParams().get(interveneState));
            queryPage.getParams().remove(interveneState);
        }
        if (ObjectUtils.isNotEmpty(map.get("isNotTransmitInterveneScheme"))){
            LambdaQueryWrapper<ProjectIntervene> queryWrapper = new LambdaQueryWrapper<ProjectIntervene>();

            List<ProjectIntervene> list = this.list(queryWrapper);
            if (ObjectUtils.isNotEmpty(map.get("isNotTransmitInterveneScheme"))){

               if (map.get("isNotTransmitInterveneScheme").equals(ShiFou.FOU.getCode())){
                   queryPage.getNotInParams().put("project.id",list.stream().map(v->v.getProjectId()).collect(Collectors.toList()));
               }else {
                   queryPage.getInParams().put("project.id",list.stream().map(v->v.getProjectId()).collect(Collectors.toList()));
               }
                queryPage.getParams().remove("isNotTransmitInterveneScheme");
            }
        }
        String serviceContent = "serviceContent";
        if (ObjectUtil.isNotEmpty(queryPage.getInParams().get(serviceContent))) {
            queryPage.getDbParams().put("serviceContent", queryPage.getInParams().get(serviceContent));
            queryPage.getInParams().remove(serviceContent);
        }
        return projectInterveneMapper.list(queryPage.toPage(), queryPage.getWrapper(), queryPage.getDbParams());
    }

    @Override
    public List<ProjectInterveneVo> listPage(List<Project> projects){
        List<ProjectInterveneVo> projectInterveneVos = new ArrayList<>();
        LambdaQueryWrapper<ProjectIntervene> queryWrapper = new LambdaQueryWrapper<ProjectIntervene>();
        queryWrapper.in(ProjectIntervene::getProjectId, projects.stream().map(v -> v.getId()).collect(Collectors.toList()));
        List<ProjectIntervene> list = this.list(queryWrapper);
        list.forEach(v -> {if (v.getId()!=null) {v.setCreateUser(userService.getUser(v.getCreateUserId()));}});
        for (Project project : projects) {
            //
            ProjectIntervene projectIntervene = list.stream().filter(a -> a.getProjectId().equals(project.getId())).max(Comparator.comparing(ProjectIntervene::getCreateTime)).orElse(new ProjectIntervene());
            ProjectInterveneVo projectInterveneVo = new ProjectInterveneVo();
            projectInterveneVo.setIsNotTransmitInterveneScheme(ShiFou.FOU.getCode());
            BeanUtils.copyProperties(project, projectInterveneVo);
            if (ObjectUtils.isNotEmpty(projectIntervene.getId())){
                this.getProInterveneVo(projectIntervene,projectInterveneVo);
            }
            //当前登入人是否是项目归属人、拓后负责人、项目团队成员、创建人
            //项目团队成员
            List<ProjectTeam> projectTeamList =  projectTeamMapper.selectList(Wrappers.<ProjectTeam>lambdaQuery().eq(ProjectTeam::getRecordId, project.getId()).eq(ProjectTeam::getUserId, SecurityUtils.getUserId()));
            if((ObjectUtils.isNotEmpty(project.getAttributorUserId()) && project.getAttributorUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getPrincipalId()) && project.getPrincipalId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(project.getCreateUserId()) && project.getCreateUserId().equals(SecurityUtils.getUserId())) || (ObjectUtils.isNotEmpty(projectTeamList) && projectTeamList.size()>0)){
                projectInterveneVo.setShow(true);
            }else {
                projectInterveneVo.setShow(false);
            }
            projectInterveneVos.add(projectInterveneVo);
        }
        return projectInterveneVos;
    }

    @Override
    @DataFill
    public ProjectIntervene get(Long stepMenuId, Long id) {
        ProjectIntervene projectIntervene = this.getById(id);

        if (ObjectUtils.isNotEmpty(projectIntervene.getAssessId())){
            projectIntervene.setAssessTitle(projectAssessService.getById(projectIntervene.getAssessId()).getAssessTitle());
        }
        super.dataProjectFillDcoument(stepMenuId, Lists.newArrayList(projectIntervene),null);
        return projectIntervene;
    }

    private ProjectInterveneVo getProInterveneVo(ProjectIntervene projectIntervene,ProjectInterveneVo projectInterveneVo){
        projectInterveneVo.setTransmitSchemeTime(projectIntervene.getCreateTime());
        projectInterveneVo.setIsNotTransmitInterveneScheme(ShiFou.SHI.getCode());
        projectInterveneVo.setInterveneSchemeName(projectIntervene.getInterveneSchemeName());
        projectInterveneVo.setTransmitSchemeTime(projectIntervene.getCreateTime());
        projectInterveneVo.setTransmitSchemeUser(projectIntervene.getCreateUser());
        projectInterveneVo.setInterveneState(projectIntervene.getInterveneState());
        projectInterveneVo.setTransmitSchemeUser(projectIntervene.getCreateUser());
        return projectInterveneVo;
    }
}