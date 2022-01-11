package com.youliao.server.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.youliao.dubbo.server.pojo.RecommendUser;
import com.youliao.dubbo.server.vo.PageInfo;
import com.youliao.server.vo.TodayBest;
import org.springframework.stereotype.Service;
import com.youliao.dubbo.server.api.RecommendUserApi;

/**
 * @author Lenny
 * @create 2021/11/25 17:39
 * @Description:
 */

@Service
public class RecommendUserService {

    @Reference(version = "1.0.0")//声明这是一个远程调用
    private RecommendUserApi recommendUserApi;

    /**
     * 查询"今日佳人"
     *
     * @param id
     * @return
     */
    public TodayBest queryTodayBest(Long id) {
        RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(id);
        if (recommendUser == null) {
            return null;
        }

        TodayBest todayBest = new TodayBest();
        //设置当前推荐人的id为"今日佳人"的id
        todayBest.setId(recommendUser.getUserId());

        //缘分值, 向下取整
        double score = Math.floor(recommendUser.getScore());
        todayBest.setFateValue(Double.valueOf(score).longValue());
        System.out.println(recommendUser.getUserId());

        return todayBest;
    }

    /**
     * 查询推荐好友列表
     *
     * @param id
     * @param page
     * @param pagesize
     * @return
     */
    public PageInfo<RecommendUser> queryRecommendationList(Long id, Integer page, Integer pagesize) {
        return recommendUserApi.queryPageInfo(id, page, pagesize);
    }
}
