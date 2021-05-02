package com.liu.blog.service.impl;

import com.liu.blog.dao.BlogCommentMapper;
import com.liu.blog.pojo.BlogComment;
import com.liu.blog.service.BlogCommentService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;
import com.liu.blog.utils.constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Autowired
    BlogCommentMapper commentMapper;


    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(int blogId, int page) {
        if(page < 1) return null;
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", constant.commentLimit);
        map.put("blogId", blogId);
        map.put("commentStatus", 1);
        PageQueryUtils utils = new PageQueryUtils(map);
        List<BlogComment> comment = commentMapper.getComment(utils);
        if(!CollectionUtils.isEmpty(comment)){
            return new PageResult(comment.size(), utils.getLimit(), utils.getPage(), comment);
        }
        return null;
    }

    @Override
    public Boolean addComment(BlogComment blogComment) {
        return commentMapper.insertSelective(blogComment) > 0;
    }
}
