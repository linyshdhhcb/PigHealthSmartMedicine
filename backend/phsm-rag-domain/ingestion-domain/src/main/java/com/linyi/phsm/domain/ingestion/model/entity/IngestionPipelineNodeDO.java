package com.linyi.phsm.domain.ingestion.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linyi.phsm.framework.database.JsonbTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 摄取流水线节点实体对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_ingestion_pipeline_node")
public class IngestionPipelineNodeDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 流水线ID
     */
    private String pipelineId;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 节点类型 (如: fetcher, parser, chunker, indexer)
     */
    private String nodeType;

    /**
     * 下一个节点ID
     */
    private String nextNodeId;

    /**
     * 设置详情JSON
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private String settingsJson;

    /**
     * 条件详情JSON
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private String conditionJson;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标记 0：未删除 1：已删除
     */
    @TableLogic
    private Integer deleted;
}
