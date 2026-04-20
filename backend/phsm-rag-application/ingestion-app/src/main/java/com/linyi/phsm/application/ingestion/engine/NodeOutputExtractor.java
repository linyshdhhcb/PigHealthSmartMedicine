package com.linyi.phsm.application.ingestion.engine;

import com.linyi.phsm.domain.ingestion.model.context.DocumentSource;
import com.linyi.phsm.domain.ingestion.model.context.IngestionContext;
import com.linyi.phsm.domain.ingestion.model.enums.IngestionNodeType;
import com.linyi.phsm.domain.ingestion.model.pipeline.NodeConfig;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 节点输出提取器
 * 负责从 IngestionContext 中提取特定节点的输出信息
 */
@Component
public class NodeOutputExtractor {

    public Map<String, Object> extract(IngestionContext context, NodeConfig config) {
        if (context == null || config == null) {
            return Map.of();
        }
        IngestionNodeType nodeType = resolveNodeType(config.getNodeType());
        if (nodeType == null) {
            return genericOutput(context);
        }
        return switch (nodeType) {
            case FETCHER -> fetcherOutput(context);
            case PARSER -> parserOutput(context);
            case ENHANCER -> enhancerOutput(context);
            case CHUNKER -> chunkerOutput(context);
            case ENRICHER -> enricherOutput(context);
            case INDEXER -> indexerOutput(context, config);
        };
    }

    private Map<String, Object> fetcherOutput(IngestionContext context) {
        Map<String, Object> output = new LinkedHashMap<>();
        DocumentSource source = context.getSource();
        if (source != null) {
            Map<String, Object> sourceView = new LinkedHashMap<>();
            sourceView.put("type", source.getType() == null ? null : source.getType().getValue());
            sourceView.put("location", source.getLocation());
            sourceView.put("fileName", source.getFileName());
            output.put("source", sourceView);
        }
        output.put("mimeType", context.getMimeType());
        byte[] raw = context.getRawBytes();
        if (raw != null) {
            output.put("rawBytesLength", raw.length);
            output.put("rawBytesBase64", Base64.getEncoder().encodeToString(raw));
        }
        return output;
    }

    private Map<String, Object> parserOutput(IngestionContext context) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("mimeType", context.getMimeType());
        output.put("rawText", context.getRawText());
        output.put("document", context.getDocument());
        return output;
    }

    private Map<String, Object> enhancerOutput(IngestionContext context) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("enhancedText", context.getEnhancedText());
        output.put("keywords", context.getKeywords());
        output.put("questions", context.getQuestions());
        output.put("metadata", context.getMetadata());
        return output;
    }

    private Map<String, Object> chunkerOutput(IngestionContext context) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("chunkCount", context.getChunks() == null ? 0 : context.getChunks().size());
        output.put("chunks", context.getChunks());
        return output;
    }

    private Map<String, Object> enricherOutput(IngestionContext context) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("chunkCount", context.getChunks() == null ? 0 : context.getChunks().size());
        output.put("chunks", context.getChunks());
        return output;
    }

    private Map<String, Object> indexerOutput(IngestionContext context, NodeConfig config) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("settings", config.getSettings());
        output.put("chunkCount", context.getChunks() == null ? 0 : context.getChunks().size());
        output.put("chunks", context.getChunks());
        return output;
    }

    private Map<String, Object> genericOutput(IngestionContext context) {
        Map<String, Object> output = new LinkedHashMap<>();
        output.put("mimeType", context.getMimeType());
        output.put("rawText", context.getRawText());
        output.put("enhancedText", context.getEnhancedText());
        output.put("keywords", context.getKeywords());
        output.put("questions", context.getQuestions());
        output.put("metadata", context.getMetadata());
        output.put("chunks", context.getChunks());
        return output;
    }

    private IngestionNodeType resolveNodeType(String nodeType) {
        if (nodeType == null || nodeType.isBlank()) {
            return null;
        }
        try {
            return IngestionNodeType.fromValue(nodeType);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
