package com.linyi.pig.service;

import com.linyi.pig.entity.KnowledgeChunk;

import java.util.List;

public interface MilvusVectorService {
    void ensureCollection();

    void upsertChunk(KnowledgeChunk chunk, List<Float> vector);

    List<KnowledgeChunk> searchByKbId(Long kbId, List<Float> vector, int topK);

    void deleteByDocId(Long docId);
}
