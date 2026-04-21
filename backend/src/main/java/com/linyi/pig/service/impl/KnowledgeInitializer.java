package com.linyi.pig.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class KnowledgeInitializer {

    @PostConstruct
    void init() {
        log.info("RAG重构已启用：不再从本地 knowledge 目录加载文档，改为 MinIO + Milvus");
    }
}
