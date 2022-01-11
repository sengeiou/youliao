package com.youliao.sso.vo;

import lombok.Data;

/**
 * @author Lenny
 * @create 2021/11/23 13:17
 * @Description:   aliyun oss图片上传返回结果
 */

@Data
public class PicUploadResult {
    //文件唯一标识;
    private String uid;
    //文件名;
    private String name;
    //文件状态; uploading done error removed;
    private String status;

    //服务端响应状态; {"status":"SUCCESS"}
    private String response;
}
