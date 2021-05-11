package com.liu.blog.controller.Admin;

import com.liu.blog.pojo.BlogConfig;
import com.liu.blog.service.BlogConfigService;
import com.liu.blog.utils.Result;
import com.liu.blog.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(value = "系统配置")
@Controller
@RequestMapping("admin")
public class ConfigController {

    @Autowired
    BlogConfigService configService;

    @ApiOperation(value = "系统配置")
    @GetMapping("/configurations")
    public String links(Model model){
        model.addAttribute("path", "configurations");
        model.addAttribute("configurations", configService.getConfig());
        return "admin/configuration";
    }

    @ApiOperation(value = "站点信息")
    @PostMapping("/configurations/website")
    @ResponseBody
    public Result website(@RequestParam(value = "websiteName", required = false)String websiteName,
                          @RequestParam(value = "websiteDescription", required = false)String websiteDescription,
                          @RequestParam(value = "websiteLogo", required = false)String websiteLogo,
                          @RequestParam(value = "websiteIcon", required = false)String websiteIcon){
        int updateResult = 0;
        if(!StringUtils.isEmpty(websiteName)){
            updateResult += configService.updateConfig("websiteName", websiteName);
        }
        if(!StringUtils.isEmpty(websiteDescription)){
            updateResult += configService.updateConfig("websiteDescription", websiteDescription);
        }
        if(!StringUtils.isEmpty(websiteLogo)){
            updateResult += configService.updateConfig("websiteLogo", websiteLogo);
        }
        if(!StringUtils.isEmpty(websiteIcon)){
            updateResult += configService.updateConfig("websiteIcon", websiteIcon);
        }
        return ResultGenerator.genSuccessResult(updateResult > 0);
    }

    @ApiOperation(value = "个人信息")
    @PostMapping("/configurations/userInfo")
    @ResponseBody
    public Result userInfo(@RequestParam("yourAvatar")String yourAvatar,
                           @RequestParam("yourName")String yourName,
                           @RequestParam("yourEmail")String yourEmail){
        int updateResult = 0;
        if(!StringUtils.isEmpty(yourAvatar)){
            updateResult += configService.updateConfig("yourAvatar", yourAvatar);
        }
        if(!StringUtils.isEmpty(yourName)){
            updateResult += configService.updateConfig("yourName", yourName);
        }
        if(!StringUtils.isEmpty(yourEmail)){
            updateResult += configService.updateConfig("yourEmail", yourEmail);
        }
        return ResultGenerator.genSuccessResult(updateResult > 0);
    }

    @ApiOperation(value = "底部设置")
    @PostMapping("/configurations/footer")
    @ResponseBody
    public Result userInfo(@RequestParam("footerAbout")String footerAbout,
                           @RequestParam("footerICP")String footerICP,
                           @RequestParam("footerCopyRight")String footerCopyRight,
                           @RequestParam("footerPoweredBy")String footerPoweredBy,
                           @RequestParam("footerPoweredByURL")String footerPoweredByURL){
        int updateResult = 0;
        if(!StringUtils.isEmpty(footerAbout)){
            updateResult += configService.updateConfig("footerAbout", footerAbout);
        }
        if(!StringUtils.isEmpty(footerICP)){
            updateResult += configService.updateConfig("footerICP", footerICP);
        }
        if(!StringUtils.isEmpty(footerCopyRight)){
            updateResult += configService.updateConfig("footerCopyRight", footerCopyRight);
        }
        if(!StringUtils.isEmpty(footerPoweredBy)){
            updateResult += configService.updateConfig("footerPoweredBy", footerPoweredBy);
        }
        if(!StringUtils.isEmpty(footerPoweredByURL)){
            updateResult += configService.updateConfig("footerPoweredByURL", footerPoweredByURL);
        }
        return ResultGenerator.genSuccessResult(updateResult > 0);
    }
}
