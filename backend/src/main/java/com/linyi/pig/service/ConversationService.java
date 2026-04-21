package com.linyi.pig.service;

import com.linyi.pig.entity.Conversation;
import com.linyi.pig.common.model.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.entity.vo.conversation.ConversationAddVo;
import com.linyi.pig.entity.vo.conversation.ConversationQueryVo;
import com.linyi.pig.entity.vo.conversation.ConversationUpdateVo;
import com.linyi.pig.entity.vo.conversation.ConversationRagResultVo;

import java.util.List;

/**
 * @Author: linyi
 * @Date: 2025-02-26 13:27:06
 * @ClassName: ConversationService
 * @Version: 1.0
 * @Description: 对话 服务层
 */
public interface ConversationService extends IService<Conversation> {
    PageResult<Conversation> conversationPage(ConversationQueryVo conversationQueryVo);

    Boolean conversationAdd(ConversationAddVo conversationAddVo);

    Boolean conversationUpdate(ConversationUpdateVo conversationUpdateVo);

    List<Conversation> getHistoryNum(Integer num);

    List<Conversation> listBySessionId(Long sessionId);

    Conversation getOllama(String prompt);

    Conversation getOllama(String prompt, Long sessionId);

    Conversation getKnowledgeAnswer(String prompt, Long kbId, Long sessionId);

    ConversationRagResultVo getKnowledgeAnswerResult(String prompt, Long kbId, Long sessionId);

    Conversation getApiLLM(String prompt);
}
