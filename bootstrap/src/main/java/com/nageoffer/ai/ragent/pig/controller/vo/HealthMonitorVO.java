package com.nageoffer.ai.ragent.pig.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 健康监测记录VO
 */
@Data
public class HealthMonitorVO {

    /**
     * 监测记录唯一标识
     */
    private Long id;

    /**
     * 生猪ID
     */
    private Long pigId;

    /**
     * 生猪耳标号
     */
    private String earTagNumber;

    /**
     * 监测日期
     */
    private Date monitorDate;

    /**
     * 体温（℃）
     */
    private BigDecimal temperature;

    /**
     * 体重（kg）
     */
    private BigDecimal weight;

    /**
     * 食欲
     */
    private String appetite;

    /**
     * 食欲名称
     */
    private String appetiteName;

    /**
     * 活动水平
     */
    private String activityLevel;

    /**
     * 活动水平名称
     */
    private String activityLevelName;

    /**
     * 粪便状况
     */
    private String fecesCondition;

    /**
     * 呼吸频率（次/分钟）
     */
    private Integer respiratoryRate;

    /**
     * 心率（次/分钟）
     */
    private Integer heartRate;

    /**
     * 异常体征
     */
    private String abnormalSigns;

    /**
     * 监测人ID
     */
    private Long monitorBy;

    /**
     * 监测人姓名
     */
    private String monitorByName;

    /**
     * 创建时间
     */
    private Date createTime;
}
