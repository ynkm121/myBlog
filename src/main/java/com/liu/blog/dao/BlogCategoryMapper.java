package com.liu.blog.dao;

import com.liu.blog.pojo.BlogCategory;
import com.liu.blog.utils.PageQueryUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogCategoryMapper {

    List<BlogCategory> getAllCategory();

    int getCategoryCount(PageQueryUtils utils);

    BlogCategory getCategoryById(@Param("categoryId") int id);

    BlogCategory getCategoryByName(@Param("categoryName")String name);
}
