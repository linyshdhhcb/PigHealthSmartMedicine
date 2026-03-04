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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生猪信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_pig")
public class PigDO {

    /**
     * 生猪唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 养殖场标识
     */
    private Long farmId;

    /**
     * 养殖户用户ID
     */
    private Long userId;

    /**
     * 耳标号
     */
    private String earTagNumber;

    /**
     * 品种
     */
    private String breed;

    /**
     * 性别：0-母猪，1-公猪
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 体重（kg）
     */
    private BigDecimal weight;

    /**
     * 健康状态：HEALTHY-健康，SICK-患病，RECOVERING-康复中，DEAD-死亡
     */
    private String healthStatus;

    /**
     * 猪栏编号
     */
    private String penNumber;

    /**
     * 饲养状态：NORMAL-正常，WEANING-断奶，FATTENING-育肥
     */
    private String feedStatus;

    /**
     * 免疫状态（JSON格式）
     */
    private String immunizationStatus;

    /**
     * 遗传信息
     */
    private String geneticInfo;

    /**
     * 备注
     */
    private String remark;

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
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;
}
