package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.History;
import com.linyi.pig.entity.vo.history.HistoryAddVo;
import com.linyi.pig.entity.vo.history.HistoryQueryVo;
import com.linyi.pig.entity.vo.history.HistoryUpdateVo;
import com.linyi.pig.mapper.HistoryMapper;
import com.linyi.pig.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: HistoryServiceImpl
* @Version: 1.0
* @Description: 操作记录 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public PageResult<History> historyPage(HistoryQueryVo historyQueryVo) {
        LambdaQueryWrapper<History> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<History> page = new Page<>(historyQueryVo.getPageNum(),historyQueryVo.getPageSize());
        //查询数据
        Page<History> pageNew = historyMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), historyQueryVo.getPageNum(), historyQueryVo.getPageSize());
    }

    @Override
    public Boolean historyAdd(HistoryAddVo historyAddVo){
        //创建实体对象
        History history = new History();
        //复制属性
        BeanUtils.copyProperties(historyAddVo, history);
        //插入数据
        return historyMapper.insert(history) > 0 ? true : false;
    }

    @Override
    public Boolean historyUpdate(HistoryUpdateVo historyUpdateVo){
        //根据ID查询数据
        History byId=this.getById(historyUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(historyUpdateVo, byId);
        //修改数据
        return historyMapper.updateById(byId) > 0 ? true : false;
    }
}
