package com.linyi.phsm.application.rag.mq;

import com.linyi.phsm.framework.mq.MessageWrapper;
import com.linyi.phsm.domain.rag.model.event.MessageFeedbackEvent;
import com.linyi.phsm.application.rag.service.MessageFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消息反馈 MQ 消费者，负责将点赞/点踩事件异步持久化到数据库
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = "message-feedback_topic${unique-name:}",
        consumerGroup = "message-feedback_cg${unique-name:}"
)
public class MessageFeedbackConsumer implements RocketMQListener<MessageWrapper<MessageFeedbackEvent>> {

    private final MessageFeedbackService feedbackService;

    @Override
    public void onMessage(MessageWrapper<MessageFeedbackEvent> message) {
        MessageFeedbackEvent event = message.getBody();

        log.info("[消费者] 开始处理点赞/点踩事件，messageId: {}, userId: {}, vote: {}, keys: {}",
                event.getMessageId(), event.getUserId(), event.getVote(), message.getKeys());
        feedbackService.submitFeedbackByEvent(event);
    }
}
