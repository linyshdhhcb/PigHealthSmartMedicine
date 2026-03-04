

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
 * RAG Trace 节点记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_rag_trace_node")
public class RagTraceNodeDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String traceId;

    /**
     * 节点ID（唯一）
     */
    private String nodeId;

    /**
     * 父节点ID
     */
    private String parentNodeId;

    /**
     * 节点深度
     */
    private Integer depth;

    /**
     * 节点类型（REWRITE/RETRIEVE/LLM等）
     */
    private String nodeType;

    /**
     * 节点名称（用于展示）
     */
    private String nodeName;

    private String className;

    private String methodName;

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
