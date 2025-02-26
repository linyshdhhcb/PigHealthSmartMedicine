package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Sessions;
import com.linyi.pig.entity.vo.sessions.SessionsAddVo;
import com.linyi.pig.entity.vo.sessions.SessionsQueryVo;
import com.linyi.pig.entity.vo.sessions.SessionsUpdateVo;
import com.linyi.pig.mapper.SessionsMapper;
import com.linyi.pig.service.SessionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: SessionsServiceImpl
* @Version: 1.0
* @Description:  服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class SessionsServiceImpl extends ServiceImpl<SessionsMapper, Sessions> implements SessionsService {

    @Autowired
    private SessionsMapper sessionsMapper;

    @Override
    public PageResult<Sessions> sessionsPage(SessionsQueryVo sessionsQueryVo) {
        LambdaQueryWrapper<Sessions> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<Sessions> page = new Page<>(sessionsQueryVo.getPageNum(),sessionsQueryVo.getPageSize());
        //查询数据
        Page<Sessions> pageNew = sessionsMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), sessionsQueryVo.getPageNum(), sessionsQueryVo.getPageSize());
    }

    @Override
    public Boolean sessionsAdd(SessionsAddVo sessionsAddVo){
        //创建实体对象
        Sessions sessions = new Sessions();
        //复制属性
        BeanUtils.copyProperties(sessionsAddVo, sessions);
        //插入数据
        return sessionsMapper.insert(sessions) > 0 ? true : false;
    }

    @Override
    public Boolean sessionsUpdate(SessionsUpdateVo sessionsUpdateVo){
        //根据ID查询数据
        Sessions byId=this.getById(sessionsUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(sessionsUpdateVo, byId);
        //修改数据
        return sessionsMapper.updateById(byId) > 0 ? true : false;
    }
}
