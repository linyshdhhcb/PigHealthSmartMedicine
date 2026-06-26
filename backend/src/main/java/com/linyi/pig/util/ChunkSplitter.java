package com.linyi.pig.util;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.IntArrayList;
import com.linyi.pig.config.ChunkProperties;

import java.util.ArrayList;
import java.util.List;

public class ChunkSplitter {

    private static final EncodingRegistry registry = Encodings.newLazyEncodingRegistry();
    private static final Encoding encoding = registry.getEncoding(EncodingType.CL100K_BASE);

    private final int chunkSize;
    private final int chunkOverlap;
    private final int minChunkSizeChars;
    private final int minChunkLengthToEmbed;
    private final int maxNumChunks;
    private final boolean keepSeparator;

    public ChunkSplitter(ChunkProperties props) {
        this.chunkSize = props.getChunkSize();
        this.chunkOverlap = props.getChunkOverlap();
        this.minChunkSizeChars = props.getMinChunkSizeChars();
        this.minChunkLengthToEmbed = props.getMinChunkLengthToEmbed();
        this.maxNumChunks = props.getMaxNumChunks();
        this.keepSeparator = props.isKeepSeparator();
    }

    public List<ChunkResult> split(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        IntArrayList tokenList = encoding.encode(text);
        int[] tokens = tokenList.toArray();
        if (tokens.length == 0) {
            return List.of();
        }

        int effectiveStep = chunkSize - chunkOverlap;
        if (effectiveStep <= 0) {
            effectiveStep = chunkSize / 2;
            if (effectiveStep <= 0) {
                effectiveStep = 1;
            }
        }

        List<ChunkResult> results = new ArrayList<>();
        int start = 0;
        int chunkIndex = 0;

        while (start < tokens.length && chunkIndex < maxNumChunks) {
            int end = Math.min(start + chunkSize, tokens.length);
            int[] chunkTokens = new int[end - start];
            System.arraycopy(tokens, start, chunkTokens, 0, end - start);
            IntArrayList chunkTokenList = new IntArrayList(chunkTokens.length);
            for (int t : chunkTokens) {
                chunkTokenList.add(t);
            }
            String chunkText = encoding.decode(chunkTokenList);

            if (!keepSeparator) {
                chunkText = chunkText.replaceAll("[\n\r\t]+", " ").trim();
            }

            if (chunkText.length() >= minChunkLengthToEmbed) {
                results.add(new ChunkResult(chunkText, chunkTokens.length));
            }

            if (end >= tokens.length) {
                break;
            }

            start += effectiveStep;
            chunkIndex++;
        }

        return results;
    }

    public int countTokens(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return encoding.encode(text).size();
    }

    public static class ChunkResult {
        private final String text;
        private final int tokenCount;

        public ChunkResult(String text, int tokenCount) {
            this.text = text;
            this.tokenCount = tokenCount;
        }

        public String getText() {
            return text;
        }

        public int getTokenCount() {
            return tokenCount;
        }
    }
}
