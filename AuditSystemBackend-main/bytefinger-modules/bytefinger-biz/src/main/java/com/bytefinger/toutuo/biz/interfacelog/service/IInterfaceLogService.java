package com.bytefinger.toutuo.biz.interfacelog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bytefinger.toutuo.biz.interfacelog.domain.InterfaceLog;
import com.bytefinger.toutuo.biz.oa.domain.dto.ReimbursementApplicationRespone;
import com.bytefinger.toutuo.biz.operlog.domain.OperLog;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;

import java.util.List;

/**
 * <p>
 * 接口日志记录表
 * </p>
 *
 * @author chengwei
 * @since 2023-12-12
 */
public interface IInterfaceLogService extends IService<InterfaceLog> {

    ReimbursementApplicationRespone sentReimbursementApplication(String projectId , String type);

    Integer sentReimbursementApplicationInit();
    void ResentReimbursementApplication();
}
