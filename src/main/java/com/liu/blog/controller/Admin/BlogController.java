package com.liu.blog.controller.Admin;


import com.liu.blog.controller.vo.BlogDetailVO;
import com.liu.blog.pojo.Blog;
import com.liu.blog.service.BlogCategoryService;
import com.liu.blog.service.BlogService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.Result;
import com.liu.blog.utils.ResultGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    BlogCategoryService categoryService;

    @ApiOperation(value = "博客管理")
    @GetMapping("/blogs")
    public String blogs(Model model){
        model.addAttribute("path", "blogs");
        return "admin/blog";
    }
    

    @ApiOperation(value = "列表获取")
    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtils pageUtil = new PageQueryUtils(params);
        return ResultGenerator.genSuccessResult(blogService.getBlogsPage(pageUtil));
    }

    @ApiOperation(value = "发布博客")
    @GetMapping("/blogs/edit")
    public String edit(Model model){
        model.addAttribute("path", "edit");
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit";
    }

    @ApiOperation(value = "修改博客")
    @GetMapping("/blogs/edit/{blogId}")
    public String edit(Model model, @PathVariable("blogId")int blogId){
        model.addAttribute("path", "edit");
        Blog blog = blogService.getBlogById(blogId);
        if(blog != null){
            model.addAttribute("blog", blog);
        }
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit";
    }

    @ApiOperation(value = "修改提交")
    @PostMapping("/blogs/update")
    @ResponseBody
    public Result update(@RequestParam("blogId") Long blogId,
                         @RequestParam("blogTitle") String blogTitle,
                         @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                         @RequestParam("blogCategoryId") Integer blogCategoryId,
                         @RequestParam("blogTags") String blogTags,
                         @RequestParam("blogContent") String blogContent,
                         @RequestParam("blogCoverImage") String blogCoverImage,
                         @RequestParam("blogStatus") Byte blogStatus,
                         @RequestParam("enableComment") Byte enableComment){
        if(StringUtils.isEmpty(blogTitle)){
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if(blogTitle.trim().length() > 150){
            return ResultGenerator.genFailResult("标题过长");
        }
        if(blogSubUrl.trim().length() > 150){
            return ResultGenerator.genFailResult("路径过长");
        }
        if(StringUtils.isEmpty(blogCategoryId)){
            return ResultGenerator.genFailResult("请选择文章分类");
        }
        if(StringUtils.isEmpty(blogTags)){
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if(blogTags.trim().length() > 150){
            return ResultGenerator.genFailResult("标签过长");
        }
        if(StringUtils.isEmpty(blogContent)){
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if(blogContent.trim().length() > 100000){
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }

        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String result = blogService.updateBlog(blog);
        if("success".equals(result)){
            return ResultGenerator.genSuccessResult("修改成功");
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    @ApiOperation(value = "博客发表")
    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(@RequestParam("blogTitle") String blogTitle,
                       @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                       @RequestParam("blogCategoryId") Integer blogCategoryId,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogCoverImage") String blogCoverImage,
                       @RequestParam("blogStatus") Byte blogStatus,
                       @RequestParam("enableComment") Byte enableComment){
        if(StringUtils.isEmpty(blogTitle)){
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if(blogTitle.trim().length() > 150){
            return ResultGenerator.genFailResult("标题过长");
        }
        if(blogSubUrl.trim().length() > 150){
            return ResultGenerator.genFailResult("路径过长");
        }
        if(StringUtils.isEmpty(blogCategoryId)){
            return ResultGenerator.genFailResult("请选择文章分类");
        }
        if(StringUtils.isEmpty(blogTags)){
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if(blogTags.trim().length() > 150){
            return ResultGenerator.genFailResult("标签过长");
        }
        if(StringUtils.isEmpty(blogContent)){
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if(blogContent.trim().length() > 100000){
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
        String result = blogService.saveBlog(blog);
        if("success".equals(result)){
            return ResultGenerator.genSuccessResult("提交成功");
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    @ApiOperation(value = "删除博客")
    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length < 1)
            return ResultGenerator.genFailResult("参数异常");
        if(blogService.batchDelete(ids)){
            return ResultGenerator.genSuccessResult("删除成功");
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}
