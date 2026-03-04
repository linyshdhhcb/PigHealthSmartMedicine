

package com.nageoffer.ai.ragent.knowledge.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库文档视图对象
 */
@Data
public class KnowledgeDocumentVO {

    /**
     * 文档唯一标识
     */
    private String id;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 文档名称
     */
    private String docName;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源位置
     */
    private String sourceLocation;

    /**
     * 是否开启定时拉取
     */
    private Integer scheduleEnabled;

    /**
     * 定时表达式
     */
    private String scheduleCron;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 切片数量
     */
    private Integer chunkCount;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 分块策略
     */
    private String chunkStrategy;

    /**
     * 处理模式：chunk / pipeline
     * - chunk: 使用分块策略直接分块
     * - pipeline: 使用数据通道进行清洗处理
     */
    private String processMode;

    /**
     * 分块参数配置（JSON）
     */
    private String chunkConfig;

    /**
     * 数据通道（Pipeline）ID
     * 仅在 processMode=pipeline 时有效
     */
    private String pipelineId;

    /**
     * 状态（如：解析中、已解析、解析失败等）
     */
    private String status;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
