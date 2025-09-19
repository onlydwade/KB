package com.bytefinger.toutuo.system.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.toutuo.api.system.log.domain.SysOperLog;
import com.bytefinger.toutuo.system.log.mapper.SysOperLogMapper;
import com.bytefinger.toutuo.system.log.service.ISysOperLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {

}
