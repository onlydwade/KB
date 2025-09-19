package com.bytefinger.toutuo.system.core.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.utils.CamelUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.system.core.domain.SysMessage;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.system.core.mapper.SysMessageMapper;
import com.bytefinger.toutuo.system.core.service.ISysMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统个人消息 前端控制器
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
@Slf4j
@Api(tags = "消息")
@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class SysMessageController extends BaseController {

    private final ISysMessageService sysMessageService;

    private final SysMessageMapper sysMessageMapper;

    @ApiOperation(value = "新增-系统个人消息", hidden = true)
    @PutMapping("/add")
    public R<SysMessage> add(@RequestBody SysMessage sysMessage) {
        sysMessageService.save(sysMessage);
        return R.ok(sysMessage);
    }

    @ApiOperation(value = "系统个人消息打开")
    @PutMapping("/open/{id}")
    public AjaxResult open(@PathVariable("id") Integer id) {
        sysMessageService.update(Wrappers.<SysMessage>lambdaUpdate()
                .eq(SysMessage::getId, id)
                .set(SysMessage::getOpenStatus, 1)
                .set(SysMessage::getOpenTime, new Date()));
        return success("更新成功");
    }

    @ApiOperation(value = "详情-系统个人消息")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        SysMessage message = sysMessageService.getOne(Wrappers.<SysMessage>lambdaQuery()
                .eq(SysMessage::getId, id));
        return success(message);
    }

    @ApiOperation(value = "分页（带条件）-系统个人消息")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUserid();
        queryPage.getParams().put(CamelUtils.camelToUndeline("userId"), userId);
        IPage<SysMessage> page = sysMessageService.page(queryPage.toPage(), queryPage.getWrapper());
        return success(page);
    }


    @ApiOperation(value = "标记消息已读")
    @PostMapping("/markMessage")
    public AjaxResult markMessage(@RequestBody List<Long> ids) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUserid();

        List<SysMessage> sysMessageList = new ArrayList<>();

        //全部标记已读,传空,表示全部标记已读
        if (CollectionUtil.isEmpty(ids)) {
            //查询当前用户所有未读
            List<SysMessage> list = sysMessageService.list(Wrappers.<SysMessage>lambdaQuery().eq(SysMessage::getOpenStatus, 0).eq(SysMessage::getUserId, userId));
            for (SysMessage message : list) {
                SysMessage sysMessage = new SysMessage();
                sysMessage.setId(message.getId());
                sysMessage.setOpenStatus(1);
                sysMessageList.add(sysMessage);
            }

            sysMessageService.updateBatchById(sysMessageList);

        } else {
            //选中的id
            for (Long id : ids) {
                SysMessage sysMessage = new SysMessage();
                sysMessage.setId(id);
                sysMessage.setOpenStatus(1);
                sysMessageList.add(sysMessage);
            }

            sysMessageService.updateBatchById(sysMessageList);
        }


        return success();
    }


    @ApiOperation(value = "统计当前用户未读消息")
    @GetMapping("/countMessage")
    public AjaxResult countMessage() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUserid();
        int count = sysMessageService.count(Wrappers.<SysMessage>lambdaQuery().eq(SysMessage::getOpenStatus, 0).eq(SysMessage::getUserId, userId));
        return success(count);
    }


}
