package com.liu.blog.service;

import com.liu.blog.dao.BlogCategoryMapper;
import com.liu.blog.pojo.BlogCategory;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BlogCategoryService {

    int getCategoryCount();

    List<BlogCategory> getAllCategory();

    PageResult getCategoryPage(PageQueryUtils utils);

    boolean insertCategory(String name, String icon);

    boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    boolean BatchDelete(Integer[] ids);
}
