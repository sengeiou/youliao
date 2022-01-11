package com.youliao.commons.utils;

import com.youliao.commons.pojo.User;

/**
 * @author Lenny
 * @create 2021/12/31 1:05
 * @Description:
 */

public class UserThreadLocal {
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    /**
     *  私有化这个类,确保不会去 new它
     */
    private UserThreadLocal() {}

    public static void setLocal(User user) {
        LOCAL.set(user);
    }

    public static User getLocal() {
        return LOCAL.get();
    }

    /**
     * 移除当前线程当中的User对象
     */
    public static void removeLocal() {
        LOCAL.remove();
    }
}
