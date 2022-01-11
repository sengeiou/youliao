package com.youliao.sso.controller;

import com.youliao.sso.service.UserInfoService;
import com.youliao.sso.vo.ErrorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Lenny
 * @create 2021/11/20 22:30
 * @Description:
 */

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 完善个人信息
     *
     * @param token
     * @param params 传过来的json参数
     * @return
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity<Object> saveUserInfo(@RequestHeader("Authorization") String token,
                                               @RequestBody Map<String, String> params) {

        boolean flag = userInfoService.saveUserInfo(token, params);
        if (flag) {
            return ResponseEntity.ok(null);
        }
        // 如果flag为false，则返回错误信息
        ErrorResult errorResult = ErrorResult.builder().errorMsg("登录失败").errorCode("000002").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }

    @PostMapping("loginReginfo/head")
    public ResponseEntity<Object> saveUserLogo(@RequestParam("headPhoto") MultipartFile file,
                                               @RequestHeader("Authorization") String token) {
        try {
            boolean flag = userInfoService.saveUserLogo(file, token);
            if (flag) {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ErrorResult errorResult = ErrorResult.builder().errorCode("000001").errorMsg("保存用户头像失败").build();
        return ResponseEntity.status(500).body(errorResult);
    }
}
