package com.liu.blog.controller.Admin;

import com.liu.blog.pojo.BlogUser;
import com.liu.blog.service.*;
import com.liu.blog.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(value = "管理面板控制")
@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    BlogCategoryService categoryService;
    @Autowired
    BlogService blogService;
    @Autowired
    BlogLinkService linkService;
    @Autowired
    BlogTagService tagService;
    @Autowired
    BlogCommentService commentService;
    
    @ApiOperation(value = "登陆界面跳转")
    @GetMapping({"/login", "/", ""})
    public String login(){
        return "admin/login";
    }

    @ApiOperation(value = "登陆验证")
    @PostMapping("/login")
    public String login(@RequestParam String userName,
                        @RequestParam String password,
                        @RequestParam String verifyCode,
                        HttpSession session,
                        Model model){
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            session.setAttribute("errorMsg", "请补全用户信息");
            return "admin/login";
        }
        if(StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        String code = String.valueOf(session.getAttribute("verifyCode"));
        if(!code.equals(verifyCode)){
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        try {
            subject.login(token);
            BlogUser user = userService.login(userName, password);
            session.setAttribute("loginUser", user.getNickName());
            session.setAttribute("loginUserId", user.getUserId());
            return "redirect:/admin/index";
        }catch (UnknownAccountException e){
            session.setAttribute("errorMsg", e.getMessage());
            return "admin/login";
        }catch (IncorrectCredentialsException e){
            session.setAttribute("errorMsg", "认证错误");
            return "admin/login";
        }catch (AuthenticationException e){
            session.setAttribute("errorMsg", "未知错误");
            return "admin/login";
        }
//        BlogUser blogUser = userService.login(userName, password);
//        if(blogUser != null){
//            session.setAttribute("loginUser", blogUser.getNickName());
//            session.setAttribute("loginUserId", blogUser.getUserId());
//            //session过期时间设置为7200秒 即两小时
//            session.setMaxInactiveInterval(60 * 60 * 2);
//            return "redirect:/admin/index";
//        }else{
//            session.setAttribute("errorMsg", "登录验证错误，请重试");
//            return "admin/login";
//        }
    }

    @ApiOperation(value = "管理面板主页")
    @GetMapping({"/index", "/index.html"})
    public String index(Model model){
        model.addAttribute("path", "index");
        model.addAttribute("categoryCount", categoryService.getCategoryCount());
        model.addAttribute("blogCount", blogService.getBlogCount());
        model.addAttribute("linkCount", linkService.getLinkCount());
        model.addAttribute("tagCount", tagService.getTagCount());
        model.addAttribute("commentCount", commentService.getCommentCount());
        return "admin/index";
    }

    @ApiOperation(value = "个人资料")
    @GetMapping("/profile")
    public String profile(HttpServletRequest req){
        Integer userId = (Integer) req.getSession().getAttribute("loginUserId");
        System.out.println(userId);
        if(userId == null){
            return "admin/login";
        }
        BlogUser user = userService.getUserById(userId);
        req.setAttribute("loginUserName", user.getUserName());
        req.setAttribute("nickName", user.getNickName());
        req.setAttribute("path", "profile");
        return "admin/profile";
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request,
                                 @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (userService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }
    }

    @ApiOperation(value = "修改昵称")
    @PostMapping("/profile/name")
    @ResponseBody
    public String rename(@RequestParam("loginUserName")String loginUserName,
                         @RequestParam("nickName")String nickName,
                         HttpServletRequest req){
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) req.getSession().getAttribute("loginUserId");
        if (userService.updateName(loginUserId, loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    @ApiOperation(value = "登出")
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("loginUser");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/";
    }

}
