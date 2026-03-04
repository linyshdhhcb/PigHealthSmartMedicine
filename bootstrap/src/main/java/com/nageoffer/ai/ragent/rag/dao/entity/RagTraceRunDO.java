

package com.nageoffer.ai.ragent.rag.dao.entity;

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

/**
 * RAG Trace 运行记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_rag_trace_run")
public class RagTraceRunDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 全局链路ID
     */
    private String traceId;

    /**
     * 链路名称
     */
    private String traceName;

    /**
     * 触发入口方法
     */
    private String entryMethod;

    private String conversationId;

    private String taskId;

    private String userId;

    /**
     * RUNNING / SUCCESS / ERROR
     */
    private String status;

    private String errorMessage;

    private Date startTime;

    private Date endTime;

    private Long durationMs;

    /**
     * 预留扩展字段（JSON字符串）
     */
    private String extraData;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer deleted;
}
