package com.nageoffer.ai.ragent.pig.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生猪信息VO
 */
@Data
public class PigVO {

    /**
     * 生猪唯一标识
     */
    private Long id;

    /**
     * 养殖场标识
     */
    private Long farmId;

    /**
     * 养殖场名称
     */
    private String farmName;

    /**
     * 养殖户用户ID
     */
    private Long userId;

    /**
     * 养殖户姓名
     */
    private String userName;

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
     * 性别名称
     */
    private String genderName;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 年龄（天）
     */
    private Integer age;

    /**
     * 体重（kg）
     */
    private BigDecimal weight;

    /**
     * 健康状态
     */
    private String healthStatus;

    /**
     * 健康状态名称
     */
    private String healthStatusName;

    /**
     * 猪栏编号
     */
    private String penNumber;

    /**
     * 饲养状态
     */
    private String feedStatus;

    /**
     * 饲养状态名称
     */
    private String feedStatusName;

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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
