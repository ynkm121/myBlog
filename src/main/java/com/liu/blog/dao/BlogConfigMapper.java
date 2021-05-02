package com.liu.blog.dao;

import com.liu.blog.pojo.BlogConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BlogConfigMapper {

    List<BlogConfig> selectAll();

    int updateConfig(BlogConfig config);
}
