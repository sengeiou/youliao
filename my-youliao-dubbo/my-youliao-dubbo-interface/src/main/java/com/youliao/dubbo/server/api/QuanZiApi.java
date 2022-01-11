package com.youliao.dubbo.server.api;

import com.youliao.dubbo.server.pojo.Publish;
import com.youliao.dubbo.server.vo.PageInfo;

/**
 * @author Lenny
 * @create 2021/12/30 1:11
 * @Description: '社区'模块的接口Api
 */

public interface QuanZiApi {

    /**
     * 好友动态
     *
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    PageInfo<Publish> queryPublishList(Long id, Integer page, Integer pageSize);

    /**
     * 根据 id 来查询动态
     *
     * @param id 动态id
     * @return
     */
    Publish queryPublishById(String id);

    /**
     * 点赞
     *
     * @param userId    登陆者的id
     * @param publishId 动态发布者的id
     * @return
     */
    Boolean likeCommnet(Long userId, String publishId);

    /**
     * 取消点赞
     *
     * @param userId
     * @param publishId
     * @return
     */
    Boolean disLikeCommnet(Long userId, String publishId);

    /**
     * 查询点赞数量
     *
     * @param publishId
     * @return
     */
    Long queryLikeCount(String publishId);

    /**
     * 查询是否点赞
     *
     * @param userId
     * @param publishId
     * @return
     */
    Boolean queryUserIsLike(Long userId, String publishId);


}
