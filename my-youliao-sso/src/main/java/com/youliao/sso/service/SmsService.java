package com.youliao.sso.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.youliao.sso.config.AliyunSMSConfig;
import com.youliao.sso.vo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author Lenny
 * @create 2021/11/20 3:48
 * @Description:
 */

@Service
@Slf4j
public class SmsService {

    //@Autowired
    //private AliyunSMSConfig aliyunSMSConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */


    /**
     * 发送短信验证码
     * 实现：发送完成短信验证码后，需要将验证码保存到redis中
     *
     * @param phone
     * @return
     */
    public ErrorResult sendCheckCode(String phone) {

        //设置一个redis当中存验证码的key
        String codeKey = "CHECK_CODE_" + phone;

        //从redis当中验证这个key
        Boolean flag = redisTemplate.hasKey(codeKey);
        if (flag) {
            return ErrorResult.builder().errorCode("000000").errorMsg("验证码仍在有效期!").build();
        }

        //阿里云短信服务暂时不可用.设为固定的值123456
        //String code = this.sendSms(phone);
        String code = "123456";
        redisTemplate.opsForValue().set(codeKey,code,Duration.ofMinutes(5));
        //redisTemplate.opsForValue().set(codeKey,code, 5, TimeUnit.MINUTES);

        return null;
    }

    //调用阿里云短信服务发送短信验证码
   /* public String sendSms(String mobile) {
        DefaultProfile profile = DefaultProfile.getProfile(this.aliyunSMSConfig.getRegionId(),
                this.aliyunSMSConfig.getAccessKeyId(), this.aliyunSMSConfig.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        String code = RandomUtils.nextInt(100000, 999999) + "";

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(this.aliyunSMSConfig.getDomain());
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", this.aliyunSMSConfig.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile); //目标手机号
        request.putQueryParameter("SignName", this.aliyunSMSConfig.getSignName()); //签名名称
        request.putQueryParameter("TemplateCode", this.aliyunSMSConfig.getTemplateCode()); //短信模板code
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");//模板中变量替换
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            if (StringUtils.contains(data, "\"Message\":\"OK\"")) {
                //return code;
                return "123456";
            }
            log.info("发送短信验证码失败~ data = " + data);
        } catch (Exception e) {
            log.error("发送短信验证码失败~ mobile = " + mobile, e);
        }
        return null;
    }*/
}
