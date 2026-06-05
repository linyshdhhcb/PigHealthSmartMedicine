package com.linyi.pig.entity.vo.knowledge;

import lombok.Data;

@Data
public class KnowledgeBaseQueryVo {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;
}
