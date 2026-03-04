package com.nageoffer.ai.ragent.pig.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生猪健康监测实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_health_monitor")
public class HealthMonitorDO {

    /**
     * 监测记录唯一标识
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 生猪ID
     */
    private Long pigId;

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
     * 食欲：GOOD-良好，NORMAL-正常，POOR-差
     */
    private String appetite;

    /**
     * 活动水平：ACTIVE-活跃，NORMAL-正常，LETHARGIC-嗜睡
     */
    private String activityLevel;

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
     * 创建时间
     */
    private Date createTime;
}
