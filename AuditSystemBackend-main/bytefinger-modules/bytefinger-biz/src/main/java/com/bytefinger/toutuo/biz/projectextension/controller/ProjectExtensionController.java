package com.bytefinger.toutuo.biz.projectextension.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.enums.Deleted;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.core.web.domain.vo.ProjectStepMenuVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainBm;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.biz.external.service.IIntMainBmService;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.bytefinger.toutuo.biz.project.constants.ProjectConstant;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentTemplateService;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansion;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectStepExpansionMenu;
import com.bytefinger.toutuo.biz.project.service.IProjectBacklogService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExtensionService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionMenuService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectStepExpansionService;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryTotalVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectQueryVo;
import com.bytefinger.toutuo.biz.projectextension.vo.ProjectTotalVo;
import com.bytefinger.toutuo.biz.projectstep.constants.ProjectStepConstant;
import com.bytefinger.toutuo.biz.projectstep.enums.ProjectApprovalStatus;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 拓后管理续签状态
 *
 * @author ycj
 * @email 
 * @date 2023-03-09
 */
@Api(tags = "拓后管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/projectExtension")
public class ProjectExtensionController extends BaseController {

    @Autowired(required = false)
    private IProjectExtensionService projectExtensionService;

    public final IProjectService projectService;

    private final IProjectStepExpansionService projectStepExpansionService;

    private final IProjectStepExpansionMenuService projectStepExpansionMenuService;

    private final IOperLogService operLogService;

    private final ISysDeptService deptService;

    private final ISysUserService userService;

    private final IIntMainBmService intMainBmService;

    private final IAuditDocumentTemplateService projectDocumentTemplateService;

    private final IAuditDocumentService projectDocumentService;

    @Autowired
    private IProjectBacklogService projectBacklogService;


    @ApiOperation(value = "拓展管理-在管项目库")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectExtension:list")
    public AjaxResult page(@RequestBody QueryPage queryPage) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        String postCode = loginUser.getPostCode();
        if (StringUtils.isNotEmpty(postCode) && postCode.equals("GONG_SHANG_ZHU_CE_/_BIAN_GENG_WEI_HU_YUAN")){
            queryPage.getParams().put("projectType","GU_QUAN_HE_ZUO_XIANG_MU");
        }
        IPage<Project> result = projectExtensionService.projectPage(queryPage);

        List<Project> projects = result.getRecords();
        result.setRecords(projectExtensionService.projectExpandQueryVos(projects));

