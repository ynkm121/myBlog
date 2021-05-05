package com.liu.blog.controller.Admin;


import com.liu.blog.service.BlogService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.Result;
import com.liu.blog.utils.ResultGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class MyBlogController {

    @Autowired
    BlogService blogService;

    @ApiOperation(value = "博客管理")
    @GetMapping("/blogs")
    public String blogs(){
        return "admin/blog";
    }

    @ApiOperation(value = "分页请求")
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
    public String edit(){
        return "admin/edit";
    }

    @ApiOperation(value = "修改博客")
    @GetMapping("/blogs/edit/{BlogId}")
    public String edit(@PathVariable("BlogId")String BlogId){

    }


}
