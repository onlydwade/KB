package com.bytefinger.toutuo.biz.keyword.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.toutuo.biz.keyword.domain.Keywords;
import com.bytefinger.toutuo.biz.keyword.service.IKeywordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 业务关键词 前端控制器 d
 * </p>
 *
 * @author patrick
 * @since 2022-10-19
 */
@Slf4j
@Api(tags = "关键词")
@AllArgsConstructor
@RestController
@RequestMapping("/keyword")
public class KeywordsController extends BaseController {

    private final IKeywordsService keywordsService;

    @ApiOperation(value = "新增-业务关键词")
    @PutMapping("/add")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("biz:keyword:add")
    public AjaxResult add(@RequestBody Keywords keywords) {
        keywordsService.save(keywords);
        return success(keywords);
    }

    @ApiOperation(value = "修改-业务关键词")
    @PutMapping("/update")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @RequiresPermissions("biz:keyword:update")
    public AjaxResult update(@RequestBody Keywords keywords) {
        keywordsService.updateById(keywords);
        return success(keywords);
    }

    @ApiOperation(value = "删除-业务关键词")
    @DeleteMapping("/delete/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("biz:keyword:delete")
    public AjaxResult delete(@PathVariable("id") Integer id) {
        keywordsService.removeById(id);
        return success(true);
    }

    @ApiOperation(value = "列表-业务关键词")
    @GetMapping("/list/{type}")
    @RequiresPermissions("biz:keyword:list")
    public AjaxResult list(@PathVariable("type") Integer type) {
        List<Keywords> keywordsList = keywordsService.list(Wrappers.<Keywords>lambdaQuery().eq(Keywords::getKeywordType, type).orderByAsc(Keywords::getSorts));
        return success(keywordsList);
    }

}
