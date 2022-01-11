package com.youliao.dubbo.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Lenny
 * @create 2021/11/23 21:26
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = -2316347423893L;

    /**
     * 总条数
     */
    private Integer total;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 一页显示的大小
     */
    private Integer pageSize;

    /**
     * 数据列表
     *
     * emptyList()
     * 作用：返回一个空的List（使用前提是不会再对返回的list进行增加和删除操作）；
     * 好处：
     * 1. new ArrayList()创建时有初始大小，占用内存，emptyList()不用创建一个新的对象，可以减少内存开销；
     * 2. 方法返回一个emptyList()时，不会报空指针异常，如果直接返回Null，没有进行非空判断就会报空指针异常；
     * 注意：此List与常用的List不同，它是Collections类里的静态内部类，在继承AbstractList后并没有实现add()、remove()等方法，所以返回的List不能进行增加和删除元素操作。
     *
     * 如果需要对collections.emptyList()进行增删操作的话，就需要将collections.emptyList()转换成ArrayList()进行操作。
     */
    private List<T> records = Collections.emptyList();




}
