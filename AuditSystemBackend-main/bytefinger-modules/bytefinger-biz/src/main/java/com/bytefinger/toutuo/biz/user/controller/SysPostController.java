package com.bytefinger.toutuo.biz.user.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytefinger.common.core.constant.UserConstants;
import com.bytefinger.common.core.utils.PingYinUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.log.annotation.Log;
import com.bytefinger.common.log.enums.BusinessType;
import com.bytefinger.common.security.annotation.RequiresPermissions;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.project.domain.ProjectTeam;
import com.bytefinger.toutuo.biz.project.service.IProjectTeamService;
import com.bytefinger.toutuo.biz.user.domain.SysPost;
import com.bytefinger.toutuo.biz.user.service.ISysMenuService;
import com.bytefinger.toutuo.biz.user.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 岗位信息操作处理
 *
 * @author pat
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class SysPostController extends BaseController {
    private ISysPostService postService;

    private ISysMenuService menuService;

    private IProjectTeamService projectTeamService;

    @ApiOperation("新增岗位")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PutMapping("/add")
    @RequiresPermissions("system:post:add")
    public AjaxResult add(@Validated @RequestBody SysPost post) {
        // 默认岗位编码字段
        post.setPostCode(PingYinUtils.getPingYin(post.getPostName()));
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(SecurityUtils.getUsername());
        return toAjax(postService.insertPost(post));
    }

    @ApiOperation("修改岗位")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @RequiresPermissions("system:post:update")
    public AjaxResult update(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return AjaxResult.error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(postService.updatePost(post));
    }

    @ApiOperation("删除岗位")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{postIds}")
    @RequiresPermissions("system:post:add")
    public AjaxResult delete(@PathVariable Long[] postIds) {
        return toAjax(postService.deletePostByIds(postIds));
    }

    @ApiOperation("根据岗位编号获取详细信息")
    @GetMapping(value = "/get/{postId}")
    public AjaxResult get(@PathVariable Long postId) {
        return AjaxResult.success(postService.selectPostById(postId));
    }

    @ApiOperation("获取岗位列表")
    @GetMapping("/listAll")
    @RequiresPermissions("system:post:list")
    public AjaxResult listAll() {
        return AjaxResult.success(postService.selectPostAll());
    }


    @ApiOperation("获取岗位列表")
    @GetMapping("/list")
    public AjaxResult list() {
        List<SysPost> sysPosts = postService.selectPostAll();
        Iterator<SysPost> iterator = sysPosts.iterator();
        while (iterator.hasNext()) {
            SysPost next = iterator.next();
            if (!next.getStatus().equals("0")) {
                iterator.remove();
            }
            //二级角色 不展示
            if (ObjectUtil.isNotEmpty(next.getType())){
                iterator.remove();
            }
        }
        return AjaxResult.success(sysPosts);
    }




    @ApiOperation("根据项目获取角色的权限集合")
    @GetMapping("/list/{projectId}")
    public AjaxResult list2(@PathVariable("projectId") Long projectId) {

        List<ProjectTeam> list = projectTeamService.list(projectId);
        if(CollectionUtils.isEmpty(list)){
            return success();
        }
        Long userId = SecurityUtils.getUserId();
        List<String> roleKey = list.stream().filter(v -> v.getUserId().equals(userId)).map(v -> v.getRoleKey()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(roleKey)){
            return success();
        }

        List<SysPost> posts = postService.selectPostListByType(roleKey);
        if(CollectionUtils.isEmpty(posts)){
            return success();
        }
        Set<String> set = new HashSet<>();
        posts.forEach(v -> {
            Set<String> _set = menuService.selectMenuPermsByPostId(v.getPostId());
            set.addAll(_set);
        });
        return AjaxResult.success(set);
    }

}
