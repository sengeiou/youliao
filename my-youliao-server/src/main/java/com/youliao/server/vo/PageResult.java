package com.youliao.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author Lenny
 * @create 2021/11/26 21:21
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult {

    private Integer counts = 0;//总记录数
    private Integer pagesize = 0;//页大小
    private Integer pages = 0;//总页数
    private Integer page = 0;//当前页码
    private List<?> items = Collections.emptyList(); //列表

}
