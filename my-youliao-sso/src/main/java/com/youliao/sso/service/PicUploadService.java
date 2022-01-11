package com.youliao.sso.service;

import com.aliyun.oss.OSSClient;
import com.youliao.sso.config.AliyunConfig;
import com.youliao.sso.vo.PicUploadResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Lenny
 * @create 2021/11/23 13:23
 * @Description:
 */
@Service
public class PicUploadService {

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private AliyunConfig aliyunConfig;

    //允许上传的图片格式
    private static final String[] IMAGE_TYPE = new String[]{".png", ".jpg", ".jpeg", ".bmp", ".gif"};

    public PicUploadResult upload(MultipartFile file) {
        PicUploadResult picUploadResult = new PicUploadResult();
        //做图片校验,对后缀名
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            //校验是否以xxx结尾,并忽略大小写
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }

        if (!isLegal) {
            picUploadResult.setStatus("error");
        }

        //文件的新路径
        String fileName = file.getOriginalFilename();
        String filePath = getFile(fileName);

        //上传到阿里云
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            //上传失败
            picUploadResult.setStatus("error");
            return picUploadResult;
        }

        //上传成功
        picUploadResult.setStatus("done");
        picUploadResult.setName(aliyunConfig.getUrlPrefix() + filePath);
        picUploadResult.setUid(String.valueOf(System.currentTimeMillis()));

        return picUploadResult;
    }

    private String getFile(String fileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy") + "/"
                + dateTime.toString("MM") + "/" + dateTime.toString("dd")
                + "/" + System.currentTimeMillis() + RandomUtils.nextInt(100, 9999) + "."
                + StringUtils.substringAfterLast(fileName, ".");
    }

}
