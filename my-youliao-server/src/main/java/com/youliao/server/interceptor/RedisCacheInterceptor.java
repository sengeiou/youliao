package com.youliao.server.interceptor;

import com.alibaba.fastjson.JSON;
import com.youliao.commons.utils.Cache;
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
 * @Description: 接口拦截器, 实现 Redis缓存
 */

@Component
public class RedisCacheInterceptor implements HandlerInterceptor {

    @Value("${youliao.cache.enable}")
    private boolean enable;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //四个开关
        //1.缓存的全局开关校验
        if (!enable) {

            //一旦返回true,后面的 都不需要走了,
            return true;
        }

        //2.校验handler是否是HandlerMethod( Controller方法对象)
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //3.判断是否为get请求
        if (!((HandlerMethod) handler).hasMethodAnnotation(GetMapping.class)) {
            return true;
        }
        //4.判断 是否 添加了 @Cache 注解
        if (!((HandlerMethod) handler).hasMethodAnnotation(Cache.class)) {
            return true;
        }

        //上述 四步全部 通过,我们实现 "缓存命中"
        String redisKey = creatRedisKey(request);
        String cacheData = redisTemplate.opsForValue().get(redisKey);

        //对 缓存进行一个非空判断
        if (StringUtils.isEmpty(cacheData)) {
            //如果为空,---缓存未命中
            return true;
        }

        //将 缓存数据 做一个响应
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(cacheData);

        //false前执行，后不执行.  即返回false取消当前请求
        return false;
    }

    /**
     * 生成Redis当中的key,
     * 定义一个生成 规则: SERVER_CACHE_DATA_MD5(url + param + token)
     *
     * @param request
     * @return
     */
    public static String creatRedisKey(HttpServletRequest request) throws Exception {
        /*
            request.getRequestURL() 返回全路径
            request.getRequestURI() 返回除去host（域名或者ip）部分的路径
            request.getContextPath() 返回工程名部分，如果工程映射为/，此处返回则为空
            request.getServletPath() 返回除去host和工程名部分的路径
            例如：
            request.getRequestURL() http://localhost:8080/jqueryLearn/resources/request.jsp
            request.getRequestURI() /jqueryLearn/resources/request.jsp
            request.getContextPath()/jqueryLearn
            request.getServletPath()/resources/request.jsp
        */
        String uri = request.getRequestURI();
        //拿请求参数.  用 阿里fastJson,  对象序列化成Json格式
        String param = JSON.toJSONString(request.getParameterMap());
        String token = request.getHeader("Authorization");

        //进行拼接
        String data = uri + "_" + param + "_" + token;

        System.out.println("SERVER_CACHE_DATA_" + DigestUtils.md5Hex(data));

        //进行md5加密,返回
        return "SERVER_CACHE_DATA_" + DigestUtils.md5Hex(data);
    }
}

