package ${package.Controller};

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
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
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@Api(tags = "${table.comment!}")
@AllArgsConstructor
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>
    private final ${table.serviceName} ${table.entityPath}Service;

    @ApiOperation(value = "新增-${table.comment!}")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody ${entity} ${table.entityPath}) {
        return success(${table.entityPath}Service.save(${table.entityPath}));
    }

    @ApiOperation(value = "修改-${table.comment!}")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody ${entity} ${table.entityPath}) {
        return success(${table.entityPath}Service.updateById(${table.entityPath}));
    }

    @ApiOperation(value = "删除-${table.comment!}")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id) {
        return success(${table.entityPath}Service.removeById(id));
    }

    @ApiOperation(value = "详情-${table.comment!}")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(${table.entityPath}Service.getById(id));
    }

    @ApiOperation(value = "列表-${table.comment!}")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(${table.entityPath}Service.list());
    }

    @ApiOperation(value = "分页（带条件）-${table.comment!}")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(${table.entityPath}Service.page(queryPage.toPage(), queryPage.getWrapper()));
    }

}
</#if>