package com.linyi.pig.common.model;

import lombok.Data;

@Data
public class PageResponse {

    /**
     * 当前页码（从1开始）
     */
    private long pageNum;

    /**
     * 每页记录数
     */
    private long pageSize;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（升序 ASC / 降序 DESC）
     */
    private String sortOrder;

    public PageResponse() {
        this.pageNum = 1; // 默认为第1页
        this.pageSize = 10; // 默认每页10条数据
        this.sortField = "id"; // 默认按id排序
        this.sortOrder = "ASC"; //排序顺序（升序 ASC / 降序 DESC）
    }

    // 构造函数
    public PageResponse(int pageNum, int pageSize, String sortField, String sortOrder) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
}
