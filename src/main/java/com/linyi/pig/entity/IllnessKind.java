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
* @Date: 2025-02-25 17:38:38
* @ClassName: IllnessKind
* @Version: 1.0
* @Description: 疾病种类
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("illness_kind")
@Schema(name = "疾病种类")
public class IllnessKind implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "主键ID",type = "int")
    private Integer id;

    /**
     * 分类名称
     */
    @TableField("name")
    @Schema(name = "name",description = "分类名称",type = "varchar")
    private String name;

    /**
     * 描述
     */
    @TableField("info")
    @Schema(name = "info",description = "描述",type = "varchar")
    private String info;

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
