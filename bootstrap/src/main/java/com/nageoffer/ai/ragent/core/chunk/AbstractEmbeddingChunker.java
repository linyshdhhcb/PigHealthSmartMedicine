

package com.nageoffer.ai.ragent.core.chunk;

import com.nageoffer.ai.ragent.framework.exception.ClientException;
import com.nageoffer.ai.ragent.infra.embedding.EmbeddingClient;
import com.nageoffer.ai.ragent.infra.model.ModelSelector;
import com.nageoffer.ai.ragent.infra.model.ModelTarget;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 具有 Embedding 感知的分块模板类
 * 子类仅负责文本切分；Embedding 向量的生成由模板方法统一处理
 */
public abstract class AbstractEmbeddingChunker implements ChunkingStrategy {

    private static final String EMBEDDING_MODEL_KEY = "embeddingModel";

    private final ModelSelector modelSelector;
    private final Map<String, EmbeddingClient> embeddingClientsByProvider;

    protected AbstractEmbeddingChunker(ModelSelector modelSelector, List<EmbeddingClient> embeddingClients) {
        this.modelSelector = modelSelector;
        this.embeddingClientsByProvider = embeddingClients.stream()
                .collect(Collectors.toMap(EmbeddingClient::provider, Function.identity()));
    }

    @Override
    public final List<VectorChunk> chunk(String text, ChunkingOptions config) {
        List<VectorChunk> chunks = doChunk(text, config);
        if (chunks == null || chunks.isEmpty()) {
            return List.of();
        }
        if (chunks.stream().allMatch(chunk -> chunk.getEmbedding() != null && chunk.getEmbedding().length > 0)) {
            return chunks;
        }
        ModelTarget target = resolveEmbeddingTarget(config);
        List<List<Float>> vectors = embedBatch(chunks, target);
        applyEmbeddings(chunks, vectors);
        return chunks;
    }

    protected abstract List<VectorChunk> doChunk(String text, ChunkingOptions config);

    protected ModelTarget resolveEmbeddingTarget(ChunkingOptions config) {
        String modelId = config == null ? null : config.getMetadata(EMBEDDING_MODEL_KEY, null);
        List<ModelTarget> targets = modelSelector.selectEmbeddingCandidates();
        if (targets == null || targets.isEmpty()) {
            throw new ClientException("No embedding model available");
        }
        if (!StringUtils.hasText(modelId)) {
            return targets.get(0);
        }
        return targets.stream()
                .filter(target -> modelId.equals(target.id()))
                .findFirst()
                .orElseThrow(() -> new ClientException("Embedding model not matched: " + modelId));
    }

    private List<List<Float>> embedBatch(List<VectorChunk> chunks, ModelTarget target) {
        EmbeddingClient client = embeddingClientsByProvider.get(target.candidate().getProvider());
        if (client == null) {
            throw new ClientException("Embedding client not found: " + target.candidate().getProvider());
        }
        List<String> texts = chunks.stream()
                .map(chunk -> chunk.getContent() == null ? "" : chunk.getContent())
                .toList();
        return client.embedBatch(texts, target);
    }

    private void applyEmbeddings(List<VectorChunk> chunks, List<List<Float>> vectors) {
        if (vectors == null || vectors.size() != chunks.size()) {
            throw new ClientException("Embedding result size mismatch");
        }
        for (int i = 0; i < chunks.size(); i++) {
            List<Float> row = vectors.get(i);
            if (row == null) {
                throw new ClientException("Embedding result missing, index: " + i);
            }
            float[] vec = new float[row.size()];
            for (int j = 0; j < row.size(); j++) {
                vec[j] = row.get(j);
            }
            chunks.get(i).setEmbedding(vec);
        }
    }
}
