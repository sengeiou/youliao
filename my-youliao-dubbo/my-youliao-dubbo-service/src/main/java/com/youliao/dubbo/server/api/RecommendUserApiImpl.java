package com.youliao.dubbo.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.youliao.dubbo.server.pojo.RecommendUser;
import com.youliao.dubbo.server.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author Lenny
 * @create 2021/11/26 18:31
 * @Description:
 */

@Service(version = "1.0.0") //申明这是一个dubbo服务
public class RecommendUserApiImpl implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser queryWithMaxScore(Long userId) {
        //查询出分数最高的,限制一个
        Query query = Query.query(Criteria.where("toUserId").is(userId))
                .with(Sort.by(Sort.Order.desc("score"))).limit(1);

        System.out.println("test");

        return mongoTemplate.findOne(query,RecommendUser.class);
    }

    @Override
    public PageInfo<RecommendUser> queryPageInfo(Long userId, Integer pageNum, Integer pageSize) {

        //分页,排序
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Order.desc("score")));

        //查询参数
        Query query = Query.query(Criteria.where("toUserId").is(userId)).with(pageRequest);

        List<RecommendUser> recommendUserList = mongoTemplate.find(query, RecommendUser.class);

        return new PageInfo<>(0, pageNum, pageNum, recommendUserList);

    }
}
