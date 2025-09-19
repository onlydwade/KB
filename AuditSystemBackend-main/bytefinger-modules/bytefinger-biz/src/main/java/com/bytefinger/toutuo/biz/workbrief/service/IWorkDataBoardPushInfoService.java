package com.bytefinger.toutuo.biz.workbrief.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.workbrief.domain.SendDataBoard;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkDataBoardPushInfo;
import com.bytefinger.toutuo.biz.workbrief.domain.WorkDataBoardPushUser;
import com.bytefinger.toutuo.biz.workbrief.dto.WorkPushUserDto;

import java.util.List;


/**
 * <p>
 * 工作简报推送对象 服务类
 * </p>
 *
 * @author lin
 * @since 2023-01-03
 */
public interface IWorkDataBoardPushInfoService extends IService<WorkDataBoardPushInfo> {

    void sendDataBoard(SendDataBoard sendDataBoard);

    List<WorkDataBoardPushInfo> getSendList();
}
