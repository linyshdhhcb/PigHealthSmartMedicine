package com.nageoffer.ai.ragent.pig.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.ai.ragent.pig.controller.vo.DrugVO;

import java.util.List;

/**
 * 兽药信息服务接口
 */
public interface DrugService {

    /**
     * 分页查询兽药列表
     */
    Page<DrugVO> listDrugs(String name, String drugType, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询兽药详情
     */
    DrugVO getDrugById(Long id);

    /**
     * 搜索兽药（按名称或适应症）
     */
    Page<DrugVO> searchDrugs(String keyword, Integer pageNum, Integer pageSize);
}
