package com.linyi.phsm.application.ingestion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.domain.ingestion.model.request.IngestionTaskCreateRequest;
import com.linyi.phsm.domain.ingestion.model.vo.IngestionTaskNodeVO;
import com.linyi.phsm.domain.ingestion.model.vo.IngestionTaskVO;
import com.linyi.phsm.domain.ingestion.model.result.IngestionResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数据摄入任务服务接口
 */
public interface IngestionTaskService {

    /**
     * 执行数据摄入任务
     *
     * @param request 创建请求
     * @return 摄入结果
     */
    IngestionResult execute(IngestionTaskCreateRequest request);

    /**
     * 上传文件并执行摄入任务
     *
     * @param pipelineId 流水线ID
     * @param file       上传文件
     * @return 摄入结果
     */
    IngestionResult upload(String pipelineId, MultipartFile file);

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务VO
     */
    IngestionTaskVO get(String taskId);

    /**
     * 分页查询任务
     *
     * @param page   分页参数
     * @param status 状态筛选
     * @return 分页结果
     */
    IPage<IngestionTaskVO> page(Page<IngestionTaskVO> page, String status);

    /**
     * 获取任务节点列表
     *
     * @param taskId 任务ID
     * @return 节点列表
     */
    List<IngestionTaskNodeVO> listNodes(String taskId);
}
