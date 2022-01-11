package com.youliao.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youliao.commons.mapper.UserInfoMapper;
import com.youliao.commons.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lenny
 * @create 2021/11/25 18:05
 * @Description:
 */

@Service
public class UserInfoService {

   @Autowired
   private UserInfoMapper userInfoMapper;
   /**
    * 通过id拿到个人信息
    * @param id 今日佳人的id
    * @return
    */
   public UserInfo queryUserInfoByUserId(Long id) {
      QueryWrapper<UserInfo> qw = new QueryWrapper<>();
      qw.eq("user_id", id);
      return userInfoMapper.selectOne(qw);
   }

    /**
     * 查询推荐人 列表
     *
     * @param queryWrapper 构造 的 查询 条件
     * @return
     */
    public List<UserInfo> queryUserInfoList(QueryWrapper queryWrapper) {
        return userInfoMapper.selectList(queryWrapper);
    }
}
