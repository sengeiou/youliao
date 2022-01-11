package com.youliao.commons.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lenny
 * @create 2021/11/20 2:31
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BasePojo {
    private String mobile;
    private Long id;

    @JsonIgnore
    private String password;

    /*
     * 密码,json序列化时忽略
     *
     * @JsonIgnore 在json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响。
     * 那么最后返回的json数据，将不会包含password这个属性值。
     *
     * 注解失效：
     * 如果注解失效，可能是因为你使用的是fastJson，尝试使用对应的注解来忽略字段，
     * 注解为：@JSONField(serialize = false)，使用方法一样。
     */


}
