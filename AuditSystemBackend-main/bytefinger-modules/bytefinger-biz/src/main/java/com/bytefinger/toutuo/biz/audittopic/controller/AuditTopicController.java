package com.bytefinger.toutuo.biz.audittopic.controller;

import com.bytefinger.toutuo.biz.audittopic.service.IAuditTopicService;
import com.bytefinger.toutuo.biz.audittopic.domain.AuditTopic;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.bytefinger.common.core.web.controller.BaseController;

/**
 * <p>
 * 话题表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2025-08-27
 */
@Slf4j
@Api(tags = "话题表")
@AllArgsConstructor
@RestController
@RequestMapping("/auditTopic")
public class AuditTopicController extends BaseController {
    private final IAuditTopicService auditTopicService;

    @ApiOperation(value = "新增-话题表")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody AuditTopic auditTopic) {
        return success(auditTopicService.save(auditTopic));
    }

    @ApiOperation(value = "新增-话题表")
    @PutMapping("/batchAdd")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult batchAdd(@RequestBody List<AuditTopic> auditTopics) {
        auditTopicService.saveBatch(auditTopics);
        return success(auditTopics);
    }

    @ApiOperation(value = "修改-话题表")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody AuditTopic auditTopic) {
        return success(auditTopicService.updateById(auditTopic));
    }

    @ApiOperation(value = "删除-话题表")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(auditTopicService.removeById(id));
    }

    @ApiOperation(value = "详情-话题表")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(auditTopicService.getById(id));
    }

    @ApiOperation(value = "列表-话题表")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(auditTopicService.list());
    }

    @ApiOperation(value = "分页（带条件）-话题表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(auditTopicService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
