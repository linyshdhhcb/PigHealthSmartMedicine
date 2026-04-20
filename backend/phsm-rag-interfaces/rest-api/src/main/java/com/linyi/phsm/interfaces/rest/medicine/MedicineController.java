package com.linyi.phsm.interfaces.rest.medicine;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.application.rag.illness.service.IllnessMedicineService;
import com.linyi.phsm.application.rag.medicine.service.MedicineService;
import com.linyi.phsm.domain.rag.model.request.MedicineCreateRequest;
import com.linyi.phsm.domain.rag.model.request.MedicinePageRequest;
import com.linyi.phsm.domain.rag.model.request.MedicineUpdateRequest;
import com.linyi.phsm.domain.rag.model.vo.IllnessVO;
import com.linyi.phsm.domain.rag.model.vo.MedicineVO;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medicines")
public class MedicineController {

    private final MedicineService medicineService;
    private final IllnessMedicineService illnessMedicineService;

    @PostMapping
    public Result<String> create(@Valid @RequestBody MedicineCreateRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(medicineService.create(requestParam));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable String id, @Valid @RequestBody MedicineUpdateRequest requestParam) {
        StpUtil.checkRole("admin");
        medicineService.update(id, requestParam);
        return Results.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        StpUtil.checkRole("admin");
        medicineService.delete(id);
        return Results.success();
    }

    @GetMapping("/{id}")
    public Result<MedicineVO> queryById(@PathVariable String id) {
        StpUtil.checkRole("admin");
        return Results.success(medicineService.queryById(id));
    }

    @GetMapping
    public Result<IPage<MedicineVO>> pageQuery(MedicinePageRequest requestParam) {
        StpUtil.checkRole("admin");
        return Results.success(medicineService.pageQuery(requestParam));
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> uploadImage(@RequestPart("file") MultipartFile file) {
        StpUtil.checkRole("admin");
        return Results.success(medicineService.uploadImage(file));
    }

    @GetMapping("/{medicineId}/illnesses")
    public Result<List<IllnessVO>> listIllnesses(@PathVariable String medicineId) {
        StpUtil.checkRole("admin");
        return Results.success(illnessMedicineService.listIllnessesByMedicine(medicineId));
    }
}
