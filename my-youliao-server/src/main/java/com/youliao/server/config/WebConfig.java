package com.youliao.server.config;

import com.youliao.server.interceptor.RedisCacheInterceptor;
import com.youliao.server.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lenny
 * @create 2021/11/29 3:18
 * @Description: 拦截器配置, 加载到spring容器当中去
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RedisCacheInterceptor redisCacheInterceptor;

    @Autowired
    private UserTokenInterceptor userTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有  /**

        //注意拦截器是有前后顺序的.   ----> 先校验token后, redis缓存拦截器才有意义
        registry.addInterceptor(userTokenInterceptor).addPathPatterns("/**");
        registry.addInterceptor(redisCacheInterceptor).addPathPatterns("/**");

    }
}
