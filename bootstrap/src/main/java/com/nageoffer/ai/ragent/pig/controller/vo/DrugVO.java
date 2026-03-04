package com.nageoffer.ai.ragent.pig.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 兽药信息VO
 */
@Data
public class DrugVO {

    /**
     * 兽药唯一标识
     */
    private Long id;

    /**
     * 兽药名称
     */
    private String name;

    /**
     * 药品类型
     */
    private String drugType;

    /**
     * 药品类型名称
     */
    private String drugTypeName;

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
     * 剂型
     */
    private String dosageForm;

    /**
     * 剂型名称
     */
    private String dosageFormName;

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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
