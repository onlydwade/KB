package com.bytefinger.toutuo.biz.status.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.toutuo.biz.status.domain.SysStatus;
import com.bytefinger.toutuo.biz.status.mapper.SysStatusMapper;
import com.bytefinger.toutuo.biz.status.service.ISysStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 状态配置基础表 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-08
 */
@Service
@AllArgsConstructor
public class SysStatusServiceImpl extends ServiceImpl<SysStatusMapper, SysStatus> implements ISysStatusService {

    private final SysStatusMapper sysStatusMapper;

    /**
     * 通过group id 查询列表
     *
     * @param groupId
     * @return
     */
    @Override
    public List<SysStatus> listByGroupId(Integer groupId) {
        return sysStatusMapper.listByGroupId(groupId);
    }

}
