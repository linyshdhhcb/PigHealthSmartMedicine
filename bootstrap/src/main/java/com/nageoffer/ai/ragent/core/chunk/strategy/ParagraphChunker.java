

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
 * 按段落进行文本分块的策略实现类
 */
@Component
public class ParagraphChunker extends AbstractEmbeddingChunker {

    public ParagraphChunker(ModelSelector modelSelector, List<EmbeddingClient> embeddingClients) {
        super(modelSelector, embeddingClients);
    }

    @Override
    public ChunkingMode getType() {
        return ChunkingMode.PARAGRAPH;
    }

    @Override
    protected List<VectorChunk> doChunk(String text, ChunkingOptions settings) {
        if (!StringUtils.hasText(text)) {
            return List.of();
        }
        int chunkSize = settings != null && settings.getChunkSize() != null ? settings.getChunkSize() : 512;
        int overlap = settings != null && settings.getOverlapSize() != null ? settings.getOverlapSize() : 128;

        List<Span> paragraphs = splitParagraphs(text);
        if (paragraphs.isEmpty()) {
            return List.of();
        }

        List<VectorChunk> chunks = new ArrayList<>();
        int index = 0;
        int paraIndex = 0;
        int nextStart = paragraphs.get(0).start;

        while (paraIndex < paragraphs.size()) {
            Span first = paragraphs.get(paraIndex);
            int chunkStart = Math.max(nextStart, first.start);
            int chunkEnd = chunkStart;
            int cursor = paraIndex;
            while (cursor < paragraphs.size()) {
                Span span = paragraphs.get(cursor);
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
            paraIndex = findParagraphIndex(paragraphs, nextStart);
        }

        return chunks;
    }

    private List<Span> splitParagraphs(String text) {
        List<Span> spans = new ArrayList<>();
        int len = text.length();
        int start = 0;
        int i = 0;
        while (i < len) {
            if (text.charAt(i) == '\n') {
                int j = i;
                while (j < len && text.charAt(j) == '\n') {
                    j++;
                }
                if (j - i >= 2) {
                    spans.add(new Span(start, i));
                    start = j;
                }
                i = j;
            } else {
                i++;
            }
        }
        if (start < len) {
            spans.add(new Span(start, len));
        }
        return spans;
    }

    private int findParagraphIndex(List<Span> spans, int start) {
        for (int i = 0; i < spans.size(); i++) {
            Span span = spans.get(i);
            if (span.end > start) {
                return i;
            }
        }
        return spans.size();
    }

    private record Span(int start, int end) {
    }
}
