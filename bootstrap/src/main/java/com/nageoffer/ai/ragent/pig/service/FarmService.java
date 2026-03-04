package com.nageoffer.ai.ragent.pig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.FarmVO;

/**
 * 养殖场管理服务接口
 */
public interface FarmService {

    /**
     * 分页查询养殖场列表
     */
    Page<FarmVO> listFarms(Long ownerId, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询养殖场详情
     */
    FarmVO getFarmById(Long id);

    /**
     * 创建养殖场
     */
    Long createFarm(FarmVO farmVO);

    /**
     * 更新养殖场信息
     */
    void updateFarm(FarmVO farmVO);

    /**
     * 删除养殖场
     */
    void deleteFarm(Long id);
}
