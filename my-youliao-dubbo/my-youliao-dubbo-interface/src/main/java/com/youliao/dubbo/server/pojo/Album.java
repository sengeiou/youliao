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
 * @create 2022/1/4 22:36
 * @Description: 相册表,用于存储发布的数据,一个用户一张表
 *
 * Serializable是一个空接口，没有什么具体内容，它的目的只是简单的标识一个类的对象可以被序列化。
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "quanzi_album_{userId}")
public class Album implements Serializable {
    private static final long serialVersionUID = 6589320843727L;

    @Id
    private ObjectId id;        //主键id

    private ObjectId publishId;    //发布id

    private Long created;       //发布时间
}
