

package com.nageoffer.ai.ragent.rag.controller.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 系统设置视图对象
 * 包含RAG和AI相关的配置信息
 */
@Setter
@Getter
public class SystemSettingsVO {

    private RagSettings rag;
    private AISettings ai;

    public SystemSettingsVO(RagSettings rag, AISettings ai) {
        this.rag = rag;
        this.ai = ai;
    }

    public static SystemSettingsVOBuilder builder() {
        return new SystemSettingsVOBuilder();
    }

    public static class SystemSettingsVOBuilder {
        private RagSettings rag;
        private AISettings ai;

        public SystemSettingsVOBuilder rag(RagSettings rag) {
            this.rag = rag;
            return this;
        }

        public SystemSettingsVOBuilder ai(AISettings ai) {
            this.ai = ai;
            return this;
        }

        public SystemSettingsVO build() {
            return new SystemSettingsVO(rag, ai);
        }
    }

    @Data
    @Builder
    public static class AISettings {
        private Map<String, ProviderConfig> providers;
        private ModelGroup chat;
        private ModelGroup embedding;
        private ModelGroup rerank;
        private Selection selection;
        private Stream stream;

        @Data
        @Builder
        public static class ProviderConfig {
            private String url;
            private String apiKey;
            private Map<String, String> endpoints;
        }

        @Data
        @Builder
        public static class ModelGroup {
            private String defaultModel;
            private String deepThinkingModel;
            private List<ModelCandidate> candidates;
        }

        @Data
        @Builder
        public static class ModelCandidate {
            private String id;
            private String provider;
            private String model;
            private String url;
            private Integer dimension;
            private Integer priority;
            private Boolean enabled;
            private Boolean supportsThinking;
        }

        @Data
        @Builder
        public static class Selection {
            private Integer failureThreshold;
            private Long openDurationMs;
        }

        @Data
        @Builder
        public static class Stream {
            private Integer messageChunkSize;
        }
    }

    @Data
    @Builder
    public static class DefaultSettings {
        private String collectionName;
        private Integer dimension;
        private String metricType;
    }

    @Data
    @Builder
    public static class MemorySettings {
        private Integer historyKeepTurns;
        private Integer ttlMinutes;
        private Boolean summaryEnabled;
        private Integer summaryStartTurns;
        private Integer summaryMaxChars;
        private Integer titleMaxLength;
    }

    @Setter
    @Getter
    public static class RagSettings {
        @JsonProperty("default")
        private DefaultSettings defaultConfig;
        private QueryRewriteSettings queryRewrite;
        private RateLimitSettings rateLimit;
        private MemorySettings memory;

        public RagSettings(DefaultSettings defaultConfig, QueryRewriteSettings queryRewrite,
                           RateLimitSettings rateLimit, MemorySettings memory) {
            this.defaultConfig = defaultConfig;
            this.queryRewrite = queryRewrite;
            this.rateLimit = rateLimit;
            this.memory = memory;
        }

        public static RagSettingsBuilder builder() {
            return new RagSettingsBuilder();
        }

        public static class RagSettingsBuilder {
            private DefaultSettings defaultConfig;
            private QueryRewriteSettings queryRewrite;
            private RateLimitSettings rateLimit;
            private MemorySettings memory;

            public RagSettingsBuilder defaultConfig(DefaultSettings defaultConfig) {
                this.defaultConfig = defaultConfig;
                return this;
            }

            public RagSettingsBuilder queryRewrite(QueryRewriteSettings queryRewrite) {
                this.queryRewrite = queryRewrite;
                return this;
            }

            public RagSettingsBuilder rateLimit(RateLimitSettings rateLimit) {
                this.rateLimit = rateLimit;
                return this;
            }

            public RagSettingsBuilder memory(MemorySettings memory) {
                this.memory = memory;
                return this;
            }

            public RagSettings build() {
                return new RagSettings(defaultConfig, queryRewrite, rateLimit, memory);
            }
        }
    }

