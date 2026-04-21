package com.linyi.pig.entity.vo.knowledge;

import lombok.Data;

@Data
public class KnowledgeQaRequest {
    private Long kbId;
    private String question;
    private Integer topK = 5;
}
