

package com.nageoffer.ai.ragent.user.controller.request;

import lombok.Data;

/**
 * 修改密码请求
 */
@Data
public class ChangePasswordRequest {

    /**
     * 当前密码
     */
    private String currentPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
