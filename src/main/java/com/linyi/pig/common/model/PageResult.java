package com.linyi.pig.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    /**
     * 当前页数据列表
     */
    private List<T> data;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页码
     */
    private long pageNum;

    /**
     * 每页记录数
     */
    private long pageSize;

    /**
     * 总页数
     */
    private long totalPages;


}
