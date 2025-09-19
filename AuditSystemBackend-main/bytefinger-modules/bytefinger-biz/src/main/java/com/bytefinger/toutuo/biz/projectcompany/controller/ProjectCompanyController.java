package com.bytefinger.toutuo.biz.projectcompany.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.api.biz.user.domain.SysUserDeptPost;
import com.bytefinger.toutuo.api.biz.user.domain.dto.SysUserDeptPostInfoDTO;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.project.constants.ProjectConstant;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeam;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.project.service.IProjectTeamService;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompany;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyExecutives;
import com.bytefinger.toutuo.biz.projectcompany.domain.ProjectCompanyShareholder;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyExecutivesService;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyService;
import com.bytefinger.toutuo.biz.projectcompany.service.IProjectCompanyShareholderService;
import com.bytefinger.toutuo.biz.user.service.ISysUserDeptPostService;
import com.bytefinger.toutuo.common.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 股权合作项目-公司 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-01
 */
@Slf4j
@Api(tags = "股权合作项目-公司")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projectCompany")
public class ProjectCompanyController extends BaseController {

    public final IProjectService projectService;

    private final IProjectCompanyService projectCompanyService;

    private final ISysUserDeptPostService sysUserDeptPostService;

    private final IProjectCompanyExecutivesService projectCompanyExecutivesService;

    private final IProjectCompanyShareholderService projectCompanyShareholderService;

    private final IProjectCompanyDocumentService projectCompanyDocumentService;


    @ApiOperation(value = "新增-股权合作项目-公司")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:projectCompany:add")
    public AjaxResult add(@RequestBody ProjectCompany projectCompany) {
        ProjectCompany projectCompanyOld = projectCompanyService.getOne(Wrappers.<ProjectCompany>lambdaQuery().eq(ProjectCompany::getCompanyNo, projectCompany.getCompanyNo()));
        if (ObjectUtil.isNotNull(projectCompanyOld)){
            if (!projectCompanyOld.getProjectId().equals(projectCompany.getProjectId())){
                return AjaxResult.error("统一社会信用代码存在重复");
            }
            projectCompany.setId(projectCompanyOld.getId());
            BeanUtil.copyProperties(projectCompany,projectCompanyOld);
            projectCompanyService.updateById(projectCompanyOld);
        }else {
            projectCompanyService.add(projectCompany);
        }
        //处理其他相关信息
        Project project = projectService.getById(projectCompany.getProjectId());
        if (ObjectUtil.isNotNull(project)&& ProjectConstant.GU_QUAN_HE_ZUO_XIANG_MU.equals(project.getProjectType())){
            //处理董监高人员的修改
            if (ObjectUtil.isNotNull(projectCompany.getId())){
                List<ProjectCompanyExecutives> projectCompanyExecutives = projectCompanyExecutivesService.list(new LambdaQueryWrapper<ProjectCompanyExecutives>().eq(ProjectCompanyExecutives::getProjectId, projectCompany.getProjectId()));
                for (ProjectCompanyExecutives projectCompanyExecutive : projectCompanyExecutives) {
                    projectCompanyExecutive.setCompanyId(projectCompany.getId());
                    projectCompanyExecutivesService.updateById(projectCompanyExecutive);
                }
            }

            //处理股权信息的修改
            if (ObjectUtil.isNotNull(projectCompany.getId())){
                List<ProjectCompanyShareholder> projectCompanyShareholders = projectCompanyShareholderService.list(new LambdaQueryWrapper<ProjectCompanyShareholder>().eq(ProjectCompanyShareholder::getProjectId, projectCompany.getProjectId()));
                for (ProjectCompanyShareholder projectCompanyShareholder : projectCompanyShareholders) {
                    projectCompanyShareholder.setCompanyId(projectCompany.getId());
                    projectCompanyShareholderService.updateById(projectCompanyShareholder);
                }
            }

            //处理文件更新,与企业绑定
            Map<String, Object> projectCompanyDocumentInfo = projectCompany.getProjectCompanyDocumentInfo();
            if (ObjectUtil.isNotNull(projectCompanyDocumentInfo)){
                //盖章版章程文档ID列表
                List<Long> charterDocumentIds = Convert.toList(Long.class, projectCompanyDocumentInfo.get("charterDocumentIds"));
                if (CollectionUtil.isNotEmpty(charterDocumentIds)){
                    for (Long charterDocumentId : charterDocumentIds) {
                        ProjectCompanyDocument projectCompanyDocument = new ProjectCompanyDocument();
                        projectCompanyDocument.setId(charterDocumentId);
                        projectCompanyDocument.setCompanyId(projectCompany.getId());
                        projectCompanyDocumentService.updateById(projectCompanyDocument);
                    }
                }
                //工商执照文档ID列表
                List<Long> businessLicenseDocumentIds = Convert.toList(Long.class, projectCompanyDocumentInfo.get("businessLicenseDocumentIds"));
                if (CollectionUtil.isNotEmpty(businessLicenseDocumentIds)){
                    for (Long businessLicenseDocumentId : businessLicenseDocumentIds) {
                        ProjectCompanyDocument projectCompanyDocument = new ProjectCompanyDocument();
                        projectCompanyDocument.setId(businessLicenseDocumentId);
                        projectCompanyDocument.setCompanyId(projectCompany.getId());
                        projectCompanyDocumentService.updateById(projectCompanyDocument);
                    }

                }
                //其他文件文档ID列表
                List<Long> otherDocumentIds = Convert.toList(Long.class, projectCompanyDocumentInfo.get("otherDocumentIds"));
                if (CollectionUtil.isNotEmpty(otherDocumentIds)){
                    for (Long otherDocumentId : otherDocumentIds) {
                        ProjectCompanyDocument projectCompanyDocument = new ProjectCompanyDocument();
                        projectCompanyDocument.setId(otherDocumentId);
                        projectCompanyDocument.setCompanyId(projectCompany.getId());
                        projectCompanyDocumentService.updateById(projectCompanyDocument);
                    }
                }
            }
        }
        return success(projectCompany);
    }

