

package com.nageoffer.ai.ragent.user.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 * 定义系统中的用户角色类型
 */
@Getter
public enum UserRole {

    /**
     * 管理员角色
     */
    ADMIN("admin"),

    /**
     * 普通用户角色
     */
    USER("user");

    /**
     * 角色编码
     */
    private final String code;

    /**
     * 构造函数
     *
     * @param code 角色编码
     */
    UserRole(String code) {
        this.code = code;
    }
}
