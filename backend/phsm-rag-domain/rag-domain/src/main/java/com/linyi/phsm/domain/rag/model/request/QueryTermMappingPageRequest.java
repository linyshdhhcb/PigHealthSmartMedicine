package com.linyi.phsm.domain.rag.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 关键词映射分页查询请求
 */
@Data
public class QueryTermMappingPageRequest extends Page {

    /**
     * 关键词（支持匹配 sourceTerm/targetTerm）
     */
    private String keyword;
}
