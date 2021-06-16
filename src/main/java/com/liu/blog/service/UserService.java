package com.liu.blog.service;

import com.liu.blog.pojo.BlogUser;

public interface UserService {

    BlogUser login(String userName, String password);

    BlogUser getUserByName(String userName);

    BlogUser getUserById(int userId);

    boolean updateName(Integer loginUserId, String loginUserName, String nickName);

    boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);
}
