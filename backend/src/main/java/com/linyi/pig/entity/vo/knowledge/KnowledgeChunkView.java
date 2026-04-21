package com.linyi.pig.entity.vo.knowledge;

import lombok.Data;

@Data
public class KnowledgeChunkView {
    private Long id;
    private Long kbId;
    private Long docId;
    private Integer chunkIndex;
    private String fileName;
    private String content;
}
