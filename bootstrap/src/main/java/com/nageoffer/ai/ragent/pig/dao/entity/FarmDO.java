package com.nageoffer.ai.ragent.pig.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 养殖场信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_farm")
public class FarmDO {

    /**
     * 养殖场唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 养殖场名称
     */
    private String name;

    /**
     * 场主用户ID
     */
    private Long ownerId;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 养殖规模：SMALL-小型，MEDIUM-中型，LARGE-大型
     */
    private String scale;

    /**
     * 生猪总数
     */
    private Integer totalPigs;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
