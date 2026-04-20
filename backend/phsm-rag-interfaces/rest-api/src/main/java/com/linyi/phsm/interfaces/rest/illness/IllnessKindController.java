package com.linyi.phsm.interfaces.rest.illness;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.illness.service.IllnessKindService;
import com.linyi.phsm.domain.rag.model.request.IllnessKindCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessKindPageRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessKindUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessKindVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/illness-kinds")
public class IllnessKindController {

    private final IllnessKindService illnessKindService;

    @PostMapping
    public Result<String> create(@Valid @RequestBody IllnessKindCreateRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(illnessKindService.create(requestParam));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable String id, @Valid @RequestBody IllnessKindUpdateRequest requestParam) {
        StpUtil.checkRole("admin");
        illnessKindService.update(id, requestParam);
        return Results.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkRole("admin");
        illnessKindService.delete(id);
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<IllnessKindVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(illnessKindService.queryById(id));
    }

    @GetMapping
    public Result<IPage<IllnessKindVO>> pageQuery(IllnessKindPageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(illnessKindService.pageQuery(requestParam));
    }
}
