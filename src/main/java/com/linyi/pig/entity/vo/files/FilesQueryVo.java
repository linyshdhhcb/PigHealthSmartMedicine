package com.linyi.pig.entity.vo.files;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linyi.pig.common.model.PageResponse;
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
* @ClassName: FilesQueryVo
* @Version: 1.0
* @Description: 文件信息查询实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "文件信息查询实体")
public class FilesQueryVo extends PageResponse implements Serializable {

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
     * 文件存储的 MinIO 桶名称
     */
    @TableField("bucket_name")
    @Schema(name = "bucketName",description = "文件存储的 MinIO 桶名称",type = "varchar")
    private String bucketName;

}
