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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("conversation_session")
@Schema(name = "会话主表")
public class ConversationSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "会话ID", type = "bigint")
    private Long id;

    @TableField("user_id")
    @Schema(name = "userId", description = "用户ID", type = "int")
    private Integer userId;

    @TableField("title")
    @Schema(name = "title", description = "会话标题", type = "varchar")
    private String title;

    @TableField("status")
    @Schema(name = "status", description = "状态: 1-进行中, 2-已结束", type = "tinyint")
    private Integer status;

    @TableField("model_name")
    @Schema(name = "modelName", description = "AI模型名称", type = "varchar")
    private String modelName;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
