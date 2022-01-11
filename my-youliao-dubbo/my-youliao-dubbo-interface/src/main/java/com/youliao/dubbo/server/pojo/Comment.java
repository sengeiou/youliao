package com.youliao.dubbo.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Lenny
 * @create 2021/12/30 1:15
 * @Description:   评论表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "quanzi_comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 123459867435873L;

    @Id
    private ObjectId id;
    private ObjectId publishId; //发布id
    private Integer commentType;    //评论类型, 1.点赞 2.评论 3.喜欢
    private String content; //评论内容
    private Long userId;    //评论人
    private Long publishUserId; //发布动态用户id
    private Boolean isParent = false;       //是否为父节点, 默认为否
    private ObjectId parentId;      //父节点id
    private Long created;       //发表时间
}
