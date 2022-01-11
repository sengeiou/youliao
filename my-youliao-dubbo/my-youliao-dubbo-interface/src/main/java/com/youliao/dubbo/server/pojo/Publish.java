package com.youliao.dubbo.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lenny
 * @create 2021/12/30 1:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "quanzi_publish")
public class Publish implements Serializable {
    private static final long serialVersionUID = -647823409861L;

    @Id
    private ObjectId id;              //主键id
    private Long pid;               //发布id
    private Long userId;            //登录(操作人)的id
    private String text;            //文本
    private List<String> medias;    //媒体数据，图片或小视频 url
    private Integer seeType;        // 谁可以看，1-公开，2-私密，3-部分可见，4-不给谁看
    private List<Long> seeList;     //部分可见的列表
    private List<Long> notSeeList;  //不给谁看的列表
    private String longitude;       //经度
    private String latitude;        //纬度
    private String locationName;    //位置名称
    private Long created;           //发布时间

}
