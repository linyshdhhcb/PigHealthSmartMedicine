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
 * 兽药信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_drug")
public class DrugDO {

    /**
     * 兽药唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 兽药名称
     */
    private String name;

    /**
     * 药品类型：CHINESE-中药，WESTERN-西药
     */
    private String drugType;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产厂家地址
     */
    private String manufacturerAddress;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 规格
     */
    private String specification;

    /**
     * 剂型：TABLET-片剂，INJECTION-注射剂，POWDER-粉剂，LIQUID-液体
     */
    private String dosageForm;

    /**
     * 成分
     */
    private String ingredients;

    /**
     * 适应症
     */
    private String indication;

    /**
     * 用法
     */
    @TableField("`usage`")
    private String usage;

    /**
     * 用量
     */
    private String dosage;

    /**
     * 禁忌症
     */
    private String contraindication;

    /**
     * 不良反应
     */
    private String adverseReaction;

    /**
     * 药物相互作用
     */
    private String drugInteraction;

    /**
     * 贮藏
     */
    private String storage;

    /**
     * 贮存条件
     */
    private String storageConditions;

    /**
     * 有效期
     */
    private String validityPeriod;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stockQuantity;

    /**
     * 销售量
     */
    private Integer salesVolume;

    /**
     * 评分（1-5）
     */
    private BigDecimal rating;

    /**
     * 评价数量
     */
    private Integer reviewCount;

    /**
     * 药品图片URL
     */
    private String imageUrl;

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
