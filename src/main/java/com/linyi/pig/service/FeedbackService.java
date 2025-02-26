package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Feedback;
import com.linyi.pig.entity.vo.feedback.FeedbackAddVo;
import com.linyi.pig.entity.vo.feedback.FeedbackQueryVo;
import com.linyi.pig.entity.vo.feedback.FeedbackUpdateVo;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: FeedbackService
* @Version: 1.0
* @Description: 反馈 服务层
*/
public interface FeedbackService extends IService<Feedback> {
    /**
     * 分页查询
     *
     * @param feedbackQueryVo 分页查询实体
     * @return PageResult<Feedback>
     */
    PageResult<Feedback> feedbackPage(FeedbackQueryVo feedbackQueryVo);

    /**
     * 新增
     *
     * @param feedbackAddVo 新增实体
     * @return Boolean
     */
    Boolean feedbackAdd(FeedbackAddVo feedbackAddVo);

    /**
     * 修改
     *
     * @param feedbackUpdateVo 修改实体
     * @return Boolean
     */
    Boolean feedbackUpdate(FeedbackUpdateVo feedbackUpdateVo);
}
