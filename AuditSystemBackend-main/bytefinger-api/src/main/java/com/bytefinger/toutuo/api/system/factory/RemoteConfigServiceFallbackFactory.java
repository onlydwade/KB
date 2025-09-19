package com.bytefinger.toutuo.api.system.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.biz.user.factory.RemoteUserFallbackFactory;
import com.bytefinger.toutuo.api.system.config.api.RemoteConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 服务降级处理
 *
 * @author patrick
 */
@Component
public class RemoteConfigServiceFallbackFactory implements FallbackFactory<RemoteConfigService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteConfigService create(Throwable throwable) {
        log.error("更新配置服务调用失败:{}", throwable.getMessage());
        return new RemoteConfigService() {
            @Override
            public R<LoginUser> set( String configKey,  String confiVal) {
                return R.fail("更新配置失败:" + throwable.getMessage());
            }


        };
    }
}
