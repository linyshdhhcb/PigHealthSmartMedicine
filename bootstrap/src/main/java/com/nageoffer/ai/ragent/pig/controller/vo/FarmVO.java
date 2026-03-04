package com.nageoffer.ai.ragent.pig.controller.vo;

import lombok.Data;

import java.util.Date;

/**
 * 养殖场信息VO
 */
@Data
public class FarmVO {

    /**
     * 养殖场唯一标识
     */
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
     * 场主姓名
     */
    private String ownerName;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 养殖规模
     */
    private String scale;

    /**
     * 养殖规模名称
     */
    private String scaleName;

    /**
     * 生猪总数
     */
    private Integer totalPigs;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
