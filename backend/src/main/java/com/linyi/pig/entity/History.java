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
* @ClassName: History
* @Version: 1.0
* @Description: 操作记录
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("history")
@Schema(name = "操作记录")
public class History implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 用户搜索历史主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id",description = "用户搜索历史主键id",type = "int")
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(name = "userId",description = "用户ID",type = "int")
    private Integer userId;

    /**
     * 搜索关键字
     */
    @TableField("keyword")
    @Schema(name = "keyword",description = "搜索关键字",type = "varchar")
    private String keyword;

    /**
     * 类型：1搜索，2科目，3药品
     */
    @TableField("operate_type")
    @Schema(name = "operateType",description = "类型：1搜索，2科目，3药品",type = "int")
    private Integer operateType;

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
