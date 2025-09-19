package com.bytefinger.toutuo.api.biz.user.factory;

import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.user.api.RemoteUserService;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.RoleDTO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户服务降级处理
 *
 * @author patrick
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {
            @Override
            public R<LoginUser> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<UserVO> getInfo(Long id, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<SysUser> getInfoByPhone(String phone, String source) {
                return R.fail("通过手机号码获取用户信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<UserVO>> list(List<Long> ids, String source) {
                return R.fail("请稍后再试:" + throwable.getMessage());
            }

            @Override
            public R<LoginUser> authRole(Long userId, Long deptId, Long postId, String source) {
                return R.fail("切换失败请稍后再试");
            }

            @Override
            public R userSyncTask(String source) {
                return R.fail("请稍后再试:" + throwable.getMessage());
            }

        };
    }
}
