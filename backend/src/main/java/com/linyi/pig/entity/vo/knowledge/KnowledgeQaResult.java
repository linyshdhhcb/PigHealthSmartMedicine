package com.linyi.pig.entity.vo.knowledge;

import lombok.Data;

import java.util.List;

@Data
public class KnowledgeQaResult {
    private List<KnowledgeChunkView> chunks;
    private String answer;
}
