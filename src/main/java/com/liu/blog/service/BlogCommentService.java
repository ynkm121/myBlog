package com.liu.blog.service;

import com.liu.blog.pojo.BlogComment;
import com.liu.blog.utils.PageResult;


public interface BlogCommentService {

    PageResult getCommentPageByBlogIdAndPageNum(int blogId, int page);

    Boolean addComment(BlogComment blogComment);
}
