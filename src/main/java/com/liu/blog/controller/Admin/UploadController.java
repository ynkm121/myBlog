package com.liu.blog.controller.Admin;

import com.liu.blog.utils.MyBlogUtils;
import com.liu.blog.utils.Result;
import com.liu.blog.utils.ResultGenerator;
import com.liu.blog.utils.constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Api(value = "文件上传")
@Controller
@RequestMapping("/admin")
public class UploadController {

    @ApiOperation( value = "上传管理")
    @PostMapping("/upload/file")
    @ResponseBody
    public Result upload(HttpServletRequest req, @RequestParam("file")MultipartFile file){
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Random r = new Random();
        String newFileName = new StringBuilder().append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName).toString();
        File fileDirectory = new File(constant.FILE_UPLOAD_DIC);
        // 创建文件
        File destFile = new File(constant.FILE_UPLOAD_DIC + newFileName);
        try{
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            file.transferTo(destFile);
            Result result = ResultGenerator.genSuccessResult();
            result.setData(MyBlogUtils.getHost(new URI(String.valueOf(req.getRequestURL()))) + "/upload/" + newFileName);
            return result;
        }catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }
}
