package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Feedback;
import com.linyi.pig.entity.vo.feedback.FeedbackAddVo;
import com.linyi.pig.entity.vo.feedback.FeedbackQueryVo;
import com.linyi.pig.entity.vo.feedback.FeedbackUpdateVo;
import com.linyi.pig.mapper.FeedbackMapper;
import com.linyi.pig.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: FeedbackServiceImpl
* @Version: 1.0
* @Description: 反馈 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public PageResult<Feedback> feedbackPage(FeedbackQueryVo feedbackQueryVo) {
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(feedbackQueryVo.getName()), Feedback::getName, feedbackQueryVo.getName());
        queryWrapper.eq(StringUtils.isNotBlank(feedbackQueryVo.getEmail()), Feedback::getEmail, feedbackQueryVo.getEmail());
        queryWrapper.like(StringUtils.isNotBlank(feedbackQueryVo.getTitle()), Feedback::getTitle, feedbackQueryVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(feedbackQueryVo.getContent()), Feedback::getContent, feedbackQueryVo.getContent());

        //分页数据
        Page<Feedback> page = new Page<>(feedbackQueryVo.getPageNum(),feedbackQueryVo.getPageSize());
        //查询数据
        Page<Feedback> pageNew = feedbackMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), feedbackQueryVo.getPageNum(), feedbackQueryVo.getPageSize());
    }

    @Override
    public Boolean feedbackAdd(FeedbackAddVo feedbackAddVo){
        //创建实体对象
        Feedback feedback = new Feedback();
        //复制属性
        BeanUtils.copyProperties(feedbackAddVo, feedback);
        //插入数据
        return feedbackMapper.insert(feedback) > 0 ? true : false;
    }

    @Override
    public Boolean feedbackUpdate(FeedbackUpdateVo feedbackUpdateVo){
        //根据ID查询数据
        Feedback byId=this.getById(feedbackUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(feedbackUpdateVo, byId);
        //修改数据
        return feedbackMapper.updateById(byId) > 0 ? true : false;
    }
}
