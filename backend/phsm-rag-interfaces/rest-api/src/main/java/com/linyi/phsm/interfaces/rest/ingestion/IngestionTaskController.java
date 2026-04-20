package com.linyi.phsm.interfaces.rest.ingestion;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.domain.ingestion.model.request.IngestionTaskCreateRequest;
import com.linyi.phsm.domain.ingestion.model.vo.IngestionTaskNodeVO;
import com.linyi.phsm.domain.ingestion.model.vo.IngestionTaskVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import com.linyi.phsm.domain.ingestion.model.result.IngestionResult;
import com.linyi.phsm.application.ingestion.service.IngestionTaskService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 知识库采集任务控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ingestion")
@Validated
public class IngestionTaskController {

    private final IngestionTaskService taskService;

    /**
     * 创建并执行采集任务
     */
    @PostMapping("/tasks")
    public Result<IngestionResult> create(@RequestBody IngestionTaskCreateRequest request) {
        return Results.success(taskService.execute(request));
    }

    /**
     * 上传文件并触发采集任务
     */
    @SneakyThrows
    @PostMapping(value = "/tasks/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<IngestionResult> upload(@RequestParam(value = "pipelineId") String pipelineId,
                                          @RequestPart("file") MultipartFile file) {
        return Results.success(taskService.upload(pipelineId, file));
    }

    /**
     * 根据任务 ID 获取任务详情
     */
    @GetMapping("/tasks/{id}")
    public Result<IngestionTaskVO> get(@PathVariable String id) {
        return Results.success(taskService.get(id));
    }

    /**
     * 根据任务 ID 获取任务节点运行记录
     */
    @GetMapping("/tasks/{id}/nodes")
    public Result<List<IngestionTaskNodeVO>> nodes(@PathVariable String id) {
        return Results.success(taskService.listNodes(id));
    }

    /**
     * 分页查询采集任务
     */
    @GetMapping("/tasks")
    public Result<IPage<IngestionTaskVO>> page(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                               @RequestParam(value = "status", required = false) String status) {
        return Results.success(taskService.page(new Page<>(pageNo, pageSize), status));
    }
}
