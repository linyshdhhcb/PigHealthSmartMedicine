package com.linyi.phsm.domain.ingestion.model.result;

import com.linyi.phsm.domain.ingestion.model.enums.IngestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 摄取结果实体类
 * 表示文档摄取任务执行完成后的结果信息，包含任务状态、分块数量等概要数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngestionResult {

    /**
     * 摄取任务的唯一标识符
     */
    private String taskId;

    /**
     * 执行本次摄取的管道ID
     */
    private String pipelineId;

    /**
     * 摄取任务的最终状态
     */
    private IngestionStatus status;

    /**
     * 文档被切分成的块数量
     */
    private Integer chunkCount;

    /**
     * 执行结果的消息说明
     * 成功时为概要信息，失败时为错误原因
     */
    private String message;
}
