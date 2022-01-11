package com.youliao.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Lenny
 * @create 2021/11/20 3:13
 * @Description:
 */

@SpringBootApplication
@MapperScan("com.youliao.commons.mapper")//扫描mapper接口,自动注入mapper
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
