package com.liu.blog.dao;

import com.liu.blog.pojo.BlogUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    BlogUser login(String userName, String passwordMD5);

    BlogUser getUserByName(String userName);

    BlogUser getUserById(int userId);

    int updateByPrimaryKeySelective(BlogUser user);
}
