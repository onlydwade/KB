package com.bytefinger.toutuo.system.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.toutuo.api.system.core.domain.SysMessage;
import com.bytefinger.toutuo.system.core.mapper.SysMessageMapper;
import com.bytefinger.toutuo.system.core.service.ISysMessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统个人消息 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-10
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
