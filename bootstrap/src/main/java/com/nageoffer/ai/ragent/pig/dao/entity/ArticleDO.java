package com.nageoffer.ai.ragent.pig.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文章信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_article")
public class ArticleDO {

    /**
     * 文章唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章分类：BREEDING-养殖知识，DISEASE-疾病防治，NUTRITION-营养饲料，MANAGEMENT-管理技术，NEWS-新闻资讯
     */
    private String category;

    /**
     * 作者用户ID
     */
    private Long authorId;

    /**
     * 作者姓名
     */
    private String authorName;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞次数
     */
    private Integer likeCount;

    /**
     * 状态：DRAFT-草稿，PUBLISHED-已发布，ARCHIVED-已归档
     */
    private String status;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
