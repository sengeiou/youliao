package com.youliao.dubbo.server.enums;

/**
 * @author Lenny
 * @create 2021/12/30 0:53
 * @Description:  定义'评论'的类型
 */

public enum CommentType {
    /**
     * 1,喜欢 2,评论 3,点赞
     */
    LIKE(1),COMMENT(2),LOVE(3);

    final int type;

    CommentType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
