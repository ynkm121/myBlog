package com.liu.blog.controller.Admin;

import com.liu.blog.pojo.BlogCategory;
import com.liu.blog.service.BlogCategoryService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.Result;
import com.liu.blog.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "分类管理")
@Controller
@RequestMapping("admin")
public class CategoryController {

    @Autowired
    BlogCategoryService categoryService;

    @ApiOperation(value = "分类管理")
    @GetMapping("/categories")
    public String category(Model model){
        model.addAttribute("path", "categories");
        return "admin/category";
    }

    @ApiOperation(value = "列表获取")
    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if(StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))){
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtils utils = new PageQueryUtils(params);
        return ResultGenerator.genSuccessResult(categoryService.getCategoryPage(utils));
    }

    @ApiOperation(value = "添加分类")
    @PostMapping("/categories/save")
    @ResponseBody
    public Result insert(@RequestParam(value = "categoryId", required = false)Integer categoryId,
                         @RequestParam("categoryName")String categoryName,
                         @RequestParam("categoryIcon")String categoryIcon){
        if(StringUtils.isEmpty(categoryName) || StringUtils.isEmpty(categoryIcon)){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(categoryService.insertCategory(categoryName, categoryIcon)){
            return ResultGenerator.genSuccessResult("新增成功");
        }else{
            return ResultGenerator.genFailResult("新增失败");
        }
    }

    @ApiOperation(value = "修改分类")
    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(@RequestParam("categoryId")Integer categoryId,
                         @RequestParam("categoryName")String categoryName,
                         @RequestParam("categoryIcon")String categoryIcon){
        if(categoryId == null || categoryId < 1 || StringUtils.isEmpty(categoryName) || StringUtils.isEmpty(categoryIcon)){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(categoryService.updateCategory(categoryId, categoryName, categoryIcon)){
            return ResultGenerator.genSuccessResult("修改成功");
        }else{
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

    @ApiOperation(value = "批量删除分类")
    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(categoryService.BatchDelete(ids)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("操作失败");
        }
    }
}
