package com.bytefinger.toutuo.biz.operlog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.biz.operlog.domain.OperLog;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;
import com.bytefinger.toutuo.biz.operlog.mapper.OperLogMapper;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务数据变更记录表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-12
 */
@Service
@AllArgsConstructor
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements IOperLogService {

    private final RemoteUserService remoteUserService;

    @Override
    public void ruleLogBySystem(String modelName, Long dataId, UpdateLog updateLog) {
        OperLog operLog = new OperLog();
        operLog.setRecordId(dataId);
        operLog.setModuleName(modelName);
        operLog.setUpdateUserId(1L);
        operLog.setUpdateUserName("系统");

        operLog.setUpdateLog(JSON.toJSONString(updateLog));
        this.save(operLog);
    }

    @Override
    public void changeLog(String modelName, Long dataId, String log) {
        OperLog operLog = new OperLog();
        operLog.setRecordId(dataId);
        operLog.setModuleName(modelName);
        operLog.setUpdateDeptId(SecurityUtils.getLoginUser().getDeptId());
        operLog.setUpdatePostId(SecurityUtils.getLoginUser().getPostId());
        operLog.setUpdateLog(JSON.toJSONString(Lists.newArrayList(UpdateLog.builder().fieldName(log).build())));
        this.save(operLog);
    }

    @Override
    public void changeLog(Class clazz, Long dataId, String fieldName, String valueBefore, String valueAfter) {
        try {
            OperLog operLog = new OperLog();
            operLog.setRecordId(dataId);
            operLog.setModuleName(clazz.getSimpleName());
            operLog.setUpdateDeptId(SecurityUtils.getLoginUser().getDeptId());
            operLog.setUpdatePostId(SecurityUtils.getLoginUser().getPostId());

            operLog.setUpdateLog(JSON.toJSONString(Lists.newArrayList(UpdateLog.builder()
                    .fieldName(fieldName)
                    .valueBefore(null == valueBefore ? "" : valueBefore)
                    .valueAfter(null == valueAfter ? "" : valueAfter)
                    .build())));

            this.save(operLog);

        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<OperLog> listByModuleAndRecordId(String moduleName, Long recordId) {
        List<OperLog> olList = super.list(Wrappers.<OperLog>lambdaQuery()
                .eq(OperLog::getModuleName, moduleName)
                .eq(OperLog::getRecordId, recordId)
                .orderByDesc(OperLog::getUpdateTime));

        List<Long> userIds = olList.stream().map(OperLog::getUpdateUserId).collect(Collectors.toList());

        // 拼装数据
        R<List<UserVO>> result = remoteUserService.list(userIds, SecurityConstants.INNER);
        if (result.isSuccess()) {
            for (OperLog operLog : olList) {
                operLog.setUpdateUser(
                        result.getData().stream().filter(u -> u.getUserId().equals(operLog.getUpdateUserId())).findFirst().orElse(null)
                );
            }
        }

        return olList;
    }

    @DataFill
    @Override
    public IPage<OperLog> pageData(IPage<OperLog> page, Wrapper<OperLog> queryWrapper) {
        return super.page(page, queryWrapper);
    }

}
