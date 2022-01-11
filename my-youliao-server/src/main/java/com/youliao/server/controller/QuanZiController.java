package com.youliao.server.controller;

import com.youliao.server.service.QuanZiService;
import com.youliao.server.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lenny
 * @create 2021/12/31 0:20
 * @Description:
 */

@RestController
@RequestMapping("/movements")
public class QuanZiController {

    @Autowired
    private QuanZiService quanZiService;

    @GetMapping
    public PageResult queryFriendsFeed(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return quanZiService.queryFriendsFeed(page, pageSize);
    }

    /**
     * 评论点赞
     *
     * @param publishId 动态发布者的id
     * @return
     */
    @GetMapping("/{id}/like")
    public ResponseEntity<Long> liekComment(@PathVariable("id") String publishId) {
        try {
            //传回来的点赞数量
            Long likeCommentCount = quanZiService.likeComment(publishId);
            if (likeCommentCount != null) {
                return ResponseEntity.ok(likeCommentCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(500).build();
    }
}
