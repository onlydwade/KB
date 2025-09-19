package com.bytefinger.toutuo.biz.followlog.domain;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytefinger.toutuo.biz.customer.domain.CustomerFollowLogDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowLogListVo {
    private IPage logPage;
    private List<CustomerFollowLogDetail> customerFollowLogDetailList;
}
