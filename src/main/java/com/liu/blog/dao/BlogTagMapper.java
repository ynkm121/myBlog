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

    List<BlogTag> getTagsList(PageQueryUtils utils);

    int getTagCounts(PageQueryUtils utils);

    List<BlogTagCount> getTagsForIndex();

    BlogTag getTagByName(String tagName);

    int BatchInsertTags(List<BlogTag> tags);

    int insertSelective(BlogTag tag);

    int BatchDelete(Integer[] ids);
}
