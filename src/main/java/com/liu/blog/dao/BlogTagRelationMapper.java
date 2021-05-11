package com.liu.blog.dao;

import com.liu.blog.pojo.BlogTagRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface BlogTagRelationMapper {
    int BatchInsertRelation(List<BlogTagRelation> relations);

    int deleteByBlogId(@Param("blogId") Long blogId);
}
