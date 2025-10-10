package com.linyi.pig.entity.vo.knowledge;

import lombok.Data;

@Data
public class KnowledgeFileQueryVo {
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    private String fileName;
    private String filePath;
    private String fileType;
    private String fileMd5;
}
