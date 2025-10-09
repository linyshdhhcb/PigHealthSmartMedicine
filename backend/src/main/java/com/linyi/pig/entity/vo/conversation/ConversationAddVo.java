package com.linyi.pig.entity.vo.conversation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Author: linyi
* @Date: 2025-02-26 13:27:06
* @ClassName: ConversationAddVo
* @Version: 1.0
* @Description: 对话新增实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "对话新增实体")
public class ConversationAddVo implements Serializable {

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(name = "userId",description = "用户ID",type = "int")
    private Integer userId;

    /**
     * 会话ID
     */
    @TableField("session_id")
    @Schema(name = "sessionId",description = "会话ID",type = "bigint")
    private Long sessionId;

    /**
     * 用户输入
     */
    @TableField("user_input")
    @Schema(name = "userInput",description = "用户输入",type = "text")
    private String userInput;

    /**
     * AI回复
     */
    @TableField("ai_response")
    @Schema(name = "aiResponse",description = "AI回复",type = "text")
    private String aiResponse;

    /**
     * 对话时间
     */
    @TableField("conversation_time")
    @Schema(name = "conversationTime",description = "对话时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date conversationTime;

    /**
     * AI模型名称
     */
    @TableField("model_name")
    @Schema(name = "modelName",description = "AI模型名称",type = "varchar")
    private String modelName;

    /**
     * AI响应时间（秒）
     */
    @TableField("response_time")
    @Schema(name = "responseTime",description = "AI响应时间（秒）",type = "decimal")
    private BigDecimal responseTime;

}
