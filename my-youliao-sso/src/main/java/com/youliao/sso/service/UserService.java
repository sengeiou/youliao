package com.youliao.sso.service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youliao.commons.mapper.UserMapper;
import com.youliao.commons.pojo.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Lenny
 * @create 2021/11/20 4:17
 * @Description:
 */

@Service
@Slf4j
public class UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public String login(String phone, String code) {
        //从redis中获取key
        String redisKey = "CHECK_CODE_" + phone;
        String redisCode = redisTemplate.opsForValue().get(redisKey);

        if (StringUtils.equals(code, redisCode)) {
            //登录成功
            redisTemplate.delete(redisKey);
        }
        if (!StringUtils.equals(code, redisCode)) {
            //登录失败
            return null;
        }

        //查询用户信息,是否为新用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", phone);

        //查询用户信息
        User user = userMapper.selectOne(queryWrapper);
        //默认不是新用户
        boolean isNew = false;

        if (user == null) {
            //注册用户
            user = new User();
            user.setMobile(phone);
            //初始化密码123456
            user.setPassword(DigestUtils.md5Hex("123456"));
            userMapper.insert(user);
            isNew = true;
        }

        //todo:注册环信

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        //生成token
        String token = Jwts.builder().
                setClaims(claims).
                signWith(SignatureAlgorithm.HS256, secret).
                //token过期时间12小时--->plusHours
                setExpiration(new DateTime().plusHours(12).toDate()).
                compact();
            /*compact() 生成JWT。过程如下：
                载荷校验，前文已经提及。
                获取key。如果是keyBytes则通过keyBytes及算法名生成key对象。
                将所使用签名算法写入header。如果使用压缩，将压缩算法写入header。
                将Json形式的header转为bytes，再Base64编码
                将Json形式的claims转为bytes，如果需要压缩则压缩，再进行Base64编码
                拼接header和claims。如果签名key为空，则不进行签名(末尾补分隔符" . ")；
                如果签名key不为空，以拼接的字符串作为参数，按照指定签名算法进行签名计算签名部分
                sign(String jwtWithoutSignature)，签名部分同样也会进行Base64编码。
              */

        System.out.println(token);

        try {
            HashMap<String, Object> msg = new HashMap<>();
            msg.put("id", user.getId());
            //data-->date.  写错了
            msg.put("date", System.currentTimeMillis());

            rocketMQTemplate.convertAndSend("youlaio-sso-login", msg);

        } catch (MessagingException e) {
            log.error("发送消息失败", e);
        }
        //8、返回token和是否是新用户的标识：两者之间使用|作为一个分隔符，后端获取分割出对应的信息
        return token + "|" + isNew;
    }


    //解析token
    public User queryUserByToken(String token) {
        try {
            // 通过token解析数据
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            User user = new User();
            //Long.valueOf(参数)是将参数转换成long的包装类——Long；也就是把基本数据类型转换成包装类。
            user.setId(Long.valueOf(body.get("id").toString()));

            //需要返回user对象中的mobile，需要查询数据库获取到mobile数据
            //如果每次都查询数据库，必然会导致性能问题，需要对用户的手机号进行缓存操作
            //数据缓存时，需要设置过期时间，过期时间要与token的时间一致
            //如果用户修改了手机号，需要同步修改redis中的数据

            String redisKey = "YOULIAO_USER_MOBILE_" + user.getId();
            if(redisTemplate.hasKey(redisKey)){
                String mobile = redisTemplate.opsForValue().get(redisKey);
                user.setMobile(mobile);
            }else {
                //查询数据库
                User u = userMapper.selectById(user.getId());
                user.setMobile(u.getMobile());

                //将手机号写入到redis中
                //在jwt中的过期时间的单位为：秒
                //Long.valueOf : 底层做了一个包装类,  使用 Long.parseLong好一些
                long timeout = Long.parseLong(body.get("exp").toString()) * 1000 - System.currentTimeMillis();
                redisTemplate.opsForValue().set(redisKey, u.getMobile(), timeout, TimeUnit.MILLISECONDS);
            }

            return user;
        } catch (ExpiredJwtException e) {
            log.info("token已经过期！ token = " + token);
        } catch (Exception e) {
            log.error("token不合法！ token = "+ token, e);
        }
        return null;
    }
}