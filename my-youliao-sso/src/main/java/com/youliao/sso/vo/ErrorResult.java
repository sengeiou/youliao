package com.youliao.sso.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Lenny
 * @create 2021/11/20 3:38
 * @Description:
 */

@Data
@Builder//@Builder声明实体，表示可以进行Builder方式初始化，
// @Value注解，表示只公开getter，对所有属性的setter都封闭，即private修饰，所以它不能和@Builder现起用
public class ErrorResult {
    private String errorCode;
    private String errorMsg;
}
