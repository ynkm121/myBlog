package com.liu.blog.dao;

import com.liu.blog.pojo.BlogComment;
import com.liu.blog.utils.PageQueryUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BlogCommentMapper {

    List<BlogComment> getComment(Map<String, Object> param);

    int insertSelective(BlogComment record);

    int getCommentCount(Map<String, Object> param);
}
