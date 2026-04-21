package com.linyi.pig.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linyi.pig.entity.KnowledgeChunk;
import com.linyi.pig.entity.KnowledgeFile;
import com.linyi.pig.mapper.KnowledgeChunkMapper;
import com.linyi.pig.mapper.KnowledgeFileMapper;
import com.linyi.pig.service.MilvusVectorService;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.ConsistencyLevel;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.DescribeCollectionReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.collection.request.LoadCollectionReq;
import io.milvus.v2.service.index.request.CreateIndexReq;
import io.milvus.v2.service.index.request.ListIndexesReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Milvus 向量存储实现，按 ragent 的成熟方案统一为：
 * id + content + metadata(JSON) + embedding
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MilvusVectorServiceImpl implements MilvusVectorService {

    private static final Gson GSON = new Gson();
    private static final String DEFAULT_COLLECTION_NAME = "rag_default_store_v2";
    private static final String DEFAULT_KB_NAME = "默认知识库";
    private static final String ID_FIELD = "id";
    private static final String CONTENT_FIELD = "content";
    private static final String METADATA_FIELD = "metadata";
    private static final String EMBEDDING_FIELD = "embedding";

    private final AiModelRouterService aiModelRouterService;
    private final KnowledgeChunkMapper knowledgeChunkMapper;
    private final KnowledgeFileMapper knowledgeFileMapper;

    @Value("${spring.ai.vectorstore.milvus.client.host:127.0.0.1}")
    private String milvusHost;

    @Value("${spring.ai.vectorstore.milvus.client.port:19530}")
    private Integer milvusPort;

    @Value("${spring.ai.vectorstore.milvus.collection-name:rag_default_store_v2}")
    private String collectionName;

    @Value("${spring.ai.vectorstore.milvus.dimension:4096}")
    private Integer dimension;

    private MilvusClientV2 milvusClient;

    @PostConstruct
    public void init() {
        try {
            String uri = normalizeMilvusUri(milvusHost, milvusPort);
            milvusClient = new MilvusClientV2(ConnectConfig.builder().uri(uri).build());
            ensureCollection();
            log.info("Milvus client initialized successfully at {}", uri);
        } catch (Exception e) {
            milvusClient = null;
            log.error("Milvus client init failed at {}:{}, RAG vector store will retry lazily.", milvusHost, milvusPort, e);
        }
    }

    @Override
    public void ensureCollection() {
        if (milvusClient == null) {
            throw new IllegalStateException("Milvus client not initialized");
        }

        String col = resolveCollectionName();
        boolean exists = Boolean.TRUE.equals(milvusClient.hasCollection(HasCollectionReq.builder().collectionName(col).build()));

        if (!exists) {
            createCollectionAndIndex(col);
            return;
        }

        try {
            var desc = milvusClient.describeCollection(DescribeCollectionReq.builder().collectionName(col).build());
            boolean hasId = desc.getFieldNames() != null && desc.getFieldNames().contains(ID_FIELD);
            boolean hasContent = desc.getFieldNames() != null && desc.getFieldNames().contains(CONTENT_FIELD);
            boolean hasMetadata = desc.getFieldNames() != null && desc.getFieldNames().contains(METADATA_FIELD);
            boolean hasEmbedding = desc.getVectorFieldNames() != null && desc.getVectorFieldNames().contains(EMBEDDING_FIELD);
            boolean hasIndex = milvusClient.listIndexes(ListIndexesReq.builder().collectionName(col).build()) != null
                    && !milvusClient.listIndexes(ListIndexesReq.builder().collectionName(col).build()).isEmpty();

            if (!hasId || !hasContent || !hasMetadata || !hasEmbedding) {
                throw new IllegalStateException("Milvus collection schema mismatch, please delete old collection and re-upload data: " + col);
            }
            if (!hasIndex) {
                createVectorIndex(col);
            }
            milvusClient.loadCollection(LoadCollectionReq.builder().collectionName(col).build());
            log.info("Milvus collection verified and loaded: {}", col);
        } catch (Exception e) {
            throw new IllegalStateException("Milvus collection init failed for " + col + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void upsertChunk(KnowledgeChunk chunk, List<Float> vector) {
        if (chunk == null || vector == null || vector.isEmpty()) {
            return;
        }
        if (milvusClient == null) {
            log.warn("Milvus client unavailable, skip insert chunkId={}", chunk.getId());
            return;
        }

        ensureCollection();
        String col = resolveCollectionName();

        String content = chunk.getContent() == null ? "" : chunk.getContent();
        if (content.length() > 65535) {
            content = content.substring(0, 65535);
        }

        JsonObject metadata = new JsonObject();
        metadata.addProperty("collection_name", col);
        metadata.addProperty("kb_id", chunk.getKbId());
        metadata.addProperty("doc_id", chunk.getDocId());
        metadata.addProperty("chunk_index", chunk.getChunkIndex());
        metadata.addProperty("content_hash", chunk.getContentHash());
        metadata.addProperty("char_count", chunk.getCharCount());
        metadata.addProperty("token_count", chunk.getTokenCount());

        JsonObject row = new JsonObject();
        row.addProperty(ID_FIELD, String.valueOf(chunk.getId()));
        row.addProperty(CONTENT_FIELD, content);
        row.add(METADATA_FIELD, metadata);
        row.add(EMBEDDING_FIELD, toJsonArray(vector));

        InsertResp resp = milvusClient.insert(InsertReq.builder()
                .collectionName(col)
                .data(List.of(row))
                .build());
        log.info("Milvus insert chunkId={}, insertCnt={}", chunk.getId(), resp == null ? null : resp.getInsertCnt());

        try {
            milvusClient.loadCollection(LoadCollectionReq.builder().collectionName(col).build());
            List<KnowledgeChunk> verify = searchByChunkId(chunk.getId());
            log.info("Milvus insert verify chunkId={}, matchedCount={}", chunk.getId(), verify.size());
        } catch (Exception e) {
            log.warn("Milvus insert verification failed, chunkId={}", chunk.getId(), e);
        }
    }

    @Override
    public List<KnowledgeChunk> searchByKbId(Long kbId, List<Float> vector, int topK) {
        if (vector == null || vector.isEmpty() || milvusClient == null) {
            return List.of();
        }

        ensureCollection();
        String col = resolveCollectionName();
        String filter = kbId == null ? null : "metadata[\"kb_id\"] == " + kbId;
        log.info("Milvus search request, collection={}, kbId={}, topK={}, filter={}", col, kbId, topK, filter);

        List<BaseVector> vectors = List.of(new FloatVec(toArray(vector)));
        Map<String, Object> params = new HashMap<>();
        params.put("metric_type", "COSINE");
        params.put("ef", 128);

        SearchResp resp = milvusClient.search(SearchReq.builder()
                .collectionName(col)
                .annsField(EMBEDDING_FIELD)
                .data(vectors)
                .topK(topK)
                .filter(filter)
                .searchParams(params)
                .outputFields(List.of(ID_FIELD, CONTENT_FIELD, METADATA_FIELD))
                .build());

        List<List<SearchResp.SearchResult>> results = resp == null ? null : resp.getSearchResults();
        if (results == null || results.isEmpty()) {
            log.info("Milvus search empty, kbId={}, topK={}", kbId, topK);
            return List.of();
        }

        List<SearchResp.SearchResult> hits = results.get(0);
        log.info("Milvus search raw hits size={}, kbId={}, topK={}", hits == null ? 0 : hits.size(), kbId, topK);

        if (hits == null || hits.isEmpty()) {
            return List.of();
        }

        List<KnowledgeChunk> chunks = hits.stream()
                .map(hit -> {
                    Map<String, Object> entity = hit.getEntity();
                    log.info("Milvus search hit score={}, entity={}", hit.getScore(), entity);
                    if (entity == null || entity.isEmpty()) {
                        return null;
                    }

                    KnowledgeChunk chunk = new KnowledgeChunk();
                    Object idObj = entity.get(ID_FIELD);
                    if (idObj != null) {
                        chunk.setId(Long.valueOf(String.valueOf(idObj)));
                    }
                    Object contentObj = entity.get(CONTENT_FIELD);
                    if (contentObj != null) {
                        chunk.setContent(String.valueOf(contentObj));
                    }
                    Object metadataObj = entity.get(METADATA_FIELD);
                    if (metadataObj != null) {
                        JsonObject metadata = metadataObj instanceof JsonObject
                                ? (JsonObject) metadataObj
                                : GSON.fromJson(String.valueOf(metadataObj), JsonObject.class);
                        applyMetadata(chunk, metadata);
                    }
                    return chunk;
                })
                .filter(Objects::nonNull)
                .map(chunk -> {
                    if (chunk.getId() != null) {
                        KnowledgeChunk dbChunk = knowledgeChunkMapper.selectById(chunk.getId());
                        if (dbChunk != null) {
                            return dbChunk;
                        }
                    }
                    return chunk;
                })
                .collect(Collectors.toList());

        log.info("Milvus search parsed chunks size={}, kbId={}", chunks.size(), kbId);
        return chunks;
    }

    @Override
    public void deleteByDocId(Long docId) {
        if (docId == null || milvusClient == null) {
            return;
        }
        ensureCollection();
        String col = resolveCollectionName();
        DeleteResp resp = milvusClient.delete(DeleteReq.builder()
                .collectionName(col)
                .filter("metadata[\"doc_id\"] == " + docId)
                .build());
        log.info("Milvus delete docId={}, deleteCnt={}", docId, resp == null ? null : resp.getDeleteCnt());
    }

    private void createCollectionAndIndex(String col) {
        List<CreateCollectionReq.FieldSchema> fields = new ArrayList<>();
        fields.add(CreateCollectionReq.FieldSchema.builder()
                .name(ID_FIELD)
                .dataType(DataType.VarChar)
                .maxLength(36)
                .isPrimaryKey(true)
                .autoID(false)
                .build());
        fields.add(CreateCollectionReq.FieldSchema.builder()
                .name(CONTENT_FIELD)
                .dataType(DataType.VarChar)
                .maxLength(65535)
                .build());
        fields.add(CreateCollectionReq.FieldSchema.builder()
                .name(METADATA_FIELD)
                .dataType(DataType.JSON)
                .build());
        fields.add(CreateCollectionReq.FieldSchema.builder()
                .name(EMBEDDING_FIELD)
                .dataType(DataType.FloatVector)
                .dimension(dimension)
                .build());

        CreateCollectionReq.CollectionSchema schema = CreateCollectionReq.CollectionSchema.builder()
                .fieldSchemaList(fields)
                .build();

        milvusClient.createCollection(CreateCollectionReq.builder()
                .collectionName(col)
                .collectionSchema(schema)
                .primaryFieldName(ID_FIELD)
                .vectorFieldName(EMBEDDING_FIELD)
                .metricType(IndexParam.MetricType.COSINE.name())
                .consistencyLevel(ConsistencyLevel.BOUNDED)
                .dimension(dimension)
                .build());

        createVectorIndex(col);
        milvusClient.loadCollection(LoadCollectionReq.builder().collectionName(col).build());
        log.info("Milvus collection created and loaded: {}", col);
    }

    private void createVectorIndex(String col) {
        milvusClient.createIndex(CreateIndexReq.builder()
                .collectionName(col)
                .indexParams(List.of(IndexParam.builder()
                        .fieldName(EMBEDDING_FIELD)
                        .indexType(IndexParam.IndexType.HNSW)
                        .metricType(IndexParam.MetricType.COSINE)
                        .indexName(EMBEDDING_FIELD)
                        .extraParams(Map.of(
                                "M", "48",
                                "efConstruction", "200",
                                "mmap.enabled", "false"
                        ))
                        .build()))
                .sync(true)
                .timeout(60000L)
                .build());
        log.info("Milvus vector index ensured: {}", col);
    }

    private List<KnowledgeChunk> searchByChunkId(Long chunkId) {
        if (chunkId == null) {
            return List.of();
        }
        return knowledgeChunkMapper.selectList(new LambdaQueryWrapper<KnowledgeChunk>()
                .eq(KnowledgeChunk::getId, chunkId)
                .last("limit 1"));
    }

    private void applyMetadata(KnowledgeChunk chunk, JsonObject metadata) {
        if (metadata == null) {
            return;
        }
        if (metadata.has("kb_id") && !metadata.get("kb_id").isJsonNull()) {
            chunk.setKbId(metadata.get("kb_id").getAsLong());
        }
        if (metadata.has("doc_id") && !metadata.get("doc_id").isJsonNull()) {
            chunk.setDocId(metadata.get("doc_id").getAsLong());
        }
        if (metadata.has("chunk_index") && !metadata.get("chunk_index").isJsonNull()) {
            chunk.setChunkIndex(metadata.get("chunk_index").getAsInt());
        }
        if (metadata.has("content_hash") && !metadata.get("content_hash").isJsonNull()) {
            chunk.setContentHash(metadata.get("content_hash").getAsString());
        }
        if (metadata.has("char_count") && !metadata.get("char_count").isJsonNull()) {
            chunk.setCharCount(metadata.get("char_count").getAsInt());
        }
        if (metadata.has("token_count") && !metadata.get("token_count").isJsonNull()) {
            chunk.setTokenCount(metadata.get("token_count").getAsInt());
        }
    }

    private float[] toArray(List<Float> vector) {
        float[] arr = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            arr[i] = vector.get(i);
        }
        return arr;
    }

    private JsonArray toJsonArray(List<Float> vector) {
        JsonArray arr = new JsonArray(vector.size());
        for (Float v : vector) {
            arr.add(v);
        }
        return arr;
    }

    private String normalizeMilvusUri(String host, Integer port) {
        String safeHost = host == null || host.isBlank() ? "127.0.0.1" : host.trim();
        String safePort = port == null ? "19530" : String.valueOf(port);
        if (safeHost.startsWith("http://") || safeHost.startsWith("https://")) {
            return safeHost.endsWith("/") ? safeHost.substring(0, safeHost.length() - 1) : safeHost;
        }
        return "http://" + safeHost + ":" + safePort;
    }

    private String resolveCollectionName() {
        return collectionName == null || collectionName.isBlank() ? DEFAULT_COLLECTION_NAME : collectionName.trim();
    }
}
