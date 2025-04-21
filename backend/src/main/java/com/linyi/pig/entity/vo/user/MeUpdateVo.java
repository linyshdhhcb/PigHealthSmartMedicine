package com.linyi.pig.entity.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: linyi
 * @Date: 2025/2/25
 * @ClassName: MeUpdateVo
 * @Version: 1.0
 * @Description: 用户自己修改信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户自己修改信息")
public class MeUpdateVo {

    /**
     * 主键ID
     */
    private Serializable id;

    /**
     * 用户的真实名字
     */
    @TableField("user_name")
    @Schema(name = "userName",description = "用户的真实名字",type = "varchar")
    private String userName;


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
     * 用户头像
     */
    @TableField("img_path")
    @Schema(name = "imgPath",description = "用户头像",type = "varchar")
    private String imgPath;
}
