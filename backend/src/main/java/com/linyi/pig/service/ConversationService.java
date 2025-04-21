package com.linyi.pig.service;

import com.linyi.pig.entity.Conversation;
import com.linyi.pig.common.model.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.entity.vo.conversation.ConversationAddVo;
import com.linyi.pig.entity.vo.conversation.ConversationQueryVo;
import com.linyi.pig.entity.vo.conversation.ConversationUpdateVo;

import java.util.List;

/**
* @Author: linyi
* @Date: 2025-02-26 13:27:06
* @ClassName: ConversationService
* @Version: 1.0
* @Description: 对话 服务层
*/
public interface ConversationService extends IService<Conversation> {
    /**
     * 分页查询
     *
     * @param conversationQueryVo 分页查询实体
     * @return PageResult<Conversation>
     */
    PageResult<Conversation> conversationPage(ConversationQueryVo conversationQueryVo);

    /**
     * 新增
     *
     * @param conversationAddVo 新增实体
     * @return Boolean
     */
    Boolean conversationAdd(ConversationAddVo conversationAddVo);

    /**
     * 修改
     *
     * @param conversationUpdateVo 修改实体
     * @return Boolean
     */
    Boolean conversationUpdate(ConversationUpdateVo conversationUpdateVo);

    /**
     * 根据几次历史对话记录
     * @param num 次数
     * @return
     */
    List<Conversation> getHistoryNum(Integer num);

    Conversation getOllama(String prompt);

    Conversation getApiLLM(String prompt);
}
