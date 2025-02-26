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
* @ClassName: NewsArticles
* @Version: 1.0
* @Description: 新闻资讯
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("news_articles")
@Schema(name = "新闻资讯")
public class NewsArticles implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 新闻ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "新闻ID",type = "int")
    private Integer id;

    /**
     * 转载url
     */
    @TableField("url")
    @Schema(name = "url",description = "转载url",type = "varchar")
    private String url;

    /**
     * 新闻标题
     */
    @TableField("title")
    @Schema(name = "title",description = "新闻标题",type = "varchar")
    private String title;

    /**
     * 新闻内容
     */
    @TableField("content")
    @Schema(name = "content",description = "新闻内容",type = "text")
    private String content;

    /**
     * 作者
     */
    @TableField("author")
    @Schema(name = "author",description = "作者",type = "varchar")
    private String author;

    /**
     * 发布时间，默认为当前时间
     */
    @TableField("publish_time")
    @Schema(name = "publishTime",description = "发布时间，默认为当前时间",type = "timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    /**
     * 新闻来源
     */
    @TableField("source")
    @Schema(name = "source",description = "新闻来源",type = "varchar")
    private String source;

    /**
     * 新闻摘要
     */
    @TableField("summary")
    @Schema(name = "summary",description = "新闻摘要",type = "text")
    private String summary;

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

}
