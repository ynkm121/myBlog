package com.liu.blog.dao;

import com.liu.blog.pojo.BlogTag;
import com.liu.blog.pojo.BlogTagCount;
import com.liu.blog.utils.PageQueryUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BlogTagMapper {

    int getTagCounts(PageQueryUtils utils);

    List<BlogTagCount> getTagsForIndex();

    BlogTag getTagByName(String tagName);
}
