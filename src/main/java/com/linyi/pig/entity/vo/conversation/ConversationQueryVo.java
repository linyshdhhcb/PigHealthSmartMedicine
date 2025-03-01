package com.linyi.pig.entity.vo.conversation;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.linyi.pig.common.model.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* @Author: linyi
* @Date: 2025-02-26 13:27:06
* @ClassName: ConversationQueryVo
* @Version: 1.0
* @Description: 对话查询实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "对话查询实体")
public class ConversationQueryVo extends PageResponse implements Serializable {
    /**
     * 对话ID
     */
    @TableField("id")
    @Schema(name = "id",description = "对话ID",type = "int")
    private Serializable id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(name = "userId",description = "用户ID",type = "int")
    private Integer userId;

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
     * 对话时间-开始
     */
    @Schema(name = "startConversationTime",description = "对话时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startConversationTime;

    /**
     * 对话时间-结束
     */
    @Schema(name = "endConversationTime",description = "对话时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endConversationTime;

    /**
     * AI模型名称
     */
    @TableField("model_name")
    @Schema(name = "modelName",description = "AI模型名称",type = "varchar")
    private String modelName;

    /**
     * AI响应时间（秒）大于
     */
    @TableField("response_time")
    @Schema(name = "responseTime",description = "AI响应时间（秒）",type = "decimal")
    private BigDecimal responseTime;

}
