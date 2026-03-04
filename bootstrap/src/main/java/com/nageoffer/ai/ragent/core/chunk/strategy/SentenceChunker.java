

package com.nageoffer.ai.ragent.core.chunk.strategy;

import cn.hutool.core.util.IdUtil;
import com.nageoffer.ai.ragent.core.chunk.AbstractEmbeddingChunker;
import com.nageoffer.ai.ragent.core.chunk.ChunkingOptions;
import com.nageoffer.ai.ragent.core.chunk.ChunkingMode;
import com.nageoffer.ai.ragent.core.chunk.VectorChunk;
import com.nageoffer.ai.ragent.infra.embedding.EmbeddingClient;
import com.nageoffer.ai.ragent.infra.model.ModelSelector;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 句子切分策略实现类
 * 该类通过识别句子边界（如标点符号和换行符）将文本切分为多个分块（Chunk）
 */
@Component
public class SentenceChunker extends AbstractEmbeddingChunker {

    public SentenceChunker(ModelSelector modelSelector, List<EmbeddingClient> embeddingClients) {
        super(modelSelector, embeddingClients);
    }

    @Override
    public ChunkingMode getType() {
        return ChunkingMode.SENTENCE;
    }

    @Override
    protected List<VectorChunk> doChunk(String text, ChunkingOptions settings) {
        if (!StringUtils.hasText(text)) {
            return List.of();
        }
        int chunkSize = settings != null && settings.getChunkSize() != null ? settings.getChunkSize() : 512;
        int overlap = settings != null && settings.getOverlapSize() != null ? settings.getOverlapSize() : 128;

        List<Span> sentences = splitSentences(text);
        if (sentences.isEmpty()) {
            return List.of();
        }

        List<VectorChunk> chunks = new ArrayList<>();
        int index = 0;
        int sentenceIndex = 0;
        int nextStart = sentences.get(0).start;

        while (sentenceIndex < sentences.size()) {
            Span first = sentences.get(sentenceIndex);
            int chunkStart = Math.max(nextStart, first.start);
            int chunkEnd = chunkStart;
            int cursor = sentenceIndex;
            while (cursor < sentences.size()) {
                Span span = sentences.get(cursor);
                int candidateEnd = span.end;
                int candidateSize = candidateEnd - chunkStart;
                if (candidateSize > chunkSize && chunkEnd > chunkStart) {
                    break;
                }
                chunkEnd = candidateEnd;
                cursor++;
                if (candidateSize >= chunkSize) {
                    break;
                }
            }

            String content = text.substring(chunkStart, chunkEnd).trim();
            if (StringUtils.hasText(content)) {
                chunks.add(VectorChunk.builder()
                        .chunkId(IdUtil.getSnowflakeNextIdStr())
                        .index(index++)
                        .content(content)
                        .build());
            }
            if (chunkEnd >= text.length()) {
                break;
            }
            nextStart = Math.max(chunkEnd - Math.max(0, overlap), chunkStart);
            sentenceIndex = findSentenceIndex(sentences, nextStart);
        }

        return chunks;
    }

    private List<Span> splitSentences(String text) {
        List<Span> spans = new ArrayList<>();
        int len = text.length();
        int start = 0;
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (isBoundary(c)) {
                int end = i + 1;
                spans.add(new Span(start, end));
                start = end;
            }
        }
        if (start < len) {
            spans.add(new Span(start, len));
        }
        return spans;
    }

    private boolean isBoundary(char c) {
        return c == '.' || c == '!' || c == '?' || c == '。' || c == '！' || c == '？' || c == '\n';
    }

    private int findSentenceIndex(List<Span> sentences, int start) {
        for (int i = 0; i < sentences.size(); i++) {
            Span span = sentences.get(i);
            if (span.end > start) {
                return i;
            }
        }
        return sentences.size();
    }

    private record Span(int start, int end) {
    }
}
