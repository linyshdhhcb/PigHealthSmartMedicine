package com.linyi.pig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: Files
* @Version: 1.0
* @Description: 文件信息
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("files")
@Schema(name = "文件信息")
public class Files implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "主键id",type = "int")
    private Integer id;

    /**
     * 文件名
     */
    @TableField("file_name")
    @Schema(name = "fileName",description = "文件名",type = "varchar")
    private String fileName;

    /**
     * 文件在 MinIO 中的路径
     */
    @TableField("file_path")
    @Schema(name = "filePath",description = "文件在 MinIO 中的路径",type = "varchar")
    private String filePath;

    /**
     * 文件大小，单位为字节
     */
    @TableField("file_size")
    @Schema(name = "fileSize",description = "文件大小，单位为字节",type = "bigint")
    private Long fileSize;

    /**
     * 文件的 MIME 类型
     */
    @TableField("content_type")
    @Schema(name = "contentType",description = "文件的 MIME 类型",type = "varchar")
    private String contentType;

    /**
     * 文件的url
     */
    @TableField("url")
    @Schema(name = "url",description = "文件的url",type = "varchar")
    private String url;

    /**
     * 文件上传时间
     */
    @TableField("upload_time")
    @Schema(name = "uploadTime",description = "文件上传时间",type = "timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;

    /**
     * 文件存储的 MinIO 桶名称
     */
    @TableField("bucket_name")
    @Schema(name = "bucketName",description = "文件存储的 MinIO 桶名称",type = "varchar")
    private String bucketName;

}
