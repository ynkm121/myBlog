package com.liu.blog.controller.Admin;

import com.liu.blog.service.BlogCommentService;
import com.liu.blog.utils.PageQueryUtils;
import com.liu.blog.utils.Result;
import com.liu.blog.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "评论管理")
@Controller
@RequestMapping("admin")
public class CommentController {

    @Autowired
    BlogCommentService commentService;

    @ApiOperation(value = "评论管理")
    @GetMapping("/comments")
    public String comments(Model model){
        model.addAttribute("path", "comments");
        return "admin/comment";
    }

    @ApiOperation(value = "列表获取")
    @GetMapping("/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtils pageUtil = new PageQueryUtils(params);
        return ResultGenerator.genSuccessResult(commentService.getCommentPage(pageUtil));
    }

    @ApiOperation(value = "评论审核")
    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result check(@RequestBody Integer[] commentIds){
        if(commentIds.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(commentService.BatchCheck(commentIds)){
            return ResultGenerator.genSuccessResult("审核成功");
        }else{
            return ResultGenerator.genFailResult("审核失败");
        }
    }

    @ApiOperation(value = "评论回复")
    @PostMapping("/comments/reply")
    @ResponseBody
    public Result reply(@RequestParam("commentId")Integer commentId,
                        @RequestParam("replyBody")String reply){
        if(commentId == null || commentId < 1 || StringUtils.isEmpty(reply)){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(commentService.replyComment(commentId, reply)){
            return ResultGenerator.genSuccessResult("回复成功");
        }else{
            return ResultGenerator.genFailResult("回复失败");
        }
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(commentService.BatchDelete(ids)){
            return ResultGenerator.genSuccessResult("删除成功");
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}
