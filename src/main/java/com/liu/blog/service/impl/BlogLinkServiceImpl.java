package com.liu.blog.service.impl;

import com.liu.blog.dao.BlogLinkMapper;
import com.liu.blog.pojo.BlogLink;
import com.liu.blog.service.BlogLinkService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.PageResult;
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
    public BlogLink getLinkById(Integer linkId) {

        return blogLinkMapper.getLinkById(linkId);
    }

    @Override
    public Map<Byte, List<BlogLink>> getLinksForLinkPage() {
        List<BlogLink> links = blogLinkMapper.getAllLinks(null);
        if(links != null){
            return links.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
        }
        return null;
    }

    @Override
    public int getLinkCount() {
        return blogLinkMapper.getLinksCount(null);
    }

    @Override
    public PageResult getLinksPage(PageQueryUtils pageUtil) {
        List<BlogLink> links = blogLinkMapper.getAllLinks(pageUtil);
        int count = blogLinkMapper.getLinksCount(null);
        return new PageResult(count, pageUtil.getLimit(), pageUtil.getPage(), links);
    }

    @Override
    public boolean insertLink(BlogLink link) {
        return blogLinkMapper.insertSelective(link) > 0;
    }

    @Override
    public boolean BatchDelete(Integer[] ids) {

        return blogLinkMapper.BatchDelete(ids) > 0;
    }

    @Override
    public Boolean updateLink(BlogLink link) {
        return blogLinkMapper.updateByIdSelective(link) > 0;
    }
}
