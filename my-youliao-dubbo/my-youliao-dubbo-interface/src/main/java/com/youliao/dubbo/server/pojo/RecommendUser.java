package com.youliao.dubbo.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import sun.dc.pr.PRError;

import java.io.Serializable;

/**
 * @author Lenny
 * @create 2021/11/23 20:50
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "recommend_user")//指定MongoDB封装实体类 对应的 集合 名称,对应 MySql  TableForName
public class RecommendUser implements Serializable {

    /**
     * serialVersionUID 用来表明类的不同版本间的兼容性。
     * 如果你修改了此类, 要修改此值。否则以前用老版本的类序列化的类恢复时会出错。
     * 序列化的时候，被序列化的类要有一个唯一标记。客户端和服务端必须需要同一个对象，serialVersionUID的唯一值判定其为同一个对象。
     * 后面的号码是自动生成的，只要是唯一的就行，通常为1，此行语句去掉在练习的时候也没有什么影响，只不过此实例类会报一个警告。
     * 将鼠标放到警告上，选择第一个解决方案，就会重新加上此行语句，后面的数字和原先的可能会不一样
     * <p>
     * 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
     */
    private static final long serialVersionUID = -4296017160071130962L;

    /**
     * 主键，不可重复，自带索引，可以在定义的列名上标注，需要自己生成并维护不重复的约束。
     * 如果自己不设置@Id主键，mongo会自动生成一个唯一主键，并且插入时效率远高于自己设置主键。
     * 原因可参考上一篇mongo和mysql的性能对比。
     * 在实际业务中不建议自己设置主键，应交给mongo自己生成，自己可以设置一个业务id，如int型字段，用自己设置的业务id来维护相关联的表。
     */
    @Id
    private Object id;//    主键id,

    /**
     * 声明该字段需要加 "索引"，加索引后以该字段为条件检索将大大提高速度。
     * 唯一索引的话是@Indexed(unique = true)。
     * 也可以对数组进行索引，如果被索引的列是数组时，MongoDB会索引这个数组中的每一个元素。
     * 也可以对整个Document进行索引，排序是预定义的按插入BSON数据的先后升序排列。
     * 也可以对关联的对象的字段进行索引，譬如User对关联的address.city进行索引。（注解怎么写还不清楚，待查）
     */
    @Indexed
    private Long userId; //推荐用户的id

    private Long toUserId; //用户id

    @Indexed
    private Double score; //推荐得分

    private String date; //日期


}
