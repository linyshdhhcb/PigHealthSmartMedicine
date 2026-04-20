package com.linyi.phsm.interfaces.rest.rag;

import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.web.Results;
import com.linyi.phsm.infrastructure.ai.AiModelProperties;
import com.linyi.phsm.infrastructure.config.MemoryProperties;
import com.linyi.phsm.infrastructure.config.RagConfigProperties;
import com.linyi.phsm.infrastructure.config.RagDefaultProperties;
import com.linyi.phsm.infrastructure.config.RagRateLimitProperties;
import com.linyi.phsm.domain.rag.model.vo.SystemSettingsVO;
import com.linyi.phsm.domain.rag.model.vo.SystemSettingsVO.AISettings;
import com.linyi.phsm.domain.rag.model.vo.SystemSettingsVO.DefaultSettings;
import com.linyi.phsm.domain.rag.model.vo.SystemSettingsVO.MemorySettings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RAG 设置控制器，负责系统 RAG、AI 模型等配置信息的查询
 */
@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RAGSettingsController {

    private final RagDefaultProperties ragDefaultProperties;
    private final RagConfigProperties ragConfigProperties;
    private final RagRateLimitProperties ragRateLimitProperties;
    private final MemoryProperties memoryProperties;
    private final AiModelProperties aiModelProperties;

    @Value("${spring.servlet.multipart.max-file-size:50MB}")
    private DataSize maxFileSize;

    @Value("${spring.servlet.multipart.max-request-size:100MB}")
    private DataSize maxRequestSize;

    /**
     * 获取系统 RAG、AI 模型等配置信息
     */
    @GetMapping("/settings")
    public Result<SystemSettingsVO> settings() {
        SystemSettingsVO response = SystemSettingsVO.builder()
                .upload(SystemSettingsVO.UploadSettings.builder()
                        .maxFileSize(maxFileSize.toBytes())
                        .maxRequestSize(maxRequestSize.toBytes())
                        .build())
                .rag(SystemSettingsVO.RagSettings.builder()
                        .defaultConfig(toDefaultSettings(ragDefaultProperties))
                        .queryRewrite(SystemSettingsVO.QueryRewriteSettings.builder()
                                .enabled(ragConfigProperties.getQueryRewriteEnabled())
                                .maxHistoryMessages(ragConfigProperties.getQueryRewriteMaxHistoryMessages())
                                .maxHistoryChars(ragConfigProperties.getQueryRewriteMaxHistoryChars())
                                .build())
                        .rateLimit(SystemSettingsVO.RateLimitSettings.builder()
                                .global(SystemSettingsVO.GlobalRateLimit.builder()
                                        .enabled(ragRateLimitProperties.getGlobalEnabled())
                                        .maxConcurrent(ragRateLimitProperties.getGlobalMaxConcurrent())
                                        .maxWaitSeconds(ragRateLimitProperties.getGlobalMaxWaitSeconds())
                                        .leaseSeconds(ragRateLimitProperties.getGlobalLeaseSeconds())
                                        .pollIntervalMs(ragRateLimitProperties.getGlobalPollIntervalMs())
                                        .build())
                                .build())
                        .memory(toMemorySettings(memoryProperties))
                        .build())
                .ai(toAISettings(aiModelProperties))
                .build();
        return Results.success(response);
    }

    private DefaultSettings toDefaultSettings(RagDefaultProperties props) {
        return DefaultSettings.builder()
                .collectionName(props.getCollectionName())
                .dimension(props.getDimension())
                .metricType(props.getMetricType())
                .build();
    }

    private MemorySettings toMemorySettings(MemoryProperties props) {
        return MemorySettings.builder()
                .historyKeepTurns(props.getHistoryKeepTurns())
                .ttlMinutes(props.getTtlMinutes())
                .summaryEnabled(props.getSummaryEnabled())
                .summaryStartTurns(props.getSummaryStartTurns())
                .summaryMaxChars(props.getSummaryMaxChars())
                .titleMaxLength(props.getTitleMaxLength())
                .build();
    }

    private AISettings toAISettings(AiModelProperties props) {
        Map<String, AISettings.ProviderConfig> providers = new HashMap<>();
        if (props.getProviders() != null) {
            props.getProviders().forEach((k, v) -> providers.put(k, AISettings.ProviderConfig.builder()
                    .url(v.getUrl())
                    .apiKey(v.getApiKey())
                    .endpoints(v.getEndpoints())
                    .build()));
        }

        return AISettings.builder()
                .providers(providers)
                .chat(toModelGroup(props.getChat()))
                .embedding(toModelGroup(props.getEmbedding()))
                .rerank(toModelGroup(props.getRerank()))
                .selection(props.getSelection() == null ? null : AISettings.Selection.builder()
                        .failureThreshold(props.getSelection().getFailureThreshold())
                        .openDurationMs(props.getSelection().getOpenDurationMs())
                        .build())
                .stream(props.getStream() == null ? null : AISettings.Stream.builder()
                        .messageChunkSize(props.getStream().getMessageChunkSize())
                        .build())
                .build();
    }

    private AISettings.ModelGroup toModelGroup(AiModelProperties.ModelGroup group) {
        if (group == null) {
            return null;
        }
        return AISettings.ModelGroup.builder()
                .defaultModel(group.getDefaultModel())
                .deepThinkingModel(group.getDeepThinkingModel())
                .candidates(group.getCandidates() == null ? null : group.getCandidates().stream()
                        .map(c -> AISettings.ModelCandidate.builder()
                                .id(c.getId())
                                .provider(c.getProvider())
                                .model(c.getModel())
                                .url(c.getUrl())
                                .dimension(c.getDimension())
                                .priority(c.getPriority())
                                .enabled(c.getEnabled())
                                .supportsThinking(c.getSupportsThinking())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
