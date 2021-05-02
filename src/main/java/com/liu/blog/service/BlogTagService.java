package com.liu.blog.service;

import com.liu.blog.pojo.BlogTagCount;

import java.util.List;

public interface BlogTagService {
    List<BlogTagCount> getIndexTagCount();
}
