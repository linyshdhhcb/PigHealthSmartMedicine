

package com.nageoffer.ai.ragent.knowledge.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 知识库文档定时刷新执行记录实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_knowledge_document_schedule_exec")
public class KnowledgeDocumentScheduleExecDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 定时任务 ID
     */
    private Long scheduleId;

    /**
     * 文档 ID
     */
    private Long docId;

    /**
     * 知识库 ID
     */
    private Long kbId;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 执行信息
     */
    private String message;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 内容哈希
     */
    private String contentHash;

    /**
     * ETag
     */
    private String etag;

    /**
     * Last-Modified
     */
    private String lastModified;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
