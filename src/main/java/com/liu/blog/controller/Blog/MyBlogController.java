package com.liu.blog.controller.Blog;

import com.liu.blog.controller.vo.BlogDetailVO;
import com.liu.blog.dao.BlogTagMapper;
import com.liu.blog.pojo.BlogComment;
import com.liu.blog.pojo.BlogLink;
import com.liu.blog.service.*;
import com.liu.blog.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Api(value = "博客访问类")
@Controller
public class MyBlogController {

    public static String theme = "amaze";

    @Autowired
    BlogService blogService;

    @Autowired
    BlogConfigService blogConfigService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogCommentService commentService;

    @Autowired
    BlogLinkService linkService;

    @ApiOperation(value = "首页加载过渡")
    @GetMapping({"/", "/index", "/index.html"})
    public String index(Model model){
        return this.page(model, 1);
    }

    @ApiOperation(value = "首页加载/换页")
    @GetMapping("/page/{pageIndex}")
    public String page(Model model, @PathVariable("pageIndex") int page){
        PageResult pageResult = blogService.getBlogsForIndex(page);
        if(pageResult == null){
            return "error/error_404";
        }
        modelAdd(model, pageResult, "首页", null, null);
        return "blog/" + theme + "/index";
    }

    @ApiOperation(value = "分类加载过渡")
    @GetMapping("/category/{cateName}")
    public String categorySearch(Model model, @PathVariable("cateName")String name){
        return categorySearch(model, name, 1);
    }

    @ApiOperation(value = "分类加载/换页")
    @GetMapping("/category/{cateName}/{page}")
    public String categorySearch(Model model, @PathVariable("cateName")String name, @PathVariable("page")int page) {
        PageResult pageResult = blogService.getByCategoryName(name, page);
        modelAdd(model, pageResult, "分类", "category", name);
        return "blog/" + theme + "/list";
    }

    @ApiOperation(value = "标签加载过渡")
    @GetMapping("/tag/{tagName}")
    public String tagSearch(Model model, @PathVariable("tagName")String tagName){
        return tagSearch(model, tagName, 1);
    }

    @ApiOperation(value = "标签加载/换页")
    @GetMapping("/tag/{tagName}/{page}")
    public String tagSearch(Model model, @PathVariable("tagName")String tagName, @PathVariable("page")int page){
        PageResult pageResult = blogService.getByTagName(tagName, page);
        modelAdd(model, pageResult, "标签", "tag", tagName);
        return "blog/" + theme + "/list";
    }

    @ApiOperation(value = "搜索过渡")
    @GetMapping("/search/{keyword}")
    public String search(Model model, @PathVariable("keyword")String keyword){
        return search(model, keyword, 1);
    }

    @ApiOperation(value = "搜索结果显示/换页")
    @GetMapping("/search/{keyword}/{page}")
    public String search(Model model, @PathVariable("keyword")String keyword, @PathVariable("page")int page) {
        PageResult pageResult = blogService.getByKeyword(keyword, page);
        modelAdd(model, pageResult, "搜索", "search", keyword);
        return "blog/" + theme + "/list";
    }

    @ApiOperation(value = "文章详细")
    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String blogDetail(Model model, @PathVariable("blogId")int blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1")Integer commentPage){
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if(blogDetailVO != null){
            model.addAttribute("blogDetailVO", blogDetailVO);
            model.addAttribute("commentPageResult", commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
        }
        model.addAttribute("pageName", "详情");
        model.addAttribute("configurations", blogConfigService.getConfig());
        return "blog/" + theme + "/detail";
    }

    @ApiOperation(value = "评论操作")
    @PostMapping("/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest req, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody){
        if(StringUtils.isEmpty(verifyCode)){
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if(!verifyCode.equals(kaptchaCode)){
            return ResultGenerator.genFailResult("验证码错误");
        }
        String referer = req.getHeader("Referer");
        if(StringUtils.isEmpty(referer)){
            return ResultGenerator.genFailResult("非法请求");
        }
        if(null == blogId || blogId < 0){
            return ResultGenerator.genFailResult("非法请求");
        }
        if(StringUtils.isEmpty(commentator)){
            return ResultGenerator.genFailResult("名称不能为空");
        }
        if(StringUtils.isEmpty(email)){
            return ResultGenerator.genFailResult("邮箱不能为空");
        }
        if(!PatternUtil.isEmail(email)){
            return ResultGenerator.genFailResult("邮箱格式错误");
        }
        if(StringUtils.isEmpty(commentator)){
            return ResultGenerator.genFailResult("名称不能为空");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment blogComment = new BlogComment();
        blogComment.setBlogId(blogId.intValue());
        blogComment.setCommentator(MyBlogUtils.cleanString(commentator));
        blogComment.setEmail(email);
        if(PatternUtil.isURL(websiteUrl)){
            blogComment.setWebsiteUrl(websiteUrl);
        }
        blogComment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentService.addComment(blogComment));
    }

    @ApiOperation(value = "友链")
    @GetMapping("/link")
    public String toLinks(Model model){
        model.addAttribute("pageName", "友链");
        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksForLinkPage();
        if(linkMap != null){
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey((byte) 0)) {
                model.addAttribute("favoriteLinks", linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                model.addAttribute("recommendLinks", linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                model.addAttribute("personalLinks", linkMap.get((byte) 2));
            }
        }
        model.addAttribute("configurations", blogConfigService.getConfig());
        return "blog/" + theme + "/link";
    }

    @ApiOperation(value = "关于")
    @GetMapping("/about")
    public String subUrl(Model model){
        BlogDetailVO blogDetailVO = blogService.getBlogBySubUrl("about");
        if(blogDetailVO != null){
            model.addAttribute("pageName", "about");
            model.addAttribute("blogDetailVO", blogDetailVO);
            model.addAttribute("configurations", blogConfigService.getConfig());
            return "blog/" + theme + "/detail";
        }
        return null;
    }


    private void modelAdd(Model model, PageResult pageResult, String pageName, String pageUrl, String keyword){
        model.addAttribute("blogPageResult", pageResult);
        model.addAttribute("newBlogs", blogService.getBlogListForIndex(1));
        model.addAttribute("hotBlogs", blogService.getBlogListForIndex(0));
        model.addAttribute("hotTags", blogTagService.getIndexTagCount());
        model.addAttribute("pageName", pageName);
        model.addAttribute("pageUrl", pageUrl);
        model.addAttribute("keyword", keyword);
        model.addAttribute("configurations", blogConfigService.getConfig());
    }
}

