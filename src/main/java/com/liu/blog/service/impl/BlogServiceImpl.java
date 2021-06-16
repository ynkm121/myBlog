package com.liu.blog.service.impl;

import com.liu.blog.controller.vo.BlogDetailVO;
import com.liu.blog.controller.vo.BlogListVO;
import com.liu.blog.controller.vo.SimpleBlogListVO;
import com.liu.blog.dao.*;
import com.liu.blog.pojo.Blog;
import com.liu.blog.pojo.BlogCategory;
import com.liu.blog.pojo.BlogTag;
import com.liu.blog.pojo.BlogTagRelation;
import com.liu.blog.service.BlogService;
import com.liu.blog.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    BlogCategoryMapper categoryMapper;

    @Autowired
    BlogTagMapper tagMapper;

    @Autowired
    BlogCommentMapper commentMapper;

    @Autowired
    BlogTagRelationMapper relationMapper;

    @Override
    public PageResult getBlogsForIndex(int page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", constant.limit);
        PageQueryUtils queryUtils = new PageQueryUtils(map);
        List<Blog> blogs = blogMapper.getBlogsList(queryUtils);
        int counts = blogMapper.getBlogCounts(queryUtils);
        List<BlogListVO> blogListVOS = getVOByBlogList(blogs);
        return new PageResult(counts, queryUtils.getLimit(), page, blogListVOS);
    }

    @Override
    public List<SimpleBlogListVO> getBlogListForIndex(int type) {
        List<SimpleBlogListVO> listVOS = new ArrayList<>();
        List<Blog> blogList = blogMapper.getBlogListByType(type, constant.limit);
        if(!CollectionUtils.isEmpty(blogList)){
            for (Blog blog : blogList) {
                SimpleBlogListVO vo = new SimpleBlogListVO();
                BeanUtils.copyProperties(blog, vo);
                listVOS.add(vo);
            }
        }
        return listVOS;
    }

    @Override
    public PageResult getByCategoryName(String name, int page) {
        if(PatternUtil.validKeyword(name)){
            BlogCategory category = categoryMapper.getCategoryByName(name);
            if(name.equals("默认分类") && category == null){
                category = new BlogCategory();
                category.setCategoryId(0);
            }
            if(category != null && page > 0){
                HashMap<String, Object> map = new HashMap<>();
                map.put("page", page);
                map.put("limit", constant.limit);
                map.put("blogCategoryId", category.getCategoryId());
                map.put("blogStatus", 1);
                PageQueryUtils utils = new PageQueryUtils(map);
                List<Blog> blogsList = blogMapper.getBlogsList(utils);
                List<BlogListVO> listVOS = this.getVOByBlogList(blogsList);
                int counts = blogMapper.getBlogCounts(utils);
                return new PageResult(counts, utils.getLimit(), utils.getPage(), listVOS);
            }
        }
        return null;
    }

    @Override
    public Blog getBlogById(int blogId) {
        return blogMapper.getBlogByPrimaryKey(blogId);
    }

    @Override
    public PageResult getByTagName(String tagName, int page) {
        BlogTag tag = tagMapper.getTagByName(tagName);
        if(tag != null && page > 0){
            HashMap<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("limit", constant.limit);
            map.put("tagId", tag.getTagId());
            PageQueryUtils utils = new PageQueryUtils(map);
            List<Blog> blogList = blogMapper.getBlogsByTagId(utils);
            List<BlogListVO> listVOS = this.getVOByBlogList(blogList);
            return new PageResult(blogList.size(), utils.getLimit(), utils.getPage(), listVOS);
        }
        return null;
    }

    @Override
    public PageResult getByKeyword(String keyword, int page) {
        if(PatternUtil.validKeyword(keyword)){
            HashMap<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("limit", constant.limit);
            map.put("keyword", keyword);
            map.put("blogStatus", 1);
            PageQueryUtils utils = new PageQueryUtils(map);
            List<Blog> blogList = blogMapper.getBlogsList(utils);
            List<BlogListVO> listVOS = this.getVOByBlogList(blogList);
            return new PageResult(blogList.size(), utils.getLimit(), utils.getPage(), listVOS);
        }
        return null;
    }

    @Override
    public PageResult getBlogsPage(PageQueryUtils pageUtil) {
        List<Blog> blogsList = blogMapper.getBlogsList(pageUtil);
        int counts = blogMapper.getBlogCounts(null);
        return new PageResult(counts, pageUtil.getLimit(), pageUtil.getPage(), blogsList);
    }

    @Override
    public BlogDetailVO getBlogDetail(int blogId) {
        Blog blog = blogMapper.getBlogByPrimaryKey(blogId);
        BlogDetailVO blogDetailVO = getBlogDetailVOByBlog(blog);
        if(blogDetailVO != null){
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public BlogDetailVO getBlogBySubUrl(String subUrl) {
        Blog blog = blogMapper.getBlogBySubUrl(subUrl);
        BlogDetailVO detailVO = getBlogDetailVOByBlog(blog);
        if(detailVO != null){
            return detailVO;
        }
        return null;
    }

    @Override
    public int getBlogCount() {
        return blogMapper.getBlogCounts(null);
    }

    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        Blog blogForUpdate = blogMapper.getBlogByPrimaryKey(blog.getBlogId().intValue());
        if(blogForUpdate == null){
            return "该博客不存在";
        }
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setEnableComment(blog.getEnableComment());
        // 处理分类数据
        BlogCategory category = categoryMapper.getCategoryById(blog.getBlogCategoryId());
        if(category == null){
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        }else{
            blogForUpdate.setBlogCategoryId(category.getCategoryId());
            blogForUpdate.setBlogCategoryName(category.getCategoryName());
            // 标签排序值增加
            category.setCategoryRank(category.getCategoryRank() + 1);
        }
        // 处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if(tags.length > 6){
            return "标签长度限制为6";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        // 若有新标签，则需要进行插入处理
        List<BlogTag> tagListForInsert = new LinkedList<>();
        // 保存所有标签
        List<BlogTag> allTags = new LinkedList<>();
        for (String tag : tags) {
            BlogTag tagByName = tagMapper.getTagByName(tag);
            if(tagByName == null){
                BlogTag tmpTag = new BlogTag();
                tmpTag.setTagName(tag);
                tagListForInsert.add(tmpTag);
            }else{
                allTags.add(tagByName);
            }
        }
        // 若新标签数组不为空，则处理插入
        if(!CollectionUtils.isEmpty(tagListForInsert)){
            tagMapper.BatchInsertTags(tagListForInsert);
        }
        // 处理标签-文章Relation
        allTags.addAll(tagListForInsert);
        LinkedList<BlogTagRelation> blogTagRelations = new LinkedList<>();
        for(BlogTag tag: allTags){
            BlogTagRelation relation = new BlogTagRelation();
            relation.setBlogId(blog.getBlogId().intValue());
            relation.setTagId(tag.getTagId());
            blogTagRelations.add(relation);
        }

        // 修改blog信息->修改标签排序值->删除原关系->添加新关系
        categoryMapper.updateByPrimaryKeySelective(category);
        relationMapper.deleteByBlogId(blog.getBlogId());
        relationMapper.BatchInsertRelation(blogTagRelations);
        if(blogMapper.updateByPrimaryKeySelective(blogForUpdate) > 0){
            return "success";
        }

        return "修改失败";
    }

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        //处理分类数据
        BlogCategory category = categoryMapper.getCategoryById(blog.getBlogCategoryId());
        if(category == null){
            blog.setBlogCategoryName("默认分类");
            blog.setBlogCategoryId(0);
        }else{
            blog.setBlogCategoryName(category.getCategoryName());
            //排序值+1
            category.setCategoryRank(category.getCategoryRank() + 1);
        }
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6){
            return "标签长度限制为6";
        }
        if(blogMapper.insertSelective(blog) > 0){
            List<BlogTag> allTags = new LinkedList<>();
            List<BlogTag> newTags = new LinkedList<>();
            for (String tag : tags) {
                BlogTag blogTag = tagMapper.getTagByName(tag);
                if (blogTag == null) {
                    BlogTag tmpTag = new BlogTag();
                    tmpTag.setTagName(tag);
                    newTags.add(tmpTag);
                } else {
                    allTags.add(blogTag);
                }
            }
            //处理新增标签
            if (!CollectionUtils.isEmpty(newTags)) {
                tagMapper.BatchInsertTags(newTags);
            }
            allTags.addAll(newTags);
            //处理标签-文章relation
            LinkedList<BlogTagRelation> relations = new LinkedList<BlogTagRelation>();
            for (BlogTag tag : allTags) {
                BlogTagRelation relation = new BlogTagRelation();
                relation.setTagId(tag.getTagId());
                relation.setBlogId(blog.getBlogId().intValue());
                relations.add(relation);
            }
            //综合提交
            categoryMapper.updateByPrimaryKeySelective(category);
            if (relationMapper.BatchInsertRelation(relations) > 0) {
                return "success";
            }
        }
        return "提交失败";
    }

    @Override
    public boolean batchDelete(Integer[] ids) {
        return blogMapper.BatchDelete(ids) > 0;
    }

    private BlogDetailVO getBlogDetailVOByBlog(Blog blog) {
        if(blog != null && blog.getBlogStatus() == 1){
            //TODO redis优化访问量存读
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateByPrimaryKey(blog);
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            BlogCategory category = categoryMapper.getCategoryById(blogDetailVO.getBlogCategoryId());
            if(category == null){
                category = new BlogCategory();
                category.setCategoryId(0);
                category.setCategoryName("默认分类");
                category.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            blogDetailVO.setBlogCategoryIcon(category.getCategoryIcon());
            if(!StringUtils.isEmpty(blog.getBlogTags())){
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("blogId", blog.getBlogId());
            map.put("commentStatus", 1);
            blogDetailVO.setCommentCount(commentMapper.getCommentCount(map));
            return blogDetailVO;
        }
        return null;
    }


    private List<BlogListVO> getVOByBlogList(List<Blog> blogList) {
        List<BlogListVO> blogListVOS = new ArrayList<BlogListVO>();
        if(!CollectionUtils.isEmpty(blogList)){
            List<Integer> categoryIds = blogList.stream().map(Blog::getBlogCategoryId).collect(Collectors.toList());
            HashMap<Integer, String> cateMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(categoryIds)){
                for (Integer id : categoryIds) {
                    BlogCategory categoryById = categoryMapper.getCategoryById(id);
                    cateMap.put(categoryById.getCategoryId(), categoryById.getCategoryIcon());
                }
            }

            for (Blog blog : blogList) {
                BlogListVO blogListVO = new BlogListVO();
                BeanUtils.copyProperties(blog, blogListVO);
                if(cateMap.containsKey(blog.getBlogCategoryId())){
                    blogListVO.setBlogCategoryIcon(cateMap.get(blog.getBlogCategoryId()));
                }else{
                    blogListVO.setBlogCategoryId(0);
                    blogListVO.setBlogCategoryName("默认分类");
                    blogListVO.setBlogCategoryIcon("admin/dist/img/category/00.png");
                }
                blogListVOS.add(blogListVO);
            }
        }
        return blogListVOS;
    }
}
