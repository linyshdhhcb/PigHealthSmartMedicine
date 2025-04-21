package com.linyi.pig.entity.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: linyi
 * @Date: 2025/2/25
 * @ClassName: RegisterVo
 * @Version: 1.0
 * @Description: 注册实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "注册实体")
public class RegisterVo extends CodeVo {

    /**
     * 用户账号
     */
    @NotBlank(message = "账号不能为空")
    @TableField("user_account")
    @Schema(name = "userAccount",description = "用户账号",type = "varchar")
    private String userAccount;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @TableField("user_pwd")
    @Schema(name = "userPwd",description = "用户密码",type = "varchar")
    private String userPwd;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(name = "userPwds",description = "用户密码确认",type = "varchar")
    private String userPwds;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @TableField("user_email")
    @Schema(name = "userEmail",description = "用户邮箱",type = "varchar")
    private String userEmail;
}
