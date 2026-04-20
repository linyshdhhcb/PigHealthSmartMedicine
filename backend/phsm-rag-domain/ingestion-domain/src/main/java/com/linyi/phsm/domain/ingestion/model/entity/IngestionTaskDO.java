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
 * 知识库摄取任务实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_ingestion_task")
public class IngestionTaskDO {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 管道 ID
     */
    private String pipelineId;

    /**
     * 数据源类型 (如: file, url, feishu, s3)
     */
    private String sourceType;

    /**
     * 数据源位置 (文件路径或 URL)
     */
    private String sourceLocation;

    /**
     * 源文件名
     */
    private String sourceFileName;

    /**
     * 任务状态 (如: pending, running, completed, failed)
     */
    private String status;

    /**
     * 切片数量
     */
    private Integer chunkCount;

    /**
     * 错误详情信息
     */
    private String errorMessage;

    /**
     * 日志 JSON
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private String logsJson;

    /**
     * 元数据 JSON
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private String metadataJson;

    /**
     * 开始时间
     */
    private Date startedAt;

    /**
     * 完成时间
     */
    private Date completedAt;

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
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标记 (0: 未删除, 1: 已删除)
     */
    @TableLogic
    private Integer deleted;
}
