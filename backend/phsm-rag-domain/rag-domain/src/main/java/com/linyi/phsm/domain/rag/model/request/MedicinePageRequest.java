package com.linyi.phsm.domain.rag.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicinePageRequest extends Page {

    private Integer medicineType;
    private String keyword;
}
