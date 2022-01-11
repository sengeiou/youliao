package com.youliao.sso.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youliao.commons.enums.SexEnum;
import com.youliao.commons.mapper.UserInfoMapper;
import com.youliao.commons.pojo.User;
import com.youliao.commons.pojo.UserInfo;
import com.youliao.sso.vo.PicUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author Lenny
 * @create 2021/11/20 22:34
 * @Description:
 */

@Service
public class UserInfoService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private FaceEngineService faceEngineService;

    @Autowired
    private PicUploadService picUploadService;

    public boolean saveUserInfo(String token, Map<String, String> params) {
        //通过token获取用户数据
        User user = userService.queryUserByToken(token);
        if (user == null) {
            return false;
        }
        //2,token校验成功,从params中将用户信息设置到UserInfo对象当中去
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        //方法比较两个字符串是否相等并且忽略大小写
        userInfo.setSex(StringUtils.equalsIgnoreCase(params.get("gender"), "man") ? SexEnum.MAN : SexEnum.WOMAN);
        userInfo.setNickName(params.get("nickname"));
        userInfo.setBirthday(params.get("birthday"));
        userInfo.setCity(params.get("city"));

        return userInfoMapper.insert(userInfo) == 1;
    }

    /**
     * 校验用户头像
     *
     * @param file
     * @param token
     * @return
     */
    public boolean saveUserLogo(MultipartFile file, String token) {
        User user = userService.queryUserByToken(token);
        //对查出的user进行一次存在判断
        if (user == null) {
            return false;
        }

        //校验头像是否为真人头像.调用虹软人脸识别接口
        try {
            boolean logo = faceEngineService.checkIsPortrait(file.getBytes());
            if (!logo) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //为真人,true,上传到阿里云OSS
        PicUploadResult result = picUploadService.upload(file);
        if (StringUtils.isEmpty(result.getName())) {
            //上传失败
            return false;
        }

        //成功,将头像url 存到userInfo表当中
        UserInfo userInfo = new UserInfo();
        userInfo.setLogo(result.getName());

        QueryWrapper<UserInfo> qw = new QueryWrapper<>();
        qw.eq("user_id", user.getId());

        return userInfoMapper.update(userInfo, qw) == 1;
    }


}
