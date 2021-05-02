package com.liu.blog.service;

import com.liu.blog.pojo.BlogLink;

import java.util.List;
import java.util.Map;

public interface BlogLinkService {

    Map<Byte, List<BlogLink>> getLinksForLinkPage();
}
