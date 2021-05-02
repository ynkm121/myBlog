package com.liu.blog.service.impl;

import com.liu.blog.dao.BlogLinkMapper;
import com.liu.blog.pojo.BlogLink;
import com.liu.blog.service.BlogLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogLinkServiceImpl implements BlogLinkService {
    @Autowired
    BlogLinkMapper blogLinkMapper;

    @Override
    public Map<Byte, List<BlogLink>> getLinksForLinkPage() {
        List<BlogLink> links = blogLinkMapper.getAllLinks(null);
        if(links != null){
            return links.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
        }
        return null;
    }
}
