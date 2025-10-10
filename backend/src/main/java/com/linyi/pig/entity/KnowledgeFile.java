package com.linyi.pig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("knowledge_file")
public class KnowledgeFile {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("file_name")
    private String fileName; // 原始文件名（含后缀）

    @TableField("file_path")
    private String filePath; // 相对路径：相对于 resources/knowledge/

    @TableField("file_size")
    private Long fileSize; // 字节

    @TableField("file_md5")
    private String fileMd5; // 32位MD5

    @TableField("file_type")
    private String fileType; // MIME-Type

    @TableField("create_by")
    private Integer createBy;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("remark")
    private String remark;
}
