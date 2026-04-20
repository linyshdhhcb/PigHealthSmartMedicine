package com.linyi.phsm.infrastructure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RagConfigProperties {

    @Value("${rag.query-rewrite.enabled:true}")
    private Boolean queryRewriteEnabled;

    @Value("${rag.query-rewrite.max-history-messages:4}")
    private Integer queryRewriteMaxHistoryMessages;

    @Value("${rag.query-rewrite.max-history-chars:500}")
    private Integer queryRewriteMaxHistoryChars;
}
