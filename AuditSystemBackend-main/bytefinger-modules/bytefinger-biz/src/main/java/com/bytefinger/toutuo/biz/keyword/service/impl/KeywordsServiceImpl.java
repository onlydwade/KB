package com.bytefinger.toutuo.biz.keyword.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.toutuo.biz.keyword.domain.Keywords;
import com.bytefinger.toutuo.biz.keyword.mapper.KeywordsMapper;
import com.bytefinger.toutuo.biz.keyword.service.IKeywordsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务关键词 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-10-19
 */
@Service
public class KeywordsServiceImpl extends ServiceImpl<KeywordsMapper, Keywords> implements IKeywordsService {

}
