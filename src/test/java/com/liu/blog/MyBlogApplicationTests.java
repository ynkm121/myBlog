package com.liu.blog;

import com.liu.blog.dao.*;
import com.liu.blog.pojo.*;
import com.liu.blog.service.BlogConfigService;
import com.liu.blog.service.BlogService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MyBlogApplicationTests {

    @Autowired
    BlogMapper blogMapper;
    @Test
    void contextLoads() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 9);
        map.put("tagId", 133);
        map.put("blogStatus", 1);
        PageQueryUtils utils = new PageQueryUtils(map);
//        int blogCounts = blogMapper.getBlogCounts(utils);
//        List<Blog> blogs = blogMapper.getBlogsForIndex(utils);
//        List<Blog> blogs = blogMapper.getBlogsList(utils);
//        List<Blog> blogs = blogMapper.getBlogsByTagId(utils);
//        System.out.println("总数是-》" + blogCounts);
        Blog blogByPrimaryKey = blogMapper.getBlogByPrimaryKey(1);
        System.out.println(blogByPrimaryKey);
//        List<Blog> list = blogMapper.getBlogListByType(0, 9);
//        for (Blog blog : list) {
//            System.out.println(blog);
//        }

    }


    @Autowired
    BlogCategoryMapper blogCategoryMapper;

    @Test
    void getCategoryTest(){
        List<BlogCategory> allCategory = blogCategoryMapper.getAllCategory();
        for (BlogCategory category : allCategory) {
            System.out.println(category);
        }
        System.out.println("========================================");
        BlogCategory category = blogCategoryMapper.getCategoryByName("日常随笔");
        System.out.println("单独查询到的是：" + category);
    }

    @Autowired
    BlogTagMapper blogTagMapper;

    @Test
    public void tagTest(){
//        List<BlogTagCount> tagsForIndex = blogTagMapper.getTagsForIndex();
//        for (BlogTagCount tags : tagsForIndex) {
//            System.out.println(tags);
//        }
        BlogTag tag = blogTagMapper.getTagByName("spring-boot企业级开发");
        System.out.println(tag);
    }

    @Autowired
    BlogCommentMapper commentMapper;


    @Test
    public void commentTest(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", constant.commentLimit);
        map.put("blogId", 1);
        Blog blog = blogMapper.getBlogByPrimaryKey(1);
        PageQueryUtils utils = new PageQueryUtils(map);
        List<BlogComment> comment = commentMapper.getComment(utils);
        int count = commentMapper.getCommentCount(utils);
        for (BlogComment blogComment : comment) {
            System.out.println(blogComment);
        }
        System.out.println(count);
    }

}
