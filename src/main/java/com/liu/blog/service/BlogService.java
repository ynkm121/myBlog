package com.liu.blog.service;

import com.liu.blog.controller.vo.BlogDetailVO;
import com.liu.blog.controller.vo.SimpleBlogListVO;
import com.liu.blog.pojo.Blog;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;

import java.util.List;

public interface BlogService {
    // 首页信息
    PageResult getBlogsForIndex(int page);

    // 首页侧边栏
    List<SimpleBlogListVO> getBlogListForIndex(int type);

    //搜索对应类
    PageResult getByCategoryName(String name, int page);

    PageResult getByTagName(String tagName, int page);

    PageResult getByKeyword(String keyword, int page);

    PageResult getBlogsPage(PageQueryUtils pageUtil);

    BlogDetailVO getBlogDetail(int blogId);

    BlogDetailVO getBlogBySubUrl(String subUrl);
}
