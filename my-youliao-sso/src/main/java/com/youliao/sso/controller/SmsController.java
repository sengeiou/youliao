package com.youliao.sso.controller;

import com.youliao.sso.service.SmsService;
import com.youliao.sso.vo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Lenny
 * @create 2021/11/20 3:43
 * @Description:
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/login")
    public ResponseEntity<ErrorResult> sendCheckCode(@RequestBody Map<String, String> params) {
        ErrorResult errorResult = null;
        String phone = params.get("phone");
        try {
            errorResult = smsService.sendCheckCode(phone);
            if (errorResult == null) {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            log.error("发送短信验证码失败，phone=", phone, e);
            errorResult = ErrorResult.builder().errorCode("000002").errorMsg("发送短信验证码失败").build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
