package com.linyi.phsm.application.knowledge.mq;

import cn.hutool.json.JSONUtil;
import com.linyi.phsm.framework.mq.MessageWrapper;
import com.linyi.phsm.framework.mq.producer.DelegatingTransactionListener;
import com.linyi.phsm.framework.mq.producer.TransactionChecker;
import com.linyi.phsm.domain.knowledge.model.entity.KnowledgeDocumentDO;
import com.linyi.phsm.infrastructure.persistence.knowledge.mapper.KnowledgeDocumentMapper;
import com.linyi.phsm.domain.knowledge.model.enums.DocumentStatus;
import com.linyi.phsm.domain.knowledge.model.event.KnowledgeDocumentChunkEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 文档分块事务消息回查器
 * <p>
 * 按 topic 注册，Broker 回查时可路由到任意实例，通过查询 DB 中文档状态判断本地事务是否已提交
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KnowledgeDocumentChunkTransactionChecker implements TransactionChecker {

    private final KnowledgeDocumentMapper documentMapper;
    private final DelegatingTransactionListener transactionListener;

    @Value("knowledge-document-chunk_topic${unique-name:}")
    private String chunkTopic;

    @PostConstruct
    public void init() {
        transactionListener.registerChecker(chunkTopic, this);
    }

    @Override
    public boolean check(MessageWrapper<?> message) {
        log.info("[事务回查] 文档分块，消息体：{}", JSONUtil.toJsonStr(message));

        KnowledgeDocumentChunkEvent event = (KnowledgeDocumentChunkEvent) message.getBody();
        String docId = event.getDocId();
        KnowledgeDocumentDO documentDO = documentMapper.selectById(docId);

        return documentDO != null
                && DocumentStatus.RUNNING.getCode().equals(documentDO.getStatus());
    }
}
