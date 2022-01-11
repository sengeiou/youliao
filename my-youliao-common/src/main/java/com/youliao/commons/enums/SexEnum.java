package com.youliao.commons.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 从数据库中读取枚举值
 * 借助MyBatis-Plus可以很容易的实现这一点。
 * 首先需要在配置文件中加入type-enums-package指定枚举的扫描包，
 * MyBatis-Plus将为包内(包含子包)所有枚举进行适配，可以使用逗号或封号分隔多个包名。
 * mybatis-plus:
 * type-enums-package: [枚举包][,|;][枚举包]
 * 接着在枚举类中指定数据库值所对应的属性。这里可以采用两种方式。
 * 实现官方提供的IEnum接口，接口中的getValue方法与数据库值对应的属性。
 */
public enum SexEnum implements IEnum<Integer> {
    MAN(1, "男"),
    WOMAN(2, "女"),
    UNKNOWN(3, "未知");

    private int value;
    private String desc;


    SexEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.desc;
    }

    public static String getSexByValue(int code) {
        String sex = "未知";
        for (SexEnum sexEnum : values()) {
            if (sexEnum.getValue() == code) {
                sex = sexEnum.desc;
            }
        }
        return sex;
    }
}
