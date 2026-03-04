

package com.nageoffer.ai.ragent.user.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_user")
public class UserDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    /**
     * 用户头像 URL
     */
    private String avatar;

    /**
     * 角色：admin / user
     */
    private String role;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色类型：FARMER-养殖户，VETERINARIAN-兽医，ADMIN-管理员
     */
    private String roleType;

    /**
     * 执业证书号（兽医）
     */
    private String licenseNumber;

    /**
     * 所属机构
     */
    private String organization;

    /**
     * 专业领域（兽医）
     */
    private String specialization;

    /**
     * 从业年限
     */
    private Integer yearsExperience;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer deleted;
}
