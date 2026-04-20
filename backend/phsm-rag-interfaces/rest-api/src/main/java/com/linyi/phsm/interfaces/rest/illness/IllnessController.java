package com.linyi.phsm.interfaces.rest.illness;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.illness.service.IllnessMedicineService;
import com.linyi.phsm.application.rag.illness.service.IllnessService;
import com.linyi.phsm.domain.rag.model.request.IllnessCreateRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessPageRequest;
import com.linyi.phsm.domain.rag.model.request.IllnessUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessVO;
import com.linyi.phsm.domain.rag.model.vo.MedicineVO;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/illnesses")
public class IllnessController {

    private final IllnessService illnessService;
    private final IllnessMedicineService illnessMedicineService;

    @PostMapping
    public Result<String> create(@Valid @RequestBody IllnessCreateRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(illnessService.create(requestParam));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable String id, @Valid @RequestBody IllnessUpdateRequest requestParam) {
        StpUtil.checkRole("admin");
        illnessService.update(id, requestParam);
        return Results.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkRole("admin");
        illnessService.delete(id);
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<IllnessVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(illnessService.queryById(id));
    }

    @GetMapping
    public Result<IPage<IllnessVO>> pageQuery(IllnessPageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(illnessService.pageQuery(requestParam));
    }

    @GetMapping("/{illnessId}/medicines")
    public Result<List<MedicineVO>> listMedicines(@PathVariable String illnessId) {
        StpUtil.checkRole("admin");
        return Results.success(illnessMedicineService.listMedicinesByIllness(illnessId));
    }
}
