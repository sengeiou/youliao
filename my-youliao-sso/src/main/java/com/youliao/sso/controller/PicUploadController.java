package com.youliao.sso.controller;

import com.youliao.sso.service.PicUploadService;
import com.youliao.sso.vo.PicUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lenny
 * @create 2021/11/23 13:20
 * @Description:
 */

@RequestMapping("pic/upload")
@Controller
public class PicUploadController {

    @Autowired
    private PicUploadService picUploadService;

    @PostMapping
    @ResponseBody
    public PicUploadResult upload(@RequestParam("file")MultipartFile file){
        return picUploadService.upload(file);
    }
}
