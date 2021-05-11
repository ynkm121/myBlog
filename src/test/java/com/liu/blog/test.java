package com.liu.blog;


import com.liu.blog.dao.BlogTagMapper;
import com.liu.blog.dao.BlogTagRelationMapper;
import com.liu.blog.pojo.BlogTag;
import com.liu.blog.pojo.BlogTagRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class test {
    @Autowired
    BlogTagMapper tagMapper;
    @Autowired
    BlogTagRelationMapper relationMapper;

    @Test
    public void test(){
        LinkedList<BlogTag> blogTags = new LinkedList<>();
        BlogTag tag = new BlogTag();
        tag.setTagName("测试2");
        blogTags.add(tag);
        tagMapper.BatchInsertTags(blogTags);
        System.out.println(tag.getTagId());
    }

    @Test
    public void test2() {

    }
}
