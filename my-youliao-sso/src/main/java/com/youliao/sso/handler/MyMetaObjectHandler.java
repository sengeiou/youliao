package com.youliao.sso.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Lenny
 * @create 2021/11/20 2:50
 * @Description: 对填充的字段进行处理
 */


@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object created = getFieldValByName("created", metaObject);
        // 如果created字段为空，填充当前时间
        if (created == null) {
            setFieldValByName("created",new Date(), metaObject);
        }

        Object updated = getFieldValByName("updated", metaObject);
        if (updated == null) {
            setFieldValByName("updated",new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新数据时，直接更新字段
        setFieldValByName("updated",new Date(), metaObject);
    }
}
