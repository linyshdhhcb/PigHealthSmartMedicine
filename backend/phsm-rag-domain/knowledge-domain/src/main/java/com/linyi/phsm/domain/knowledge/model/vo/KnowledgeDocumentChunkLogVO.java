package com.linyi.phsm.domain.knowledge.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 知识库文档分块日志 VO
 */
@Data
public class KnowledgeDocumentChunkLogVO {

    private String id;

    private String docId;

    /**
     * 执行状态：running / success / failed
     */
    private String status;

    /**
     * 处理模式：chunk / pipeline
     */
    private String processMode;

    /**
     * 分块策略（仅 chunk 模式）
     */
    private String chunkStrategy;

    /**
     * Pipeline ID（仅 pipeline 模式）
     */
    private String pipelineId;

    /**
     * Pipeline 名称（仅 pipeline 模式）
     */
    private String pipelineName;

    /**
     * 文本提取耗时（毫秒）
     */
    private Long extractDuration;

    /**
     * 分块耗时（毫秒）
     */
    private Long chunkDuration;

    /**
     * 嵌入 API 耗时（毫秒）
     */
    private Long embedDuration;

    /**
     * DB 持久化耗时（毫秒）
     */
    private Long persistDuration;

    /**
     * 其他耗时（毫秒）
     */
    private Long otherDuration;

    /**
     * 总耗时（毫秒）
     */
    private Long totalDuration;

    /**
     * 生成的分块数量
     */
    private Integer chunkCount;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    private Date createTime;
}
