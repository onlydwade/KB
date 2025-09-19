package com.bytefinger.toutuo.biz.workbrief.service.impl;

import com.bytefinger.common.core.service.ThisService;
import com.bytefinger.toutuo.biz.customer.domain.vo.CustomerVO;
import com.bytefinger.toutuo.biz.workbrief.domain.KeyCustomerFollow;
import com.bytefinger.toutuo.biz.workbrief.mapper.KeyCustomerFollowMapper;
import com.bytefinger.toutuo.biz.workbrief.service.IKeyCustomerFollowService;
import com.bytefinger.toutuo.biz.workbrief.vo.KeyCustomerInfoVo;
import com.bytefinger.toutuo.common.service.BizService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 重点客户跟进情况 服务实现类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
@Service
@AllArgsConstructor
public class KeyCustomerFollowServiceImpl extends BizService<KeyCustomerFollowMapper, KeyCustomerFollow> implements IKeyCustomerFollowService, ThisService<KeyCustomerFollowServiceImpl> {

    public final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void deleteByBriefId(Long briefId) {
        this.baseMapper.deleteByBriefId(briefId);
    }

    @Override
    public List<CustomerVO> getList() {
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
        return this.baseMapper.getList(startTime,endTime);
    }

    @Override
    public List<KeyCustomerInfoVo> getListByCustomerId(Long customerId) {
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
        return this.baseMapper.getListByCustomerId(customerId,startTime,endTime);
    }
}
