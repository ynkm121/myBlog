package com.liu.blog.service.impl;

import com.liu.blog.dao.BlogCategoryMapper;
import com.liu.blog.pojo.BlogCategory;
import com.liu.blog.service.BlogCategoryService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {
    @Autowired
    BlogCategoryMapper categoryMapper;

    @Override
    public int getCategoryCount() {
        return categoryMapper.getCategoryCount(null);
    }

    @Override
    public List<BlogCategory> getAllCategory() {
        return categoryMapper.getAllCategory(null);
    }

    @Override
    public PageResult getCategoryPage(PageQueryUtils utils) {
        List<BlogCategory> categories = categoryMapper.getAllCategory(utils);
        int counts = categoryMapper.getCategoryCount(null);
        return new PageResult(counts, utils.getLimit(), utils.getPage(), categories);
    }

    @Override
    public boolean insertCategory(String name, String icon) {
        BlogCategory tmp = categoryMapper.getCategoryByName(name);
        if(tmp == null){
            BlogCategory category = new BlogCategory();
            category.setCategoryName(name);
            category.setCategoryIcon(icon);
            return categoryMapper.insertCategorySelective(category) > 0;
        }
        return false;
    }

    @Override
    public boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        BlogCategory tmp = categoryMapper.getCategoryByName(categoryName);
        if(tmp == null){
            BlogCategory category = categoryMapper.getCategoryById(categoryId);
            category.setCategoryName(categoryName);
            category.setCategoryIcon(categoryIcon);
            return categoryMapper.updateByPrimaryKeySelective(category) > 0;
        }
        return false;
    }

    @Override
    public boolean BatchDelete(Integer[] ids) {
        return categoryMapper.BatchDelete(ids) > 0;
    }
}
