package com.bytefinger.toutuo.biz.external.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.InnerAuth;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainBm;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.biz.external.domain.IntMainHr;
import com.bytefinger.toutuo.biz.external.domain.IntMainUser;
import com.bytefinger.toutuo.biz.external.service.IIntMainBmService;
import com.bytefinger.toutuo.biz.external.service.IIntMainHrService;
import com.bytefinger.toutuo.biz.external.service.IIntMainUserService;
import com.bytefinger.toutuo.biz.user.domain.DeptMainDept;
import com.bytefinger.toutuo.biz.user.service.IDeptMainDeptService;
import com.bytefinger.toutuo.biz.user.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 主数据人员表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-13
 */
@Slf4j
@Api(tags = "主数据人员表")
@AllArgsConstructor
@RestController
@RequestMapping("/mainUser")
public class IntMainUserController extends BaseController {
    private final IIntMainUserService intMainUserService;

    private final IDeptMainDeptService deptMainDeptService;

    private final IIntMainBmService intMainBmService;

    private final IIntMainHrService intMainHrService;

    private final ISysUserService userService;

    @ApiOperation(value = "详情-主数据人员表")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(intMainUserService.getById(id));
    }

    @ApiOperation(value = "列表-主数据人员表")
    @GetMapping("/list")
    public AjaxResult list() {
        return success(intMainUserService.list());
    }

    @ApiOperation(value = "分页（带条件）-主数据人员表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(intMainUserService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation(value = "", hidden = true)
    @PutMapping("/userSyncTask")
    @InnerAuth
    public R userSyncTask() {
        QueryPage queryPage = new QueryPage();
        queryPage.setPageSize(100);
        Page<IntMainUser> page = intMainUserService.page(queryPage.toPage(), Wrappers.<IntMainUser>lambdaQuery().eq(IntMainUser::getTodo, 1));
        List<IntMainUser> list = page.getRecords();
        if(CollectionUtils.isEmpty(list) || list.size() == 0){
            return R.ok();
        }

        list.forEach(v -> {
            SysUser user = bulidUser(v);
            boolean result = userService.update(user, Wrappers.<SysUser>lambdaQuery().eq(SysUser::getMainUserId, v.getDesc1()));
            if(!result){
                userService.save(user);
            }
        });
        List<String> ids = list.stream().map(v -> v.getDesc1()).collect(Collectors.toList());
        IntMainUser mainUser = new IntMainUser();
        mainUser.setTodo(0);
        intMainUserService.update(mainUser, Wrappers.<IntMainUser>lambdaQuery().eq(IntMainUser::getTodo, 1).in(IntMainUser::getDesc1, ids));
        return R.ok();
    }

    private SysUser bulidUser(IntMainUser mainUser) {
        SysUser user = new SysUser();
        List<IntMainHr> hrs = intMainHrService.list(Wrappers.<IntMainHr>lambdaQuery().eq(IntMainHr::getDesc1, mainUser.getCodeId()));
        if(!CollectionUtils.isEmpty(hrs) && hrs.size() > 0){
            List<IntMainBm> list = intMainBmService.list(Wrappers.<IntMainBm>lambdaQuery().eq(IntMainBm::getDesc1, hrs.get(0).getDesc6()));
            if(!CollectionUtils.isEmpty(list) || list.size() > 0){
                DeptMainDept mainDept = deptMainDeptService.getOne(Wrappers.<DeptMainDept>lambdaQuery().eq(DeptMainDept::getMainDeptId, list.get(0).getCode()));
                if(!ObjectUtils.isEmpty(mainDept)){
                    user.setDeptId(mainDept.getDeptId());
                    user.setMainDeptId(mainDept.getMainDeptId());
                }
            }

        }
        user.setUserName(mainUser.getDesc5());
        user.setRealname(mainUser.getDesc4());
        user.setUserType("00");
        user.setEmail(mainUser.getDesc50());
        user.setPhonenumber(mainUser.getDesc47());
        if("01".equals(mainUser.getDesc9())){
            user.setSex("0");
        }else if("02".equals(mainUser.getDesc9())){
            user.setSex("1");
        }else{
            user.setSex("2");
        }
        user.setPassword("-----");
        user.setStatus("0");
        user.setDelFlag("0");
        user.setMainUserId(mainUser.getDesc1());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return user;
    }
}
