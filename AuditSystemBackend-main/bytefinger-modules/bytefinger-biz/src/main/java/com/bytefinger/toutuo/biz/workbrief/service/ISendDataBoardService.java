package com.bytefinger.toutuo.biz.workbrief.service;


import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * 数据看板推送 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
public interface ISendDataBoardService {

    void sendTodo(Set<String> userIds, Integer year, Long deptId);

}
