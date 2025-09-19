//package com.bytefinger.toutuo.biz.auditdocument.controller;
//
//import com.bytefinger.common.core.web.controller.BaseController;
//import com.bytefinger.common.core.web.domain.vo.AjaxResult;
//import com.bytefinger.common.das.domain.vo.QueryPage;
//import com.bytefinger.common.log.annotation.Log;
//import com.bytefinger.common.log.enums.BusinessType;
//import com.bytefinger.toutuo.biz.auditdocument.domain.ProjectDocumentMenu;
//import com.bytefinger.toutuo.biz.auditdocument.service.IProjectDocumentMenuService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
///**
// * <p>
// * 文件菜单 前端控制器
// * </p>
// *
// * @author Jone
// * @since 2023-01-31
// */
//@Slf4j
//@Api(tags = "文件菜单")
//@AllArgsConstructor
//@RestController
//@RequestMapping("/projectDocumentMenu")
//public class ProjectDocumentMenuController extends BaseController {
//    private final IProjectDocumentMenuService projectDocumentMenuService;
//
//    @ApiOperation(value = "新增-文件菜单")
//    @PutMapping("/add")
//    @Log(title = "操作日志", businessType = BusinessType.INSERT)
//    public AjaxResult add(@RequestBody ProjectDocumentMenu projectDocumentMenu) {
//        projectDocumentMenuService.save(projectDocumentMenu);
//        return success(projectDocumentMenu);
//    }
//
//    @ApiOperation(value = "修改-文件菜单")
//    @PutMapping("/update")
//    @Log(title = "操作日志", businessType = BusinessType.UPDATE)f
//    public AjaxResult update(@RequestBody ProjectDocumentMenu projectDocumentMenu) {
//        projectDocumentMenuService.updateById(projectDocumentMenu);
//        return success(projectDocumentMenu);
//    }
//
//    @ApiOperation(value = "删除-文件菜单")
//    @DeleteMapping("/delete/{id}")
//    @Log(title = "操作日志", businessType = BusinessType.DELETE)
//    public AjaxResult delete(@PathVariable("id") Integer id) {
//        return success(projectDocumentMenuService.removeById(id));
//    }
//
//    @ApiOperation(value = "详情-文件菜单")
//    @GetMapping("/get/{id}")
//    public AjaxResult get(@PathVariable("id") Integer id) {
//        return success(projectDocumentMenuService.getById(id));
//    }
//
//    @ApiOperation(value = "分页（带条件）-文件菜单")
//    @PostMapping("/page")
//    public AjaxResult page(@RequestBody QueryPage queryPage) {
//        return success(projectDocumentMenuService.page(queryPage.toPage(), queryPage.getWrapper()));
//    }
//
//}
