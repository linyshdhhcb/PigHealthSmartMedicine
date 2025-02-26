package com.linyi.pig.entity.vo.feedback;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: FeedbackUpdateVo
* @Version: 1.0
* @Description: 反馈修改实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "反馈修改实体")
public class FeedbackUpdateVo implements Serializable {
    /**
    * 主键ID
    */
    private Serializable id;

    /**
     * 反馈用户
     */
    @TableField("name")
    @Schema(name = "name",description = "反馈用户",type = "varchar")
    private String name;

    /**
     * 邮箱地址
     */
    @TableField("email")
    @Schema(name = "email",description = "邮箱地址",type = "varchar")
    private String email;

    /**
     * 反馈标题
     */
    @TableField("title")
    @Schema(name = "title",description = "反馈标题",type = "varchar")
    private String title;

    /**
     * 反馈内容
     */
    @TableField("content")
    @Schema(name = "content",description = "反馈内容",type = "text")
    private String content;

}
