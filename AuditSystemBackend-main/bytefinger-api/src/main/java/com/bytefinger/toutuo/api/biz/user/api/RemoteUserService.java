package com.bytefinger.toutuo.api.biz.user.api;

import com.bytefinger.common.core.constant.SecurityConstants;
import com.bytefinger.common.core.constant.ServiceNameConstants;
import com.bytefinger.common.core.domain.R;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.LoginUser;
import com.bytefinger.toutuo.api.biz.user.domain.dto.RoleDTO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.toutuo.api.biz.user.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务
 *
 * @author patrick
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.BIZ_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取用户信息
     *
     * @param id
     * @param source
     * @return
     */
    @GetMapping("/user/getInfo/{id}")
    public R<UserVO> getInfo(@PathVariable("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过手机号码获取用户信息
     *
     * @param phone
     * @param source
     * @return
     */
    @GetMapping("/user/getInfoByPhone/{phone}")
    public R<SysUser> getInfoByPhone(@PathVariable("phone") String phone, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取用户列表
     *
     * @param ids
     * @param source
     * @return
     */
    @PostMapping("/user/list")
    public R<List<UserVO>> list(@RequestBody List<Long> ids, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 判断用户是否有此角色
     *
     * @param deptId
     * @param postId
     * @param source
     * @return
     */
    @GetMapping("/user/authRole/{userId}/{deptId}/{postId}")
    public R<LoginUser> authRole(@PathVariable("userId") Long userId, @PathVariable("deptId") Long deptId, @PathVariable("postId") Long postId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 定时器，检查并自动放弃
     *
     * @param source
     * @return
     */
    @PutMapping("/mainUser/userSyncTask")
    R userSyncTask(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
