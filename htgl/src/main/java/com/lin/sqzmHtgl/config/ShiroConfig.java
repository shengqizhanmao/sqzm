package com.lin.sqzmHtgl.config;

import com.lin.sqzmHtgl.shiro.Jwt.JwtFilter;
import com.lin.sqzmHtgl.shiro.core.Realm.SUserRealm;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @Description 权限配置类
 */
@Configuration
@ComponentScan(basePackages = {"com.lin.sqzmHtgl"})
@Log4j2
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean(value = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager
            ){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //设置我们自定义的JWT过滤器
        Map<String, Filter> map = new HashMap<>();
        map.put("jwt",new JwtFilter());
        shiroFilterFactoryBean.setFilters(map);
        //anon游客,authc登录,user,perms资源,role角色
        Map<String,String> filterMap=new LinkedHashMap<>();
        //资源
        filterMap.put("/static/**","anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/doc.html", "anon");
        filterMap.put("/v2/**", "anon");
        //不经过jwt拦截器就可以的
        filterMap.put("/login/**","anon");
        //全部请求经过jwt拦截
        filterMap.put("/**","jwt");
//        filterMap.put("/user/add","perms[user:add]");
//        filterMap.put("/user/**","perms[user:*]");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        shiroFilterFactoryBean.setLoginUrl("/login/LoginUrl");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login/Unauthorized");
        return shiroFilterFactoryBean;
    }

    //安全管理器
    @Bean(value = "defaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(
            @Qualifier("sUserRealm")SUserRealm sUserRealm,
            @Qualifier("defaultSubjectDAO")DefaultSubjectDAO defaultSubjectDAO
    ){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        //关联Realm
        defaultWebSecurityManager.setRealm(sUserRealm);
        //关联Session,关闭session
       defaultWebSecurityManager.setSubjectDAO(defaultSubjectDAO);
        return defaultWebSecurityManager;
    }

    //Realm
    @Bean(value = "sUserRealm")
    public SUserRealm getUserRealm(){
        return new SUserRealm();
    }

    //session
    @Bean(value = "defaultSubjectDAO")
    public DefaultSubjectDAO defaultSubjectDAO(@Qualifier("defaultSessionStorageEvaluator")DefaultSessionStorageEvaluator defaultSessionStorageEvaluator) {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        return subjectDAO;
    }
    @Bean(value = "defaultSessionStorageEvaluator")
    public DefaultSessionStorageEvaluator defaultSessionStorageEvaluator() {
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        return defaultSessionStorageEvaluator;
    }
    //aop
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor
                = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
