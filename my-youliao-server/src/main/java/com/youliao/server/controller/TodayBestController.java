package com.youliao.server.controller;

import com.youliao.commons.utils.Cache;
import com.youliao.server.service.TodayBestService;
import com.youliao.server.vo.PageResult;
import com.youliao.server.vo.RecommendUserQueryParam;
import com.youliao.server.vo.TodayBest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lenny
 * @create 2021/11/25 16:51
 * @Description:
 */

@RestController
@Slf4j
@RequestMapping("tanhua")
public class TodayBestController {

    @Autowired
    private TodayBestService todayBestService;

    @GetMapping("/todayBest")
    public ResponseEntity<TodayBest> queryTodayBest(@RequestHeader("Authorization") String token) {

        TodayBest todayBest = todayBestService.queryTodayBest(token);

        try {
            if (todayBest != null) {
                return ResponseEntity.ok(todayBest);
            }
        } catch (Exception e) {
            log.error("查询今日佳人出错!token=" + token, e);
        }

        return ResponseEntity.status(500).body(null);
    }

    /**
     * 查询推荐好友推荐列表
     *
     * @param token
     * @param params
     * @return
     */
    @GetMapping("/recommendation")

    @Cache(time = "60")  //开启缓存(自定义)  可以指定多少秒,  默认是60s
    public ResponseEntity<PageResult> queryRecommendation(@RequestHeader("Authorization") String token,
                                                          RecommendUserQueryParam params) {
        try {
            PageResult result = todayBestService.queryRecommendationList(token, params);
            //这里写错了,  写成==
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error("查询推荐列表出错!token=" + token, e);
        }
        return ResponseEntity.status(500).body(null);

    }
}
