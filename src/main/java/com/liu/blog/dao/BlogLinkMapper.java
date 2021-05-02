package com.liu.blog.dao;

import com.liu.blog.pojo.BlogLink;
import com.liu.blog.utils.PageQueryUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogLinkMapper {

    List<BlogLink> getAllLinks(PageQueryUtils utils);

    int getLinksCount(PageQueryUtils utils);
}