    @Setter
    @Getter
    public static class QueryRewriteSettings {
        private Boolean enabled;
        private Integer maxHistoryMessages;
        private Integer maxHistoryChars;

        public QueryRewriteSettings(Boolean enabled, Integer maxHistoryMessages, Integer maxHistoryChars) {
            this.enabled = enabled;
            this.maxHistoryMessages = maxHistoryMessages;
            this.maxHistoryChars = maxHistoryChars;
        }

        public static QueryRewriteSettingsBuilder builder() {
            return new QueryRewriteSettingsBuilder();
        }

        public static class QueryRewriteSettingsBuilder {
            private Boolean enabled;
            private Integer maxHistoryMessages;
            private Integer maxHistoryChars;

            public QueryRewriteSettingsBuilder enabled(Boolean enabled) {
                this.enabled = enabled;
                return this;
            }

            public QueryRewriteSettingsBuilder maxHistoryMessages(Integer maxHistoryMessages) {
                this.maxHistoryMessages = maxHistoryMessages;
                return this;
            }

            public QueryRewriteSettingsBuilder maxHistoryChars(Integer maxHistoryChars) {
                this.maxHistoryChars = maxHistoryChars;
                return this;
            }

            public QueryRewriteSettings build() {
                return new QueryRewriteSettings(enabled, maxHistoryMessages, maxHistoryChars);
            }
        }
    }

    @Setter
    @Getter
    public static class RateLimitSettings {
        private GlobalRateLimit global;

        public RateLimitSettings(GlobalRateLimit global) {
            this.global = global;
        }

        public static RateLimitSettingsBuilder builder() {
            return new RateLimitSettingsBuilder();
        }

        public static class RateLimitSettingsBuilder {
            private GlobalRateLimit global;

            public RateLimitSettingsBuilder global(GlobalRateLimit global) {
                this.global = global;
                return this;
            }

            public RateLimitSettings build() {
                return new RateLimitSettings(global);
            }
        }
    }

    @Setter
    @Getter
    public static class GlobalRateLimit {
        private Boolean enabled;
        private Integer maxConcurrent;
        private Integer maxWaitSeconds;
        private Integer leaseSeconds;
        private Integer pollIntervalMs;

        public GlobalRateLimit(Boolean enabled, Integer maxConcurrent, Integer maxWaitSeconds,
                               Integer leaseSeconds, Integer pollIntervalMs) {
            this.enabled = enabled;
            this.maxConcurrent = maxConcurrent;
            this.maxWaitSeconds = maxWaitSeconds;
            this.leaseSeconds = leaseSeconds;
            this.pollIntervalMs = pollIntervalMs;
        }

        public static GlobalRateLimitBuilder builder() {
            return new GlobalRateLimitBuilder();
        }

        public static class GlobalRateLimitBuilder {
            private Boolean enabled;
            private Integer maxConcurrent;
            private Integer maxWaitSeconds;
            private Integer leaseSeconds;
            private Integer pollIntervalMs;

            public GlobalRateLimitBuilder enabled(Boolean enabled) {
                this.enabled = enabled;
                return this;
            }

            public GlobalRateLimitBuilder maxConcurrent(Integer maxConcurrent) {
                this.maxConcurrent = maxConcurrent;
                return this;
            }

            public GlobalRateLimitBuilder maxWaitSeconds(Integer maxWaitSeconds) {
                this.maxWaitSeconds = maxWaitSeconds;
                return this;
            }

            public GlobalRateLimitBuilder leaseSeconds(Integer leaseSeconds) {
                this.leaseSeconds = leaseSeconds;
                return this;
            }

            public GlobalRateLimitBuilder pollIntervalMs(Integer pollIntervalMs) {
                this.pollIntervalMs = pollIntervalMs;
                return this;
            }

            public GlobalRateLimit build() {
                return new GlobalRateLimit(enabled, maxConcurrent, maxWaitSeconds, leaseSeconds, pollIntervalMs);
            }
        }
    }
}
