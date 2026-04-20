package com.linyi.phsm.domain.knowledge.model.request;

import lombok.Data;

@Data
public class KnowledgeDocumentUpdateRequest {

    /**
     * 文档名称
     */
    private String docName;

    /**
     * 处理模式：chunk / pipeline
     */
    private String processMode;

    /**
     * 分块策略（CHUNK 模式），如 fixed_size、structure_aware
     */
    private String chunkStrategy;

    /**
     * 分块参数 JSON（CHUNK 模式），如 {"chunkSize":512,"overlapSize":128}
     */
    private String chunkConfig;

    /**
     * Pipeline ID（PIPELINE 模式）
     */
    private String pipelineId;

    /**
     * 来源位置（URL）
     */
    private String sourceLocation;

    /**
     * 是否开启定时拉取：1-启用，0-禁用
     */
    private Integer scheduleEnabled;

    /**
     * 定时表达式（cron）
     */
    private String scheduleCron;
}
