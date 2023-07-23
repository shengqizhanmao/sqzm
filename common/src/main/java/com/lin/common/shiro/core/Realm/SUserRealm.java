package com.lin.common.shiro.core.Realm;

import com.alibaba.fastjson.JSON;
import com.lin.common.RedisStatus;
import com.lin.common.pojo.Resource;
import com.lin.common.pojo.Vo.SUserTokenVo;
import com.lin.common.service.ResourceService;
import com.lin.common.service.SUserService;
import com.lin.common.shiro.Jwt.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.time.Duration;
import java.util.List;

/**
 * @author lin
 * 自定义Realm
 */
@Slf4j
public class SUserRealm extends AuthorizingRealm {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SUserService sUserService;
    @Autowired
    private ResourceService resourceService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(@NotNull PrincipalCollection principalCollection) {
//        log.info("------开始授权-------");
        SUserTokenVo sUser = (SUserTokenVo) principalCollection.getPrimaryPrincipal();
        String id = sUser.getId();
        String infoCache = redisTemplate.opsForValue().get(RedisStatus.INFO_TOKEN + sUser.getId());
        if (!StringUtils.isBlank(infoCache)) {
            //            log.info("------走了缓存-------");
//            log.info("------授权结束-------");
            return JSON.parseObject(infoCache, SimpleAuthorizationInfo.class);
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<Resource> listResourceBySUserId = resourceService.getListResourceBySUserId(id);
        for (Resource r : listResourceBySUserId) {
            String enableFlag = r.getEnableFlag();
            if (enableFlag.equals("-1")) {
                continue;
            }
            info.addStringPermission(r.getLavel());
        }
        redisTemplate.opsForValue().set(RedisStatus.INFO_TOKEN + sUser.getId(), JSON.toJSONString(info), Duration.ofMillis(1000 * 60 * 60 * 24 * 10));
//        log.info("------存了缓存-------");
//        log.info("------授权结束-------");
        return info;
    }

    //请求认证
    @Nullable
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(@NotNull AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken token = (JwtToken) authenticationToken;
        String credentials = (String) authenticationToken.getCredentials();
        SUserTokenVo sUser = sUserService.findSUserByToken(token.getToken());
        if (sUser == null) {
            return null;
//            throw new AuthenticationException();
        }
        if (sUser.getEnableFlag().equals("-1")) {
            throw new DisabledAccountException();
        }
        return new SimpleAuthenticationInfo(sUser, credentials, "SUserRealm");
    }
}
