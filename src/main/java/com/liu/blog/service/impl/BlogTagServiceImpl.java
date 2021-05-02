package com.liu.blog.service.impl;

import com.liu.blog.dao.BlogTagMapper;
import com.liu.blog.pojo.BlogTagCount;
import com.liu.blog.service.BlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogTagServiceImpl implements BlogTagService {
    @Autowired
    BlogTagMapper blogTagMapper;

    @Override
    public List<BlogTagCount> getIndexTagCount() {
        return blogTagMapper.getTagsForIndex();
    }
}
