package com.youliao.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.youliao.commons.pojo.User;
import com.youliao.commons.pojo.UserInfo;
import com.youliao.dubbo.server.pojo.RecommendUser;
import com.youliao.dubbo.server.vo.PageInfo;
import com.youliao.server.vo.PageResult;
import com.youliao.server.vo.RecommendUserQueryParam;
import com.youliao.server.vo.TodayBest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Lenny
 * @create 2021/11/25 16:59
 * @Description:
 */

@Service
public class TodayBestService {

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendUserService recommendUserService;

    @Autowired
    private UserInfoService userInfoService;

    @Value("${youliao.sso.default.user}")
    private Long defaultId;

    public TodayBest queryTodayBest(String token) {
        //校验token是否有效
        User user = userService.queryUserByToken(token);

        if (user == null) {
            //token非法或者过期
            return null;
        }

        //校验通过,查询"今日佳人".
        TodayBest todayBest = recommendUserService.queryTodayBest(user.getId());

        if (todayBest == null) {
            //给出一个默认的,给不出 推荐的情况下一个默认的
            todayBest = new TodayBest();
            todayBest.setId(defaultId);
            todayBest.setFateValue(80L);

        }

        //能查到的话,构造对象,补全信息
        UserInfo userInfo = userInfoService.queryUserInfoByUserId(todayBest.getId());
        if (userInfo == null) {
            return null;
        }

        todayBest.setAge(userInfo.getAge());
        todayBest.setAvatar(userInfo.getLogo());
        todayBest.setGender(userInfo.getSex().getValue() == 1 ? "man" : "woman");
        todayBest.setNickname(userInfo.getNickName());
        todayBest.setTags(StringUtils.split(userInfo.getTags(), ','));

        return todayBest;
    }

    /**
     * 查询推好友推荐列表
     *
     * @param token
     * @param params
     * @return
     */
    public PageResult queryRecommendationList(String token, RecommendUserQueryParam params) {
        //校验token,通过SSO接口
        User user = userService.queryUserByToken(token);

        if (user == null) {
            //token非法或者过期
            return null;
        }

        PageResult pageResult = new PageResult();
        pageResult.setPage(params.getPage());
        pageResult.setPagesize(params.getPagesize());

        //MongoDB表 recommend_user 得到的
        PageInfo<RecommendUser> pageInfo = recommendUserService.queryRecommendationList(user.getId(), params.getPage(), params.getPagesize());

        List<RecommendUser> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            //查到的为空
            return pageResult;
        }

        //不为空,继续进行填充,
        //Set集合, 无序, 不重复
        HashSet<Long> userIds = new HashSet<>();
        //得到新的没有重复id 的集合对象
        for (RecommendUser record : records) {
            userIds.add(record.getUserId());
        }

        //4.2动态条件的组装...对比 MySql进行操作,UserInfo表
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        //确保userIds在 指定的userInfo表  user_id列 中
        queryWrapper.in("user_id", userIds);

        //推荐列表 人 索要满足的一些条件
        //params.getGender()----man,woman
        if (StringUtils.isNotEmpty(params.getGender())) {
            //性别不为空
            //男生,设置为1.   女生,设置为2.
            //queryWrapper.eq("sex", ("man".equals(params.getGender())) ? 1 : 2);
            queryWrapper.eq("sex", (StringUtils.equals(params.getGender(), "man")) ? 1 : 2);
        }
        if (StringUtils.isNotEmpty(params.getCity())) {
            //设置城市   不是qw.eq,   like:模糊查询  %北京城区%
            queryWrapper.like("city", params.getCity());
        }
        if (params.getAge() != null) {      //StringUtils.isNotEmpty  只能比较 CharSequence类型的,String,SBuffer,SBuilder,
            //有缘人的年龄需要小于等于   30岁
            queryWrapper.le("age", params.getAge());
        }

        //有了这些条件之后,构造查询
        List<UserInfo> userInfoList = userInfoService.queryUserInfoList(queryWrapper);

        List<TodayBest> todayBests = new ArrayList<>();
        //将查到的有缘人  封装进  todayBests
        for (UserInfo userInfo : userInfoList) {

            TodayBest todayBest = new TodayBest();
            todayBest.setId(userInfo.getId());
            todayBest.setAvatar(userInfo.getLogo());
            todayBest.setNickname(userInfo.getNickName());
            todayBest.setGender(userInfo.getSex().getValue() == 1 ? "man": "woman");
            todayBest.setAge(userInfo.getAge());
            todayBest.setTags(StringUtils.split(userInfo.getTags(),','));

            //缘分值
            for (RecommendUser record : records) {
                if (record.getUserId().longValue() == userInfo.getUserId().longValue()) {  //longValue 包装数据 变为 进本数据 类型 Long --> long
                    //取整操作  向下
                    double score = Math.floor(record.getScore());
                    //设置 缘分值
                    todayBest.setFateValue(Double.valueOf(score).longValue());
                    //执行完成,直接 跳出
                    break;
                }
            }

            //所有操作 完后之后,  得到 今日佳人 的 集合
            todayBests.add(todayBest);
        }

        //将缘分值进行倒序排列
        todayBests.sort((((o1, o2) -> new Long(o2.getFateValue() - o1.getFateValue()).intValue())));

        //todayBests.stream().sorted(Comparator.comparing(TodayBest::getFateValue).reversed());

        //将今日佳人  封装 到PageResult 返回
        pageResult.setItems(todayBests);

        return pageResult;

    }
}
