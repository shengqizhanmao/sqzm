package com.lin.common.shiro.core.Realm;

import com.alibaba.fastjson.JSON;
import com.lin.common.RedisStatus;
import com.lin.common.pojo.Resource;
import com.lin.common.pojo.Vo.UserTokenVo;
import com.lin.common.service.ResourceService;
import com.lin.common.service.UserService;
import com.lin.common.shiro.Jwt.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;

/**
 * @author lin
 * 自定义Realm
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    ResourceService resourceService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        log.info("------开始授权-------");
        UserTokenVo user = (UserTokenVo)principalCollection.getPrimaryPrincipal();
        String id = user.getId();
        String infoCache = redisTemplate.opsForValue().get(RedisStatus.INFO_TOKEN + user.getId());
        if(!StringUtils.isBlank(infoCache)){
            SimpleAuthorizationInfo simpleAuthorizationInfo = JSON.parseObject(infoCache, SimpleAuthorizationInfo.class);
//            log.info("------走了缓存-------");
//            log.info("------授权结束-------");
            return simpleAuthorizationInfo;
        }
        SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();
        List<Resource> listResourceByUserId = resourceService.getListResourceByUserId(id);
        for (Resource r : listResourceByUserId) {
            String enableFlag = r.getEnableFlag();
            if(enableFlag.equals("-1")){
                continue;
            }
            info.addStringPermission(r.getLavel());
        }
        redisTemplate.opsForValue().set(RedisStatus.INFO_TOKEN+user.getId(), JSON.toJSONString(info), Duration.ofMillis(1000*60*60*24*10));
//        log.info("------存了缓存-------");
//        log.info("------授权结束-------");
        return info;
    }

    //认证
    @Nullable
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken token=(JwtToken)authenticationToken;
        String credentials = (String)authenticationToken.getCredentials();
            UserTokenVo user = userService.findUserByToken(token.getToken());
            if(user==null){
                return null;
            }
            if(user.getEnableFlag().equals("-1")){
                throw new DisabledAccountException();
            }
            return new SimpleAuthenticationInfo(user,credentials,"UserRealm");
    }
}
