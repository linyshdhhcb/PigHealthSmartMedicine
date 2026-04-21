package com.linyi.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Conversation;
import com.linyi.pig.entity.vo.conversation.ConversationAddVo;
import com.linyi.pig.entity.vo.conversation.ConversationQueryVo;
import com.linyi.pig.entity.vo.conversation.ConversationRagResultVo;
import com.linyi.pig.entity.vo.conversation.ConversationUpdateVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeChunkView;
import com.linyi.pig.entity.vo.knowledge.KnowledgeQaResult;
import com.linyi.pig.exception.LinyiException;
import com.linyi.pig.mapper.ConversationMapper;
import com.linyi.pig.service.ConversationService;
import com.linyi.pig.service.KnowledgeFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation>
        implements ConversationService {

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private KnowledgeFileService knowledgeFileService;

    @Override
    public PageResult<Conversation> conversationPage(ConversationQueryVo conversationQueryVo) {
        LambdaQueryWrapper<Conversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Optional.ofNullable(conversationQueryVo.getId()).isPresent(), Conversation::getId,
                conversationQueryVo.getId());
        queryWrapper.eq(Optional.ofNullable(conversationQueryVo.getUserId()).isPresent(),
                Conversation::getUserId,
                conversationQueryVo.getUserId());
        queryWrapper.eq(StringUtils.isNotBlank(conversationQueryVo.getUserInput()), Conversation::getUserInput,
                conversationQueryVo.getUserInput());
        queryWrapper.eq(StringUtils.isNotBlank(conversationQueryVo.getAiResponse()),
                Conversation::getAiResponse,
                conversationQueryVo.getAiResponse());
        queryWrapper.eq(Optional.ofNullable(conversationQueryVo.getAiResponse()).isPresent(),
                Conversation::getAiResponse, conversationQueryVo.getAiResponse());
        queryWrapper.gt(Optional.ofNullable(conversationQueryVo.getStartConversationTime()).isPresent(),
                Conversation::getConversationTime, conversationQueryVo.getStartConversationTime());
        queryWrapper.lt(Optional.ofNullable(conversationQueryVo.getEndConversationTime()).isPresent(),
                Conversation::getConversationTime, conversationQueryVo.getEndConversationTime());
        queryWrapper.eq(StringUtils.isNotBlank(conversationQueryVo.getModelName()), Conversation::getModelName,
                conversationQueryVo.getModelName());
        queryWrapper.ge(Optional.ofNullable(conversationQueryVo.getResponseTime()).isPresent(),
                Conversation::getResponseTime, conversationQueryVo.getResponseTime());

        Page<Conversation> page = new Page<>(conversationQueryVo.getPageNum(),
                conversationQueryVo.getPageSize());
        Page<Conversation> pageNew = conversationMapper.selectPage(page, queryWrapper);
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(),
                conversationQueryVo.getPageNum(), conversationQueryVo.getPageSize());
    }

    @Override
    public Boolean conversationAdd(ConversationAddVo conversationAddVo) {
        Conversation conversation = new Conversation();
        BeanUtils.copyProperties(conversationAddVo, conversation);
        return conversationMapper.insert(conversation) > 0;
    }

    @Override
    public Boolean conversationUpdate(ConversationUpdateVo conversationUpdateVo) {
        Conversation byId = this.getById(conversationUpdateVo.getId());
        if (Optional.ofNullable(byId).isEmpty()) {
            log.error("数据不存在");
            return false;
        }
        BeanUtils.copyProperties(conversationUpdateVo, byId);
        return conversationMapper.updateById(byId) > 0;
    }

    @Override
    public List<Conversation> getHistoryNum(Integer num) {
        LambdaQueryWrapper<Conversation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Conversation::getUserId, StpUtil.getLoginId())
                .orderByDesc(Conversation::getConversationTime).last("limit " + num);
        return conversationMapper.selectList(queryWrapper);
    }

    @Override
    public Conversation getOllama(String msg) {
        return getOllama(msg, null);
    }

    @Override
    public List<Conversation> listBySessionId(Long sessionId) {
        LambdaQueryWrapper<Conversation> qw = new LambdaQueryWrapper<>();
        qw.eq(Conversation::getUserId, StpUtil.getLoginId())
                .eq(Conversation::getSessionId, sessionId)
                .orderByAsc(Conversation::getId);
        return conversationMapper.selectList(qw);
    }

    @Override
    public Conversation getOllama(String msg, Long sessionId) {
        return getKnowledgeAnswer(msg, null, sessionId);
    }

    @Override
    public Conversation getKnowledgeAnswer(String prompt, Long kbId, Long sessionId) {
        return toConversation(getKnowledgeAnswerResult(prompt, kbId, sessionId));
    }

    @Override
    public ConversationRagResultVo getKnowledgeAnswerResult(String prompt, Long kbId, Long sessionId) {
        log.info("问题是:{}", prompt);
        if (StringUtils.isBlank(prompt)) {
            throw new LinyiException("请输入chat内容");
        }
        long startTime = System.nanoTime();
        KnowledgeQaResult qa = knowledgeFileService.qaByKbId(kbId, prompt, 5);
        long endTime = System.nanoTime();
        String formattedTimeTaken = String.format("%.2f", (endTime - startTime) / 1e9);
        String answer = qa.getAnswer();
        log.info("回答:{}", answer);
        Conversation conversation = Conversation.builder()
                .userId(Integer.valueOf(StpUtil.getLoginId().toString()))
                .sessionId(sessionId)
                .userInput(prompt)
                .aiResponse(answer)
                .modelName("rag")
                .responseTime(new BigDecimal(formattedTimeTaken))
                .build();
        conversationMapper.insert(conversation);

        ConversationRagResultVo vo = new ConversationRagResultVo();
        vo.setSessionId(sessionId);
        vo.setPrompt(prompt);
        vo.setAiResponse(answer);
        vo.setChunks(qa.getChunks());
        return vo;
    }

    private Conversation toConversation(ConversationRagResultVo vo) {
        Conversation conversation = new Conversation();
        conversation.setSessionId(vo.getSessionId());
        conversation.setUserInput(vo.getPrompt());
        conversation.setAiResponse(vo.getAiResponse());
        conversation.setModelName("rag");
        return conversation;
    }

    @Override
    public Conversation getApiLLM(String prompt) {
        return null;
    }
}
