package com.youliao.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lenny
 * @create 2021/11/25 16:12
 * @Description:
 */

//server依赖了interface,里面有MongoDB依赖,exclude解决: springboot mdb 依赖而不会 对其 做 "自动创建"(  SpringBoot有自动创建的特性)
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@MapperScan("com.youliao.commons.mapper")
//@ComponentScan(basePackages = "com.youliao")
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
