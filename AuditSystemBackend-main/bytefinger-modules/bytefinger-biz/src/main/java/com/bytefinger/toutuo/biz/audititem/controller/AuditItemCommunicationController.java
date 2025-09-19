//package com.bytefinger.toutuo.biz.audititem.controller;
//
//import com.bytefinger.common.core.web.controller.BaseController;
//import com.bytefinger.common.core.web.domain.vo.AjaxResult;
//import com.bytefinger.common.das.domain.vo.QueryPage;
//import com.bytefinger.common.log.annotation.Log;
//import com.bytefinger.common.log.enums.BusinessType;
//import com.bytefinger.toutuo.biz.audititem.domain.AuditItemCommunication;
//import com.bytefinger.toutuo.biz.audititem.service.IAuditItemCommunicationService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
///**
// * <p>
// * 审计项目（Item）交流记录 前端控制器
// * </p>
// *
// * @author Jone
// * @since 2025-08-05
// */
//@Slf4j
//@Api(tags = "审计项目（Item）交流记录")
//@AllArgsConstructor
//@RestController
//@RequestMapping("/audititemCommunication")
//public class AuditItemCommunicationController extends BaseController {
//    private final IAuditItemCommunicationService auditItemCommunicationService;
//
//    @ApiOperation(value = "新增-审计项目（Item）交流记录")
//    @PutMapping("/add")
//    @Log(title = "操作日志", businessType = BusinessType.INSERT)
//    public AjaxResult add(@RequestBody AuditItemCommunication auditItemCommunication) {
//        return success(auditItemCommunicationService.save(auditItemCommunication));
//    }
//
//    @ApiOperation(value = "修改-审计项目（Item）交流记录")
//    @PutMapping("/update")
//    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
//    public AjaxResult update(@RequestBody AuditItemCommunication auditItemCommunication) {
//        return success(auditItemCommunicationService.updateById(auditItemCommunication));
//    }
//
//    @ApiOperation(value = "删除-审计项目（Item）交流记录")
//    @DeleteMapping("/delete/{id}")
//    @Log(title = "操作日志", businessType = BusinessType.DELETE)
//    public AjaxResult delete(@PathVariable("id") Integer id) {
//        return success(auditItemCommunicationService.removeById(id));
//    }
//
//    @ApiOperation(value = "详情-审计项目（Item）交流记录")
//    @GetMapping("/get/{id}")
//    public AjaxResult get(@PathVariable("id") Integer id) {
//        return success(auditItemCommunicationService.getById(id));
//    }
//
//    @ApiOperation(value = "列表-审计项目（Item）交流记录")
//    @GetMapping("/list")
//    public AjaxResult list() {
//        return success(auditItemCommunicationService.list());
//    }
//
//    @ApiOperation(value = "分页（带条件）-审计项目（Item）交流记录")
//    @PostMapping("/page")
//    public AjaxResult page(@RequestBody QueryPage queryPage) {
//        return success(auditItemCommunicationService.page(queryPage.toPage(), queryPage.getWrapper()));
//    }
//
//}
