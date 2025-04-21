package com.linyi.pig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: User
* @Version: 1.0
* @Description: 用户
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
@Schema(name = "用户")
public class User implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 用户主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "用户主键id",type = "int")
    private Integer id;

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
     * 用户密码
     */
    @TableField("user_pwd")
    @Schema(name = "userPwd",description = "用户密码",type = "varchar")
    private String userPwd;

    /**
     * 用户年龄
     */
    @TableField("user_age")
    @Schema(name = "userAge",description = "用户年龄",type = "int")
    private Integer userAge;

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

    /**
     * 用户头像
     */
    @TableField("img_path")
    @Schema(name = "imgPath",description = "用户头像",type = "varchar")
    private String imgPath;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @Schema(name = "createTime",description = "创建时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @Schema(name = "updateTime",description = "更新时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
