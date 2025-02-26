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
* @ClassName: Articles
* @Version: 1.0
* @Description: 文章
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("articles")
@Schema(name = "文章")
public class Articles implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "文章ID",type = "int")
    private Integer id;

    /**
     * 文章标题
     */
    @TableField("title")
    @Schema(name = "title",description = "文章标题",type = "varchar")
    private String title;

    /**
     * 文章内容
     */
    @TableField("content")
    @Schema(name = "content",description = "文章内容",type = "text")
    private String content;

    /**
     * 作者
     */
    @TableField("author")
    @Schema(name = "author",description = "作者",type = "varchar")
    private String author;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @Schema(name = "createTime",description = "创建时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @Schema(name = "updateTime",description = "更新时间",type = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 文章类型ID，外键关联article_types表
     */
    @TableField("type_id")
    @Schema(name = "typeId",description = "文章类型ID，外键关联article_types表",type = "int")
    private Integer typeId;

}
