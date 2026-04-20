package com.linyi.phsm.interfaces.rest.rag;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import com.linyi.phsm.domain.rag.model.request.QueryTermMappingCreateRequest;
import com.linyi.phsm.domain.rag.model.request.QueryTermMappingPageRequest;
import com.linyi.phsm.domain.rag.model.request.QueryTermMappingUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.QueryTermMappingVO;
import com.linyi.phsm.application.rag.service.QueryTermMappingAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 关键词映射管理控制器
 */
@RestController
@RequestMapping("/mappings")
@RequiredArgsConstructor
public class QueryTermMappingController {

    private final QueryTermMappingAdminService queryTermMappingAdminService;

    /**
     * 分页查询映射规则
     */
    @GetMapping()
    public Result<IPage<QueryTermMappingVO>> pageQuery(QueryTermMappingPageRequest requestParam) {
        return Results.success(queryTermMappingAdminService.pageQuery(requestParam));
    }

    /**
     * 查询映射规则详情
     */
    @GetMapping("/{id}")
    public Result<QueryTermMappingVO> queryById(@PathVariable String id) {
        return Results.success(queryTermMappingAdminService.queryById(id));
    }

    /**
     * 创建映射规则
     */
    @PostMapping()
    public Result<String> create(@RequestBody QueryTermMappingCreateRequest requestParam) {
        return Results.success(queryTermMappingAdminService.create(requestParam));
    }

    /**
     * 更新映射规则
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable String id, @RequestBody QueryTermMappingUpdateRequest requestParam) {
        queryTermMappingAdminService.update(id, requestParam);
        return Results.success();
    }

    /**
     * 删除映射规则
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        queryTermMappingAdminService.delete(id);
        return Results.success();
    }
}