    @ApiOperation(value = "修改-股权合作项目-公司")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:projectCompany:update")
    public AjaxResult update(@RequestBody ProjectCompany projectCompany) {
        if (projectCompanyService.count(Wrappers.<ProjectCompany>lambdaQuery()
                .eq(ProjectCompany::getCompanyNo, projectCompany.getCompanyNo())
                .ne(ProjectCompany::getId, projectCompany.getId())) > 0) {
            return AjaxResult.error("统一社会信用代码存在重复");
        }
        return projectCompanyService.update(projectCompany).toAjaxResult();
    }

    @ApiOperation(value = "删除-股权合作项目-公司")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:projectCompany:delete")
    public AjaxResult delete(@PathVariable("id") Long id) {
        return success(projectCompanyService.removeById(id));
    }

    @ApiOperation(value = "详情-股权合作项目-公司")
    @GetMapping("/get/{id}")
    @RequiresPermissions("biz:projectCompany:get")
    public AjaxResult get(@PathVariable("id") Long id) {
        return success(projectCompanyService.getByCompanyId(id));
    }

    @ApiOperation(value = "列表-股权合作项目-公司")
    @GetMapping("/list/{projectId}")
    @RequiresPermissions("biz:projectCompany:list")
    public AjaxResult list(@PathVariable("projectId") Long projectId) {
        return success(projectCompanyService.list(Wrappers.<ProjectCompany>lambdaQuery().eq(ProjectCompany::getProjectId, projectId).orderByDesc(ProjectCompany::getCreateTime)));
    }
    @ApiOperation(value = "分页（带条件）-股权合作项目-公司")
    @PostMapping("/page")
    @RequiresPermissions("biz:projectCompany:page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(projectCompanyService.page(queryPage));
    }

    @ApiOperation(value = "导出-项目数据")
    @PostMapping("/export")
    public void export(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        IPage<ProjectCompany> page = projectCompanyService.page(queryPage);
//        if(CollectionUtils.isEmpty(page.getRecords())){
//            page.getRecords().forEach(v -> {
//                v.setPaidCapitalStatus(v.getPaidCapital().compareTo(BigDecimal.ZERO) > 0 ? ShiFou.SHI.getCode() : ShiFou.FOU.getCode());
//            });
//        }
        com.bytefinger.toutuo.common.util.ExcelUtil<ProjectCompany> excelUtil = new ExcelUtil<>(ProjectCompany.class);
        excelUtil.exportExcel(ServletUtils.getResponse(), page.getRecords(), "子公司数据");
    }

}
