package com.linyi.pig.entity.vo.articles;

import com.baomidou.mybatisplus.annotation.TableField;
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
* @ClassName: ArticlesAddVo
* @Version: 1.0
* @Description: 文章新增实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "文章新增实体")
public class ArticlesAddVo implements Serializable {

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
     * 文章类型ID，外键关联article_types表
     */
    @TableField("type_id")
    @Schema(name = "typeId",description = "文章类型ID，外键关联article_types表",type = "int")
    private Integer typeId;

}
