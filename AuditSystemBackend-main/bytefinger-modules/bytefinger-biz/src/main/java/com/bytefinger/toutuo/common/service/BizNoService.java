package com.bytefinger.toutuo.common.service;

import com.bytefinger.common.core.constant.CacheConstants;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.toutuo.common.enums.BizNo;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 业务编号
 *
 * @author pat
 * @date 2022/10/21 10:28
 **/
@Service
@AllArgsConstructor
public class BizNoService {

    private final RedisTemplate redisTemplate;

    /**
     * 生成业务编码
     *
     * @param bizNo
     * @return
     */
    public String getNo(BizNo bizNo) {
        String dateNo = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = CacheConstants.BIZ_CODE_KEY + bizNo.getCode() + dateNo;

        Long autoId = redisTemplate.opsForValue().increment(key, 1);
        String recode = MessageFormat.format("{0}{1}{2}", bizNo.getCode(), dateNo, StringUtils.leftPad(String.valueOf(autoId), 3, "0"));

        redisTemplate.expire(key, 2, TimeUnit.DAYS);
        return recode;
    }

}
