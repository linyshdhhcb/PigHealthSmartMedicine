package com.linyi.phsm.interfaces.rest.illness;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.illness.service.IllnessMedicineService;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineBatchCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineBatchDeleteRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicineCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessMedicinePageRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessMedicineVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/illness-medicines")
public class IllnessMedicineController {

    private final IllnessMedicineService illnessMedicineService;

    @PostMapping
    public Result<String> create(@Valid @RequestBody IllnessMedicineCreateRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(illnessMedicineService.create(requestParam));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkRole("admin");
        illnessMedicineService.delete(id);
        return Results.success();
    }

    @PostMapping("/batch")
    public Result<Void> batchCreate(@Valid @RequestBody IllnessMedicineBatchCreateRequest requestParam) {
        StpUtil.checkRole("admin");
        illnessMedicineService.batchCreate(requestParam);
        return Results.success();
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@Valid @RequestBody IllnessMedicineBatchDeleteRequest requestParam) {
        StpUtil.checkRole("admin");
        illnessMedicineService.batchDelete(requestParam);
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<IllnessMedicineVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(illnessMedicineService.queryById(id));
    }

    @GetMapping
    public Result<IPage<IllnessMedicineVO>> pageQuery(IllnessMedicinePageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(illnessMedicineService.pageQuery(requestParam));
    }
}
