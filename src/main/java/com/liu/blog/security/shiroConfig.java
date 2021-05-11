package com.liu.blog.security;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class shiroConfig {

//    @Bean
    public ShiroFilterFactoryBean factoryBean(@Qualifier("DefaultSecurityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        /*
            anon:无拦截
            authc：必须认证才能访问
            user：必须勾选 记住我 才能访问
            perms：拥有某个资源权限才能访问
            role：拥有角色权限才能访问
         */
        bean.setLoginUrl("/admin/login");
        bean.setUnauthorizedUrl("/admin/logout");
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        // 静态资源放行
        filterMap.put("*.js", "anon");
        filterMap.put("*.css", "anon");
        filterMap.put("/admin/dist/**", "anon");
        filterMap.put("/admin/plugins/**", "anon");
        // 权限认证
        filterMap.put("/swagger-ui.html", "authc");
        filterMap.put("/admin/login", "anon");
        filterMap.put("/admin/**", "authc");
        bean.setFilterChainDefinitionMap(filterMap);

        return bean;
    }

    @Bean(name = "DefaultSecurityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("UserRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }


    @Bean(name = "UserRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(securityManager(userRealm()));
        return bean;
    }
}
