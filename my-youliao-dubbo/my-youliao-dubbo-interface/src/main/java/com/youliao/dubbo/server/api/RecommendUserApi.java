package com.youliao.dubbo.server.api;

import com.youliao.dubbo.server.pojo.RecommendUser;
import com.youliao.dubbo.server.vo.PageInfo;

/**
 * @author Lenny
 * @create 2021/11/25 16:43
 * @Description:
 */

public interface RecommendUserApi {

    /**
     * 查询出一位得分最高的用户
     *
     * @param userId
     * @return
     */
    RecommendUser queryWithMaxScore(Long userId);

    /**
     * 按照得分进行排序
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<RecommendUser> queryPageInfo(Long userId, Integer pageNum, Integer pageSize);


}
