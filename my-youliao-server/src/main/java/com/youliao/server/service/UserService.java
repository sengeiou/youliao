package com.youliao.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youliao.commons.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lenny
 * @create 2021/11/25 18:04
 * @Description:
 */

@Service
@Slf4j
public class UserService {

    @Value("${youliao.sso.url}")
    private String ssoUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 反序列化
     *
     * JSON数据转Java对象
     * 跳转到目录
     * 步骤：1.导入 Jackson 相关 jar 包,2.创建 Jackson 核心对象 ObjectMapper,3.调用 ObjectMapper 的相关方法进行转换
     *
     * 转换方法：
     * readValue(json字符串数据，Class)
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public User queryUserByToken(String token) {
        // https:127.0.0.1/user/token   这时候走Nginx进行分配  走sso当中校验token的那个方法
        String url = ssoUrl + "/user/" + token;
        String data = null;

        try {
            //User.class.   返回User对象
            data = restTemplate.getForObject(url, String.class);
            if (StringUtils.isEmpty(data)) {
                return null;
            }

            //这里拿到的是json对象,  所以需要进行反序列化
            return MAPPER.readValue(data, User.class);
        } catch (Exception e) {
            log.error("校验token出现错误,token="+token,e);
        }

        return null;

    }
}
