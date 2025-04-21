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
* @ClassName: Feedback
* @Version: 1.0
* @Description: 反馈
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("feedback")
@Schema(name = "反馈")
public class Feedback implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "主键ID",type = "int")
    private Integer id;

    /**
     * 反馈用户
     */
    @TableField("name")
    @Schema(name = "name",description = "反馈用户",type = "varchar")
    private String name;

    /**
     * 邮箱地址
     */
    @TableField("email")
    @Schema(name = "email",description = "邮箱地址",type = "varchar")
    private String email;

    /**
     * 反馈标题
     */
    @TableField("title")
    @Schema(name = "title",description = "反馈标题",type = "varchar")
    private String title;

    /**
     * 反馈内容
     */
    @TableField("content")
    @Schema(name = "content",description = "反馈内容",type = "text")
    private String content;

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
