package com.youliao.server.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.youliao.commons.pojo.User;
import com.youliao.commons.utils.UserThreadLocal;
import com.youliao.dubbo.server.api.QuanZiApi;
import com.youliao.dubbo.server.pojo.Publish;
import com.youliao.dubbo.server.vo.PageInfo;
import com.youliao.server.vo.PageResult;
import com.youliao.server.vo.QuanZiVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lenny
 * @create 2021/12/31 0:32
 * @Description:
 */

@Service
public class QuanZiService {

    @Reference(version = "1.0.0")
    private QuanZiApi quanZiApi;

    /**
     * 好友动态
     *
     * @param page
     * @param pageSize
     * @return
     */
    public PageResult queryFriendsFeed(Integer page, Integer pageSize) {

        PageResult pageResult = new PageResult();
        pageResult.setPage(page);
        pageResult.setPagesize(pageSize);

        //Token统一校验, 拿到User,  -->  ThreadLocal 代替
        User user = UserThreadLocal.getLocal();

        //查询 好友时间线表, 得到好友动态Feed
        PageInfo<Publish> pageInfo = quanZiApi.queryPublishList(user.getId(), page, pageSize);

        //数据列表
        List<Publish> records = pageInfo.getRecords();
        if (CollUtil.isEmpty(records)) {
            return pageResult;
        }

        //records不为空,  封装数据, 用于响应 ,客户端
        for (Publish record : records) {
            QuanZiVo quanZiVo = new QuanZiVo();

            //MongoDB id 转换为 16进制, (MOgo的一种属性)
            quanZiVo.setId(record.getId().toHexString());

            //动态文本
            quanZiVo.setTextContent(record.getText());

            //发布的图片
            quanZiVo.setImageContent(record.getMedias().toArray(new String[]{}));

            //看到 动态的 发布者 的 id
            quanZiVo.setUserId(record.getUserId());

            //发布时间, 需要做些处理
            //发布距离 查看的时间.   -->  自定义一个时间的工具类
            //quanZiVo.setCreateDate(String.valueOf(record.getCreated()));
            quanZiVo.setCreateDate(String.valueOf(record.getCreated()));
        }

        return  null;


    }

    /**
     * 点赞
     *
     * @param publishId 发布者id
     * @return 点赞数量
     */
    public Long likeComment(String publishId) {
        //校验token
        return null;
    }


}
