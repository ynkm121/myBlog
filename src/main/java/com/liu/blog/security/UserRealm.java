package com.liu.blog.security;

import com.liu.blog.pojo.BlogUser;
import com.liu.blog.service.impl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserServiceImpl userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了认证");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        BlogUser user = userService.getUserByName(token.getUsername());
        if(user == null){
            throw new UnknownAccountException("用户名或密码不正确");
        }
        Object originalPassword = new String((char[]) token.getCredentials());
        SimpleHash encrypedPassword = new SimpleHash("MD5", originalPassword, null, 1);
        if(!user.getPassword().equals(encrypedPassword.toString())){
            throw new UnknownAccountException("用户名或密码不正确");
        }
        return new SimpleAuthenticationInfo(user, originalPassword, "");
    }
}
