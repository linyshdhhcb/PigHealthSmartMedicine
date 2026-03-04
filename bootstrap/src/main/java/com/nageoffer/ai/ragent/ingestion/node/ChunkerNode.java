

package com.nageoffer.ai.ragent.ingestion.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nageoffer.ai.ragent.core.chunk.ChunkingOptions;
import com.nageoffer.ai.ragent.core.chunk.ChunkingStrategyFactory;
import com.nageoffer.ai.ragent.core.chunk.VectorChunk;
import com.nageoffer.ai.ragent.core.chunk.ChunkingStrategy;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import com.nageoffer.ai.ragent.ingestion.domain.context.IngestionContext;
import com.nageoffer.ai.ragent.ingestion.domain.enums.IngestionNodeType;
import com.nageoffer.ai.ragent.ingestion.domain.pipeline.NodeConfig;
import com.nageoffer.ai.ragent.ingestion.domain.result.NodeResult;
import com.nageoffer.ai.ragent.ingestion.domain.settings.ChunkerSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文本分块节点
 * 负责将输入的完整文本（原始文本或增强后的文本）按照指定的策略切分成多个较小的文本块（Chunk）
 */
@Component
@RequiredArgsConstructor
public class ChunkerNode implements IngestionNode {

    private final ObjectMapper objectMapper;
    private final ChunkingStrategyFactory chunkingStrategyFactory;

    @Override
    public String getNodeType() {
        return IngestionNodeType.CHUNKER.getValue();
    }

    @Override
    public NodeResult execute(IngestionContext context, NodeConfig config) {
        String text = StringUtils.hasText(context.getEnhancedText()) ? context.getEnhancedText() : context.getRawText();
        if (!StringUtils.hasText(text)) {
            return NodeResult.fail(new ClientException("可分块文本为空"));
        }
        ChunkerSettings settings = parseSettings(config.getSettings());
        ChunkingStrategy chunker = chunkingStrategyFactory.requireStrategy(settings.getStrategy());
        if (chunker == null) {
            return NodeResult.fail(new ClientException("未找到分块策略: " + settings.getStrategy()));
        }

        ChunkingOptions chunkConfig = convertToChunkConfig(settings);
        List<VectorChunk> results = chunker.chunk(text, chunkConfig);
        List<VectorChunk> chunks = convertToVectorChunks(results);

        context.setChunks(chunks);
        return NodeResult.ok("已分块 " + chunks.size() + " 段");
    }

    private ChunkingOptions convertToChunkConfig(ChunkerSettings settings) {
        return ChunkingOptions.builder()
                .chunkSize(settings.getChunkSize())
                .overlapSize(settings.getOverlapSize())
                .separator(settings.getSeparator())
                .build();
    }

    private List<VectorChunk> convertToVectorChunks(List<VectorChunk> results) {
        return results.stream()
                .map(result -> VectorChunk.builder()
                        .chunkId(result.getChunkId())
                        .index(result.getIndex())
                        .content(result.getContent())
                        .metadata(result.getMetadata())
                        .embedding(result.getEmbedding())
                        .build())
                .collect(Collectors.toList());
    }

    private ChunkerSettings parseSettings(JsonNode node) {
        ChunkerSettings settings = objectMapper.convertValue(node, ChunkerSettings.class);
        if (settings.getChunkSize() == null || settings.getChunkSize() <= 0) {
            settings.setChunkSize(512);
        }
        if (settings.getOverlapSize() == null || settings.getOverlapSize() < 0) {
            settings.setOverlapSize(128);
        }
        return settings;
    }
}
