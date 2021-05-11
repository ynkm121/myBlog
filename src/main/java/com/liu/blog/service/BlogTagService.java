package com.liu.blog.service;

import com.liu.blog.pojo.BlogTagCount;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;

import java.util.List;

public interface BlogTagService {
    List<BlogTagCount> getIndexTagCount();

    int getTagCount();

    PageResult getTagPage(PageQueryUtils utils);

    boolean insertTag(String tagName);

    boolean BatchDelete(Integer[] ids);
}
