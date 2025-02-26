package com.linyi.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.linyi.pig.entity.Conversation;
import com.linyi.pig.entity.vo.conversation.ConversationAddVo;
import com.linyi.pig.entity.vo.conversation.ConversationQueryVo;
import com.linyi.pig.entity.vo.conversation.ConversationUpdateVo;
import com.linyi.pig.mapper.ConversationMapper;
import com.linyi.pig.service.ConversationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.linyi.pig.common.model.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Optional;
import java.util.List;

/**
* @Author: linyi
* @Date: 2025-02-26 13:27:06
* @ClassName: ConversationServiceImpl
* @Version: 1.0
* @Description: 对话 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

    @Autowired
    private ConversationMapper conversationMapper;

    @Override
    public PageResult<Conversation> conversationPage(ConversationQueryVo conversationQueryVo) {
        LambdaQueryWrapper<Conversation> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<Conversation> page = new Page<>(conversationQueryVo.getPageNum(),conversationQueryVo.getPageSize());
        //查询数据
        Page<Conversation> pageNew = conversationMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), conversationQueryVo.getPageNum(), conversationQueryVo.getPageSize());
    }

    @Override
    public Boolean conversationAdd(ConversationAddVo conversationAddVo){
        //创建实体对象
        Conversation conversation = new Conversation();
        //复制属性
        BeanUtils.copyProperties(conversationAddVo, conversation);
        //插入数据
        return conversationMapper.insert(conversation) > 0 ? true : false;
    }

    @Override
    public Boolean conversationUpdate(ConversationUpdateVo conversationUpdateVo){
        //根据ID查询数据
        Conversation byId=this.getById(conversationUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(conversationUpdateVo, byId);
        //修改数据
        return conversationMapper.updateById(byId) > 0 ? true : false;
    }

    @Override
    public List<Conversation> getHistoryNum(Integer num) {
        //查询数据
        LambdaQueryWrapper<Conversation> queryWrapper = new LambdaQueryWrapper<>();
        //根据用户ID查询历史记录
        queryWrapper.eq(Conversation::getUserId, StpUtil.getLoginId())
                .orderByDesc(Conversation::getConversationTime).last("limit " + num);
        return conversationMapper.selectList(queryWrapper);
    }
}
