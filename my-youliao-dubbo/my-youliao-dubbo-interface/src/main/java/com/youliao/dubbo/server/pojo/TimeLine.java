package com.youliao.dubbo.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.logging.LoggingPermission;

/**
 * @author Lenny
 * @create 2021/12/30 1:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "quanzi_time_line_{userId}")
public class TimeLine implements Serializable {
    private static final long serialVersionUID = -76945627040L;

    @Id
    private ObjectId id;

    private Long userId;        //好友id

    private ObjectId publishId; //发布id

    private Long date; //发布的时间
}
