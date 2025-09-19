package com.bytefinger.toutuo.biz.workbrief.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.toutuo.api.biz.user.domain.SysDept;
import com.bytefinger.toutuo.biz.user.service.ISysDeptService;
import com.bytefinger.toutuo.biz.workbrief.domain.RecentProjectUndertaking;
import com.bytefinger.toutuo.biz.workbrief.mapper.RecentProjectUndertakingMapper;
import com.bytefinger.toutuo.biz.workbrief.service.IRecentProjectUndertakingService;
import com.bytefinger.toutuo.common.service.BizService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 近期项目承接情况 服务实现类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Service
@RequiredArgsConstructor
public class RecentProjectUndertakingServiceImpl extends BizService<RecentProjectUndertakingMapper, RecentProjectUndertaking> implements IRecentProjectUndertakingService, ThisService<RecentProjectUndertakingServiceImpl> {

    public final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public final ISysDeptService iSysDeptService;

//    @Value("${dept.headquartersCode}")
//    private Long headquartersCode;

    @Override
    public void deleteByBriefId(Long briefId) {
        this.baseMapper.deleteByBriefId(briefId);
    }

    @Override
    public List<RecentProjectUndertaking> getListByBriefId(Long briefId) {
        return this.baseMapper.getListByBriefId(briefId);
    }

    @DataFill
    @Override
    public List<RecentProjectUndertaking> getList(QueryPage queryPage) {
        // 近两周时间运算
        String startTime = StringUtils.EMPTY;
        String endTime = StringUtils.EMPTY;
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(13);
        LocalDateTime startOfSevenDaysAgo = sevenDaysAgo.atTime(0, 0, 0);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern(dateTimeFormat);
        startTime = startOfSevenDaysAgo.format(formatterStart);
        LocalDateTime endOfSevenDaysAgo = today.atTime(23,59,59);
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern(dateTimeFormat);
        endTime = endOfSevenDaysAgo.format(formatterEnd);
        // 汇报单位id
        Long companyId = 100L;
        List<Long> companyIds = Lists.newArrayList();
        Map<String, Object> params = queryPage.getParams();
        if (ObjectUtil.isNotEmpty(params)){
            companyId = Long.valueOf(params.get("companyId").toString());
            companyIds.add(companyId);
            SysDept byId = iSysDeptService.getById(companyId);
            // 迭代二需求：如果选的机构是部门的话，都要读取“宝石花总部”的数据 || 如果选的机构是总部数据的话，要加载全公司的数据
//            if(ObjectUtil.equals(byId.getDeptType(),"BU_MEN") || ObjectUtil.equals(companyId,headquartersCode)){
//                companyIds = null;
//            }
            // 迭代二需求：如果选的公司，则加载公司下面所有的项目
            if(ObjectUtil.equals(byId.getLevel(),2) && !ObjectUtil.equals(byId.getDeptType(),"BU_MEN")){
                LambdaQueryWrapper<SysDept> deptQw = new LambdaQueryWrapper<>();
                deptQw.eq(SysDept::getParentId,companyId);
                deptQw.eq(SysDept::getDelFlag,0);
                List<SysDept> deptList = iSysDeptService.list(deptQw);
                if(ObjectUtil.isNotEmpty(deptList)){
                    companyIds.addAll(deptList.stream().map(SysDept::getDeptId).collect(Collectors.toList()));
                }
            }
        }
        return this.baseMapper.getList(queryPage.getWrapper(),queryPage.getDbParams(),startTime,endTime,companyIds);
    }
}
