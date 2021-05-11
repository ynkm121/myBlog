package com.liu.blog.controller.Admin;

import com.liu.blog.pojo.BlogLink;
import com.liu.blog.service.BlogLinkService;
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

import java.util.Date;
import java.util.Map;

@Api(value = "友情链接")
@Controller
@RequestMapping("admin")
public class LinkController {
    @Autowired
    BlogLinkService linkService;

    @ApiOperation(value = "友情链接")
    @GetMapping("/links")
    public String links(Model model){
        model.addAttribute("path", "links");
        return "admin/link";
    }

    @ApiOperation(value = "列表获取")
    @GetMapping("/links/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtils pageUtil = new PageQueryUtils(params);
        return ResultGenerator.genSuccessResult(linkService.getLinksPage(pageUtil));
    }

    @ApiOperation(value = "添加友链")
    @PostMapping("/links/save")
    @ResponseBody
    public Result save(@RequestParam("linkType")Byte linkType,
                       @RequestParam("linkName")String linkName,
                       @RequestParam("linkUrl")String linkUrl,
                       @RequestParam("linkDescription")String linkDescription,
                       @RequestParam("linkRank")Integer linkRank){
        if(linkType < 0 || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription) || linkRank < 0){
            return ResultGenerator.genFailResult("参数异常");
        }
        BlogLink link = new BlogLink();
        link.setLinkType(linkType);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkDescription(linkDescription);
        link.setLinkRank(linkRank);
        return ResultGenerator.genSuccessResult(linkService.insertLink(link));
    }

    @ApiOperation(value = "删除友链")
    @PostMapping("/links/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        if(ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(linkService.BatchDelete(ids)){
            return ResultGenerator.genSuccessResult("删除成功");
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    @ApiOperation(value = "修改前置获取信息")
    @GetMapping("/links/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id")Integer id){
        if(id < 0){
            return ResultGenerator.genFailResult("参数异常");
        }
        return ResultGenerator.genSuccessResult(linkService.getLinkById(id));
    }


    @ApiOperation(value = "修改友链")
    @PostMapping("/links/update")
    @ResponseBody
    public Result update(@RequestParam("linkId")Integer linkId,
                         @RequestParam("linkType")Byte linkType,
                         @RequestParam("linkName")String linkName,
                         @RequestParam("linkUrl")String linkUrl,
                         @RequestParam("linkDescription")String linkDescription,
                         @RequestParam("linkRank")Integer linkRank){
        BlogLink tmpLink = linkService.getLinkById(linkId);
        if(tmpLink == null){
            return ResultGenerator.genFailResult("无数据");
        }
        if(linkType < 0 || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription) || linkRank < 0){
            return ResultGenerator.genFailResult("参数异常");
        }
        tmpLink.setLinkType(linkType);
        tmpLink.setLinkName(linkName);
        tmpLink.setLinkUrl(linkUrl);
        tmpLink.setLinkDescription(linkDescription);
        tmpLink.setLinkRank(linkRank);
        return ResultGenerator.genSuccessResult(linkService.updateLink(tmpLink));

    }

}
