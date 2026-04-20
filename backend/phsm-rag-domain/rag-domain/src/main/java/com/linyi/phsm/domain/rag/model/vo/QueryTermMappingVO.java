package com.linyi.phsm.domain.rag.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 关键词映射视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryTermMappingVO {

    private String id;
    private String sourceTerm;
    private String targetTerm;
    private Integer matchType;
    private Integer priority;
    private Boolean enabled;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
