package com.linyi.pig.entity.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linyi.pig.common.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: UserQueryVo
* @Version: 1.0
* @Description: 用户查询实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户查询实体")
public class UserQueryVo extends PageResponse implements Serializable {

    /**
     * 用户账号
     */
    @TableField("user_account")
    @Schema(name = "userAccount",description = "用户账号",type = "varchar")
    private String userAccount;

    /**
     * 用户的真实名字
     */
    @TableField("user_name")
    @Schema(name = "userName",description = "用户的真实名字",type = "varchar")
    private String userName;


    /**
     * 用户年龄-max
     */
    @Schema(name = "userAgeMax",description = "用户年龄",type = "int")
    private Integer userAgeMax;
    /**
     * 用户年龄-min
     */
    @Schema(name = "userAgeMin",description = "用户年龄",type = "int")
    private Integer userAgeMin;

    /**
     * 用户性别
     */
    @TableField("user_sex")
    @Schema(name = "userSex",description = "用户性别",type = "varchar")
    private String userSex;

    /**
     * 用户邮箱
     */
    @TableField("user_email")
    @Schema(name = "userEmail",description = "用户邮箱",type = "varchar")
    private String userEmail;

    /**
     * 手机号
     */
    @TableField("user_tel")
    @Schema(name = "userTel",description = "手机号",type = "varchar")
    private String userTel;

    /**
     * 角色状态，1管理员，0普通用户
     */
    @TableField("role_status")
    @Schema(name = "roleStatus",description = "角色状态，1管理员，0普通用户",type = "int")
    private Integer roleStatus;



}
