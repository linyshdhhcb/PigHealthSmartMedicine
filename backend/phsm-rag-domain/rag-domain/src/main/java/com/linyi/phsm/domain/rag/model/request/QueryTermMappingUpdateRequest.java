package com.linyi.phsm.domain.rag.model.request;

import lombok.Data;

/**
 * 关键词映射更新请求
 */
@Data
public class QueryTermMappingUpdateRequest {

    /**
     * 用户原始短语
     */
    private String sourceTerm;

    /**
     * 归一化后的目标短语
     */
    private String targetTerm;

    /**
     * 匹配类型 1：精确匹配 2：前缀匹配 3：正则匹配 4：整词匹配
     */
    private Integer matchType;

    /**
     * 优先级，数值越小优先级越高
     */
    private Integer priority;

    /**
     * 是否生效
     */
    private Boolean enabled;

    /**
     * 备注
     */
    private String remark;
}
