package com.bytefinger.toutuo.api.biz.project.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.project.api.RemoteProjectService;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.biz.user.factory.RemoteUserFallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务降级处理
 *
 * @author patrick
 */
@Component
public class RemoteProjectServiceFallbackFactory implements FallbackFactory<RemoteProjectService> {


    private static final Logger log = LoggerFactory.getLogger(RemoteProjectServiceFallbackFactory.class);

    @Override
    public RemoteProjectService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteProjectService() {

            @Override
            public R sendNotifyTask(String source) {
                return R.fail("请稍后再试:" + throwable.getMessage());
            }

            @Override
            public R syncProjectCompany(String source) {
                return R.fail("请稍后再试:" + throwable.getMessage());
            }

            @Override
            public R projectDaoQiStatusChange(String source) {
                return R.fail("请稍后再试:" + throwable.getMessage());
            }

            @Override
            public R projectCircularChange(String source) {
                return R.fail("请稍后再试:" + throwable.getMessage());
            }
        };
    }
}
