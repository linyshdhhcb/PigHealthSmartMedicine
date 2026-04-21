package com.linyi.pig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("knowledge_chunk")
public class KnowledgeChunk {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("kb_id")
    private Long kbId;

    @TableField("doc_id")
    private Long docId;

    @TableField("chunk_index")
    private Integer chunkIndex;

    private String content;

    @TableField("content_hash")
    private String contentHash;

    @TableField("char_count")
    private Integer charCount;

    @TableField("token_count")
    private Integer tokenCount;

    @TableField("created_by")
    private String createdBy;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
