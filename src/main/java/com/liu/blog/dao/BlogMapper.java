package com.liu.blog.dao;

import com.liu.blog.pojo.Blog;
import com.liu.blog.utils.PageQueryUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {

    // 首页信息
    List<Blog> getBlogsList(PageQueryUtils pageUtils);

    // 文章总数
    int getBlogCounts(PageQueryUtils pageUtils);

    /*
    * 首页侧边栏列表展示
    * @Param type
    * @Value 0=hot,1=new
    */
    List<Blog> getBlogListByType(@Param("type") int type, @Param("limit") int limit);

    List<Blog> getBlogsByTagId(PageQueryUtils utils);

    Blog getBlogByPrimaryKey(@Param("blogId") int blogId);

    void updateByPrimaryKey(Blog blog);

    Blog getBlogBySubUrl(String subUrl);
}
