package com.linyi.phsm.domain.knowledge.model.request;

import lombok.Data;

@Data
public class KnowledgeDocumentUploadRequest {

    /**
     * 来源类型：file / url
     */
    private String sourceType;

    /**
     * 来源位置（URL）
     */
    private String sourceLocation;

    /**
     * 是否开启定时拉取
     */
    private Boolean scheduleEnabled;

    /**
     * 定时表达式（cron）
     */
    private String scheduleCron;

    /**
     * 处理模式：chunk / pipeline
     * - chunk: 使用分块策略直接分块
     * - pipeline: 使用数据通道进行清洗处理
     */
    private String processMode;

    /**
     * 分块策略：fixed_size / structure_aware
     * 仅在 processMode=chunk 时有效
     */
    private String chunkStrategy;

    /**
     * 分块参数JSON，processMode=chunk 时必传
     * 如 {"chunkSize":512,"overlapSize":128} 或 {"targetChars":1400,"maxChars":1800,"minChars":600,"overlapChars":0}
     */
    private String chunkConfig;

    /**
     * 数据通道（Pipeline）ID
     * 仅在 processMode=pipeline 时有效
     */
    private String pipelineId;
}
