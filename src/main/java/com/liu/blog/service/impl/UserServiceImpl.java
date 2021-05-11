package com.liu.blog.service.impl;

import com.liu.blog.dao.UserMapper;
import com.liu.blog.pojo.BlogUser;
import com.liu.blog.service.UserService;
import com.liu.blog.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public BlogUser login(String userName, String password) {
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        return userMapper.login(userName, passwordMD5);
    }

    @Override
    public BlogUser getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    @Override
    public BlogUser getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        BlogUser user = userMapper.getUserById(loginUserId);
        if(user != null){
            user.setUserName(loginUserName);
            user.setNickName(nickName);
            if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        BlogUser user = userMapper.getUserById(loginUserId);
        //当前用户非空才可以进行更改
        if (user != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            //比较原密码是否正确
            if (originalPasswordMd5.equals(user.getPassword())) {
                //设置新密码并修改
                user.setPassword(newPasswordMd5);
                if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                    //修改成功则返回true
                    return true;
                }
            }
        }
        return false;

    }
}
