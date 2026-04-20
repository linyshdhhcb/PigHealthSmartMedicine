package com.linyi.phsm.application.ingestion.prompt;

import com.linyi.phsm.domain.ingestion.model.enums.ChunkEnrichType;

import java.util.EnumMap;
import java.util.Map;

public final class EnricherPromptManager {

    private static final Map<ChunkEnrichType, String> DEFAULT_SYSTEM_PROMPTS = new EnumMap<>(ChunkEnrichType.class);

    static {
        DEFAULT_SYSTEM_PROMPTS.put(ChunkEnrichType.KEYWORDS, """
                从文本片段中提取 3-8 个关键词/短语。
                输出格式：JSON 数组，如 ["关键词1", "关键词2"]
                只输出 JSON，不要其他内容。
                """);

        DEFAULT_SYSTEM_PROMPTS.put(ChunkEnrichType.SUMMARY, """
                请用 1-3 句话对文本片段进行摘要，保持关键信息完整。
                直接输出摘要文本，不要添加标题或解释。
                """);

        DEFAULT_SYSTEM_PROMPTS.put(ChunkEnrichType.METADATA, """
                从文本片段中抽取可结构化的信息，输出 JSON 对象。
                只输出 JSON，不要其他内容。
                """);
    }

    private EnricherPromptManager() {
    }

    public static String systemPrompt(ChunkEnrichType type) {
        return DEFAULT_SYSTEM_PROMPTS.get(type);
    }
}
