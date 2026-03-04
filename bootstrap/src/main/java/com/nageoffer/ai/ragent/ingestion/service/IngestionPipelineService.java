

package com.nageoffer.ai.ragent.ingestion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.ingestion.controller.request.IngestionPipelineCreateRequest;
import com.nageoffer.ai.ragent.ingestion.controller.request.IngestionPipelineUpdateRequest;
import com.nageoffer.ai.ragent.ingestion.controller.vo.IngestionPipelineVO;
import com.nageoffer.ai.ragent.ingestion.domain.pipeline.PipelineDefinition;

/**
 * 数据清洗流水线服务接口
 */
public interface IngestionPipelineService {

    /**
     * 创建流水线
     *
     * @param request 创建请求
     * @return 流水线VO
     */
    IngestionPipelineVO create(IngestionPipelineCreateRequest request);

    /**
     * 更新流水线
     *
     * @param pipelineId 流水线ID
     * @param request    更新请求
     * @return 流水线VO
     */
    IngestionPipelineVO update(String pipelineId, IngestionPipelineUpdateRequest request);

    /**
     * 获取流水线详情
     *
     * @param pipelineId 流水线ID
     * @return 流水线VO
     */
    IngestionPipelineVO get(String pipelineId);

    /**
     * 分页查询流水线
     *
     * @param page    分页参数
     * @param keyword 关键字
     * @return 分页结果
     */
    IPage<IngestionPipelineVO> page(Page<IngestionPipelineVO> page, String keyword);

    /**
     * 删除流水线
     *
     * @param pipelineId 流水线ID
     */
    void delete(String pipelineId);

    /**
     * 获取流水线定义
     *
     * @param pipelineId 流水线ID
     * @return 流水线定义
     */
    PipelineDefinition getDefinition(String pipelineId);
}
