package com.liu.blog.service.impl;

import com.liu.blog.dao.BlogTagMapper;
import com.liu.blog.pojo.BlogTag;
import com.liu.blog.pojo.BlogTagCount;
import com.liu.blog.service.BlogTagService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogTagServiceImpl implements BlogTagService {
    @Autowired
    BlogTagMapper blogTagMapper;

    @Override
    public List<BlogTagCount> getIndexTagCount() {
        return blogTagMapper.getTagsForIndex();
    }

    @Override
    public int getTagCount() {
        return blogTagMapper.getTagCounts(null);
    }

    @Override
    public PageResult getTagPage(PageQueryUtils utils) {

        List<BlogTag> tags = blogTagMapper.getTagsList(utils);
        int counts = blogTagMapper.getTagCounts(null);
        return new PageResult(counts, utils.getLimit(), utils.getPage(), tags);
    }

    @Override
    public boolean insertTag(String tagName) {
        BlogTag tmp = blogTagMapper.getTagByName(tagName);
        if(tmp == null){
            BlogTag tag = new BlogTag();
            tag.setTagName(tagName);
            tag.setCreateTime(new Date());
            return blogTagMapper.insertSelective(tag) > 0;
        }
        return false;
    }

    @Override
    public boolean BatchDelete(Integer[] ids) {

        return blogTagMapper.BatchDelete(ids) > 0;
    }

}
