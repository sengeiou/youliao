package com.youliao.sso.controller;

import com.youliao.commons.pojo.User;
import com.youliao.sso.service.UserService;
import com.youliao.sso.vo.ErrorResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenny
 * @create 2021/11/20 4:16
 * @Description:
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/loginVerification")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> params) {

        //前端传来的JSON格式 params进行解析
        String phone = params.get("phone");
        String code = params.get("verificationCode");

        //交给service层进行处理
        String data = userService.login(phone, code);

        if (StringUtils.isNotEmpty(data)) {
            //不为空,登录成功

            String[] str = StringUtils.split(data, "|");

            HashMap<String, Object> map = new HashMap<>();
            map.put("token", str[0]);
            map.put("isNew", Boolean.valueOf(str[1]));

            //结果响应给客户端
            return ResponseEntity.ok(map);
        }
        //为空,登录失败
        ErrorResult errorResult = ErrorResult.builder().errorCode("000002").errorMsg("登录失败").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);

    }

    /**
     * 校验token,根据token查用户数据
     *
     * @param token
     * @return
     */
    @GetMapping("{token}")
    public User queryByToken(@PathVariable("token") String token) {
        return userService.queryUserByToken(token);
    }
}
