package com.youliao.server.interceptor;

import com.alibaba.fastjson.JSON;
import com.youliao.commons.pojo.User;
import com.youliao.commons.utils.Cache;
import com.youliao.commons.utils.NoAuthorization;
import com.youliao.commons.utils.UserThreadLocal;
import com.youliao.server.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lenny
 * @create 2021/11/29 0:56
 * @Description: 接口拦截器, token的拦截处理,  记得注册拦截器
 */

@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //校验handler是否为HandlerMethod
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //判断是否包含自定义的注解,包含,放行
        if (((HandlerMethod) handler).hasMethodAnnotation(NoAuthorization.class)) {
            return true;
        }

        //不包含,走拦截器
        //请求头当中获取 token
        String token = request.getHeader("Authorization");
        // 判断null or 空
        if (StringUtils.isEmpty(token)) {
            User user = userService.queryUserByToken(token);
            if (user != null) {
                //token有效  --->  放到ThreadLocal当中去
                UserThreadLocal.setLocal(user);

                return true;
            }
        }
        //token无效,  响应 401状态码, 代表无权限
        response.setStatus(401);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除ThreadLocal
        UserThreadLocal.removeLocal();
    }
}

