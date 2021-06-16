package com.liu.blog.controller.Admin;

import com.liu.blog.service.BlogTagService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.Result;
import com.liu.blog.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "标签管理")
@Controller
@RequestMapping("admin")
public class TagController {

    @Autowired
    BlogTagService tagService;

    @ApiOperation(value = "标签管理")
    @GetMapping("/tags")
    public String tags(Model model){
        model.addAttribute("path", "tags");
        return "admin/tag";
    }

    @ApiOperation(value = "列表获取")
    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if(StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty("limit")){
            return ResultGenerator.genFailResult("参数异常");
        }
        PageQueryUtils utils = new PageQueryUtils(params);
        return ResultGenerator.genSuccessResult(tagService.getTagPage(utils));
    }

    @ApiOperation(value = "增加标签")
    @PostMapping("/tags/save")
    @ResponseBody
    public Result save(@RequestParam("tagName")String tagName){
        if(StringUtils.isEmpty(tagName) || tagName.length() > 18){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(tagService.insertTag(tagName)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("标签不符合规范");
        }
    }

    @ApiOperation(value = "删除标签")
    @PostMapping("/tags/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(tagService.BatchDelete(ids)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("标签不符合规范");
        }
    }
}
