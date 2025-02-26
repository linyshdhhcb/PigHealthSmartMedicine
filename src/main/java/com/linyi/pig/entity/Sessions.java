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
* @Date: 2025-02-26 08:42:14
* @ClassName: Sessions
* @Version: 1.0
* @Description: 
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sessions")
@Schema(name = "")
public class Sessions implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "会话ID",type = "varchar")
    private String id;

    /**
     * 对话上下文
     */
    @TableField("context")
    @Schema(name = "context",description = "对话上下文",type = "text")
    private String context;

    /**
     * 用户id
     */
    @TableField("user_id")
    @Schema(name = "userId",description = "用户id",type = "int")
    private Integer userId;

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
