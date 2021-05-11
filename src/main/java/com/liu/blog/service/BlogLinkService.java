package com.liu.blog.service;

import com.liu.blog.pojo.BlogLink;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.Map;

public interface BlogLinkService {

    BlogLink getLinkById(Integer linkId);

    Map<Byte, List<BlogLink>> getLinksForLinkPage();

    int getLinkCount();

    PageResult getLinksPage(PageQueryUtils pageUtil);

    boolean insertLink(BlogLink link);

    boolean BatchDelete(Integer[] ids);

    Boolean updateLink(BlogLink link);
}
