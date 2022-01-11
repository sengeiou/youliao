package com.youliao.sso.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Lenny
 * @create 2021/11/20 3:19
 * @Description:
 */
@Data
@Configuration
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties(prefix = "aliyun.sms")//读取配置文件中的前缀名为aliyun的属性
public class AliyunSMSConfig {
    private String regionId;
    private String accessKeyId;
    private String accessKeySecret;
    private String domain;
    private String signName;
    private String templateCode;
    
}
