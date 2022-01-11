package com.youliao.server.interceptor;

import com.alibaba.fastjson.JSON;
import com.youliao.commons.utils.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.concurrent.TimeUnit;

/*
 * 目标：
 *  在controller响应(return)数据之前，对响应数据做处理
 *
 *  ResponseBodyAdvice+@ControllerAdvice 实现在controller响应数据之前，对响应数据做处理
 *
 *  ResponseBodyAdvice：响应体做增强
 *      supports：
 *          支持，true   做增强
 *                      是 执行beforeBodyWrite
 *               false  不做增强
 *                      是 不执行beforeBodyWrite
 *   beforeBodyWrite
 *
 * */
@ControllerAdvice//对controller增强
public class MyResponseBodyAdvice implements ResponseBodyAdvice {

    @Value("${youliao.cache.enable}")
    private boolean enable;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        //四个开关都符合,  才会 执行 beforeWrite
        return enable && returnType.hasMethodAnnotation(GetMapping.class) && returnType.hasMethodAnnotation(Cache.class);
    }

    /**
     * @param body 响应体
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType
            , Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body == null) {
            return null;
        }

        try {
            String redisValue = null;
            /*
             * instanceof是Java的一个保留关键字，左边是对象，右边是类，返回类型是Boolean类型。
             * 它的具体作用是测试左边的对象是否是右边类或者该类的子类创建的实例对象，是，则返回true，否则返回false
             * */
            if (body instanceof String) {  //响应体类型的判断
                redisValue = (String) body;
            } else {
                // 对象--> 转Json格式字符串
                redisValue = JSON.toJSONString(body);
            }


            //需要 HttpServletRequest request,  做一个强转
            String redisKey = RedisCacheInterceptor.creatRedisKey(((ServletServerHttpRequest) request).getServletRequest());

            //获取Cache注解
            Cache cache = returnType.getMethodAnnotation(Cache.class);

            //缓存单位时间设置成秒    Long.valueOf--->包装类,装箱     parseLong:-->基本数据类型拆箱
            redisTemplate.opsForValue().set(redisKey, redisValue, Long.parseLong(cache.time()), TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body;
    }
}
