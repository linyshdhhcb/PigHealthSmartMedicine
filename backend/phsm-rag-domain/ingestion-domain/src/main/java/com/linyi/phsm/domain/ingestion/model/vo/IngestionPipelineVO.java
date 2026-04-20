package com.linyi.phsm.domain.ingestion.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据摄取管道视图对象
 */
@Data
public class IngestionPipelineVO {

    /**
     * 管道ID
     */
    private String id;

    /**
     * 管道名称
     */
    private String name;

    /**
     * 管道描述
     */
    private String description;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 管道节点列表
     */
    private List<IngestionPipelineNodeVO> nodes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