        return success(result);
    }

    @ApiOperation(value = "拓展管理-在管项目库导出")
    @PostMapping("/export")
    @RequiresPermissions("biz:projectExtension:export")
    public void export(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage<Project> page = projectExtensionService.projectPage(queryPage);

        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        com.bytefinger.toutuo.common.util.ExcelUtil<Project> excelUtil = new ExcelUtil<>(Project.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "拓前在管项目库项目数据");
    }


    @ApiOperation(value = "拓展管理-续签重新投标列表")
    @PostMapping("/renewBidPage")
    @RequiresPermissions("biz:projectExtension:list")
    public AjaxResult renewBidPage(@RequestBody QueryPage queryPage) {
        IPage<Project> page = projectExtensionService.renewBidPage(queryPage);
        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        List<ProjectQueryVo> projectQueryVos = projectExtensionService.renewBidPage(page.getRecords());

        IPage<ProjectQueryVo> projectQueryVoIPage = new Page<>();
        BeanUtils.copyProperties(page, projectQueryVoIPage);
        projectQueryVoIPage.setRecords(projectQueryVos);
        return success(projectQueryVoIPage);
    }

    @ApiOperation(value = "拓展管理-续签重新投标列表")
    @PostMapping("/renewBidPageExport")
    @RequiresPermissions("biz:projectExtension:list")
    public void renewBidPageExport(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage<Project> page = projectExtensionService.renewBidPage(queryPage);
        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        com.bytefinger.toutuo.common.util.ExcelUtil<Project> excelUtil = new ExcelUtil<>(Project.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "续签重新投标项目数据");
    }


    @ApiOperation(value = "拓后运营-在管项目库")
    @PostMapping("/extensionPage")
    @RequiresPermissions("biz:projectExtension:list")
    public AjaxResult extensionPage(@RequestBody QueryPage queryPage) {
        IPage<Project> page = projectExtensionService.extensionProjectPage(queryPage);

        page.setRecords(projectService.getYtProjetList(page.getRecords()));
        List<ProjectQueryVo> projectQueryVos = projectExtensionService.page(page.getRecords());

        IPage<ProjectQueryVo> projectQueryVoIPage = new Page<>();
        BeanUtils.copyProperties(page, projectQueryVoIPage);
        projectQueryVoIPage.setRecords(projectQueryVos);

        return success(projectQueryVoIPage);
    }

    @ApiOperation(value = "拓后运营-在管项目库")
    @PostMapping("/extensionExport")
    @RequiresPermissions("biz:projectExtension:list")
    public void extensionExport(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage<Project> page = projectExtensionService.extensionProjectPage(queryPage);

        page.setRecords(projectService.getYtProjetList(page.getRecords()));

        List<ProjectQueryVo> projectQueryVos = projectExtensionService.page(page.getRecords());

        IPage<ProjectQueryVo> projectQueryVoIPage = new Page<>();
        BeanUtils.copyProperties(page, projectQueryVoIPage);
        projectQueryVoIPage.setRecords(projectQueryVos);

        com.bytefinger.toutuo.common.util.ExcelUtil<ProjectQueryVo> excelUtil = new ExcelUtil<>(ProjectQueryVo.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), projectQueryVos, "拓后运营项目数据");
    }


    @ApiOperation(value = "拓展管理-同步功能")
    @GetMapping("/syncOperationProject/{id}")
    @RequiresPermissions("biz:projectExtension:syncOperationProject")
    public AjaxResult syncOperationProject(@PathVariable("id") Long id) {
        String msg = projectExtensionService.syncOperationProject(id);
        if (StringUtils.isEmpty(msg))
            return success();

        return error(msg);
    }


    @ApiOperation(value = "统计")
    @PostMapping("/total")
    @RequiresPermissions("biz:projectExtension:total")
    public AjaxResult total(@RequestBody QueryPage queryPage) {

        ProjectQueryTotalVo projectQueryTotalVo = projectExtensionService.total(queryPage);

        return success(projectQueryTotalVo);
    }

    @ApiOperation(value = "详情-项目基础资料")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectExtension:get")
    public AjaxResult get(@PathVariable("id") Long id) {
        Project project = projectService.getById(id);
        project = projectService.getYtProjet(project);
        List<ProjectQueryVo> projectQueryVos = projectExtensionService.details(project);
        ProjectQueryVo projectQueryVo =  projectQueryVos.get(0);
        if(ProjectConstant.GU_QUAN_HE_ZUO_XIANG_MU.equals(project.getProjectType())){
            return success(projectQueryVo);
        }
        if(null == project.getContractAmount() || null == project.getProposedServicePeriod()){
            return success(projectQueryVo);
        }
        projectQueryVo.setAnnualConversionAmount(this.projectService.getCalculateAnnualConversion(project));
        return success(projectQueryVo);

    }

    @ApiOperation(value = "回退-项目")
    @GetMapping("/rollbackProject/{id}")
    public AjaxResult rollbackProject(@PathVariable("id") Long id) {
        return projectExtensionService.rollbackProject(id).toAjaxResult();
    }

    @ApiOperation(value = "续签-项目")
    @PutMapping("/saveRenewal")
    public AjaxResult saveRenewal(@RequestBody Project project) {
        return projectExtensionService.saveRenewal(project).toAjaxResult();
    }

    @ApiOperation(value = "续签项目")
    @GetMapping("/renew/{id}")
    @RequiresPermissions("biz:projectExtension:renew")
    @Log(title = "操作日志", businessType = BusinessType.OTHER)
    public AjaxResult renew(@PathVariable("id") Long id) {
        projectExtensionService.renew(id);
        return success();
    }

    @ApiOperation(value = "重新投标")
    @GetMapping("/newBid/{id}")
    @RequiresPermissions("biz:projectExtension:newBid")
    @Log(title = "操作日志", businessType = BusinessType.OTHER)
    public AjaxResult newBid(@PathVariable("id") Long id) {
        projectExtensionService.newBid(id);
        return success();
    }


    private void recursion(List<ProjectStepMenuVO> list, Map<Long, ProjectStepExpansion> stepMap){
        if(CollectionUtils.isEmpty(list) || list.size() == 0) return;
        list.forEach(v -> {
            ProjectStepExpansion projectStep = stepMap.get(v.getId());
            v.setStatus(ObjectUtils.isEmpty(projectStep) ? ProjectStepConstant.PROJECT_STEP_UNDONE : projectStep.getStatus());
            v.setApprovalStatus(ObjectUtils.isEmpty(projectStep) ? ProjectApprovalStatus.WEI_FA_QI_SHEN_PI.getCode() : projectStep.getApprovalStatus());
//            v.setStatus(ObjectUtils.isEmpty(projectStep) ? ProjectStepConstant.PROJECT_STEP_DONE : projectStep.getStatus());
            recursion(v.getChildren(), stepMap);
        });
    }


    @ApiOperation(value = "节点树-拓后项目步骤菜单配置表")
    @GetMapping("/treeByProjectId/{projectId}")
    @RequiresPermissions("biz:projectExtension:list")
    public AjaxResult treeByProjectId(@PathVariable("projectId") Long projectId) {
        List<ProjectStepExpansionMenu> list = projectExtensionService.findProjectEXtensionStepMenuBy();
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        List<Long> menuIds = list.stream().map(v -> v.getId()).collect(Collectors.toList());
        List<ProjectStepMenuVO> tree = projectStepExpansionMenuService.tree(Wrappers.<ProjectStepExpansionMenu>lambdaQuery()
                .in(ProjectStepExpansionMenu::getId, menuIds).orderByAsc(ProjectStepExpansionMenu ::getSorts));

        //组装节点完成状态
        List<ProjectStepExpansion> steps = projectStepExpansionService.listByProjectId(projectId);
        Map<Long, ProjectStepExpansion> stepMap = steps.stream().collect(Collectors.toMap(ProjectStepExpansion::getStepMenuId, Function.identity(), (key1, key2) -> key2));
        recursion(tree, stepMap);
        return success(tree);
    }





    @ApiOperation(value = "分页查询用户信息")
    @PostMapping("/userPage")
    public AjaxResult userPage(@RequestBody QueryPage queryPage) {

        // 存在部门id则查询当前部门下所有用户
        List<Long> childDeptIds = org.apache.commons.compress.utils.Lists.newArrayList();
        if (queryPage.getParams().containsKey("deptId")) {
            Integer deptId = (Integer) queryPage.getParams().get("deptId");
            if (null != deptId && deptId > 0) {
                queryPage.getParams().remove("deptId");
                childDeptIds.addAll(this.deptService.getChildDeptIds(Integer.toUnsignedLong(deptId)));
            }
        }

        QueryWrapper<SysUser> wrapper = queryPage.getWrapper();
        wrapper.lambda().ne(SysUser::getUserId, 1).eq(SysUser::getDelFlag, Deleted.NO.getCode());
        if (!childDeptIds.isEmpty()) {
            wrapper.lambda().in(SysUser::getDeptId, childDeptIds);
        }

        Page<SysUser> page = userService.page(queryPage.toPage(), wrapper);
        List<Long> deptIds = page.getRecords().stream().map(SysUser::getDeptId).collect(Collectors.toList());

        Boolean companyBool = false;
        if (!deptIds.isEmpty()) {
            List<SysDept> list = deptService.list(Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, deptIds));

            if (!list.isEmpty()) {
                for (SysUser record : page.getRecords()) {
                    for (SysDept sysDept : list) {
                        if (sysDept.getDeptId().equals(record.getDeptId())) {
                            List<SysDept> listNew = deptService.list(Wrappers.<SysDept>lambdaQuery().in(SysDept::getParentId, sysDept.getDeptId()));
                            ////如果id有下级，则是公司，无需部门数据
                            if (!ObjectUtils.isEmpty(listNew)){
                                record.setCompanyId(sysDept.getDeptId());
                                record.setCompanyName(sysDept.getDeptName());
                            }else {
                                //是部门，查询上级公司
                                record.setDeptName(sysDept.getDeptName());
                                List<SysDept> listDeptNew = deptService.list(Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, sysDept.getParentId()));
                                if (!ObjectUtils.isEmpty(listDeptNew)){
                                    record.setCompanyId(sysDept.getDeptId());
                                    record.setCompanyName(listDeptNew.get(0).getDeptName());
                                }

                            }
                        }
                    }
                }
            }
        }

        if (companyBool){

        }

        List<String> mainDeptIds = page.getRecords().stream().map(SysUser::getMainDeptId).collect(Collectors.toList());

        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(mainDeptIds)) {
            List<IntMainBm> list = intMainBmService.list(Wrappers.<IntMainBm>lambdaQuery().in(IntMainBm::getCode, mainDeptIds));

            if (!list.isEmpty()) {
                for (SysUser record : page.getRecords()) {
                    for (IntMainBm bm : list) {
                        if (null != bm.getCode() && bm.getCode().equals(record.getMainDeptId())) {
                            record.setMainDeptName(bm.getDesc2());
                        }
                    }
                }
            }
        }

        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(page.getRecords())) {
            for (SysUser record : page.getRecords()) {
                record.setRealname(record.getRealname() + "(" + record.getUserName() + ")");
            }
        }

        return success(page);
    }


    @ApiOperation(value = "在管项目统计")
    @PostMapping("/projectTotal")
    @RequiresPermissions("biz:projectExtension:projectTotal")
    public AjaxResult projectTotal(@RequestBody QueryPage queryPage) {
        //QueryPage queryPage=new QueryPage();
        ProjectTotalVo projectTotalVo = projectExtensionService.projectTotal(queryPage);
        return success(projectTotalVo);
    }

}
