package com.linyi.pig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: Pageview
* @Version: 1.0
* @Description: 浏览量
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pageview")
@Schema(name = "浏览量")
public class Pageview implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "主键id",type = "int")
    private Integer id;

    /**
     * 浏览量
     */
    @TableField("pageviews")
    @Schema(name = "pageviews",description = "浏览量",type = "int")
    private Integer pageviews;

    /**
     * 病的id
     */
    @TableField("illness_id")
    @Schema(name = "illnessId",description = "病的id",type = "int")
    private Integer illnessId;

}
