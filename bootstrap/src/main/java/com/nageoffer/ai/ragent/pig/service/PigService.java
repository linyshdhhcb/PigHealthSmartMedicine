package com.nageoffer.ai.ragent.pig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.PigVO;

/**
 * 生猪信息服务接口
 */
public interface PigService {

    /**
     * 分页查询生猪列表
     */
    Page<PigVO> listPigs(Long farmId, Long userId, String healthStatus, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询生猪详情
     */
    PigVO getPigById(Long id);

    /**
     * 创建生猪
     */
    Long createPig(PigVO pigVO);

    /**
     * 更新生猪信息
     */
    void updatePig(PigVO pigVO);

    /**
     * 删除生猪
     */
    void deletePig(Long id);

    /**
     * 根据耳标号查询生猪
     */
    PigVO getPigByEarTag(String earTagNumber);
}
