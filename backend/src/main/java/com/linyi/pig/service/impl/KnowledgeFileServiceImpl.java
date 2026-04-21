package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.config.MinioComponent;
import com.linyi.pig.entity.KnowledgeBase;
import com.linyi.pig.entity.KnowledgeChunk;
import com.linyi.pig.entity.KnowledgeFile;
import com.linyi.pig.entity.vo.knowledge.KnowledgeChunkView;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileQueryVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileUpdateVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeQaResult;
import com.linyi.pig.exception.LinyiException;
import com.linyi.pig.mapper.KnowledgeBaseMapper;
import com.linyi.pig.mapper.KnowledgeChunkMapper;
import com.linyi.pig.mapper.KnowledgeFileMapper;
import com.linyi.pig.service.KnowledgeFileService;
import com.linyi.pig.service.MilvusVectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeFileServiceImpl extends ServiceImpl<KnowledgeFileMapper, KnowledgeFile>
        implements KnowledgeFileService {

    private final MinioComponent minioComponent;
    private final KnowledgeChunkMapper knowledgeChunkMapper;
    private final KnowledgeBaseMapper knowledgeBaseMapper;
    private final AiModelRouterService aiModelRouterService;
    private final MilvusVectorService milvusVectorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeFile saveToKnowledge(MultipartFile file, Integer createBy, Long kbId) {
        try (InputStream in = file.getInputStream()) {
            String md5 = DigestUtils.md5Hex(in);
            String originalName = file.getOriginalFilename();
            String contentType = file.getContentType();
            long size = file.getSize();

            String lowerName = originalName == null ? "" : originalName.toLowerCase();
            boolean allowedExt = lowerName.endsWith(".txt") || lowerName.endsWith(".md") || lowerName.endsWith(".pdf")
                    || lowerName.endsWith(".doc") || lowerName.endsWith(".docx");
            if (!allowedExt) {
                throw new LinyiException("仅支持上传 txt、md、pdf、word(doc/docx) 文件");
            }

            Long resolvedKbId = resolveKbId(kbId);
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String objectName = "knowledge/" + datePath + "/" + System.currentTimeMillis() + "_"
                    + (originalName == null ? "unknown" : originalName);
            minioComponent.upload(file, objectName);

            KnowledgeFile kf = new KnowledgeFile();
            kf.setKbId(resolvedKbId);
            kf.setFileName(originalName);
            kf.setFilePath(objectName);
            kf.setFileSize(size);
            kf.setFileType(contentType);
            kf.setStatus("pending");
            kf.setProcessMode("chunk");
            kf.setChunkCount(0);
            kf.setCreateBy(createBy == null ? "system" : String.valueOf(createBy));
            kf.setCreateTime(LocalDateTime.now());
            kf.setUpdateTime(LocalDateTime.now());
            save(kf);
            processAndVectorize(file, kf, md5);
            return kf;
        } catch (IOException e) {
            throw new RuntimeException("保存知识库文件失败", e);
        }
    }

    @Override
    public PageResult<KnowledgeFile> page(KnowledgeFileQueryVo vo) {
        LambdaQueryWrapper<KnowledgeFile> qw = new LambdaQueryWrapper<>();
        if (vo.getFileName() != null && !vo.getFileName().isBlank()) {
            qw.like(KnowledgeFile::getFileName, vo.getFileName());
        }
        if (vo.getFilePath() != null && !vo.getFilePath().isBlank()) {
            qw.like(KnowledgeFile::getFilePath, vo.getFilePath());
        }
        if (vo.getFileType() != null && !vo.getFileType().isBlank()) {
            qw.eq(KnowledgeFile::getFileType, vo.getFileType());
        }
        if (vo.getKbId() != null) {
            qw.eq(KnowledgeFile::getKbId, vo.getKbId());
        }
        Page<KnowledgeFile> page = new Page<>(vo.getPageNum(), vo.getPageSize());
        Page<KnowledgeFile> res = this.baseMapper.selectPage(page, qw);
        return new PageResult<>(res.getRecords(), res.getTotal(), res.getPages(), vo.getPageNum(), vo.getPageSize());
    }

    @Override
    public boolean updateRemark(KnowledgeFileUpdateVo vo) {
        KnowledgeFile db = getById(vo.getId());
        if (db == null)
            return false;
        db.setStatus(vo.getStatus());
        db.setUpdateTime(LocalDateTime.now());
        return updateById(db);
    }

    private boolean deletePhysicalFile(String objectName) {
        try {
            // 这里保留数据库删除主流程，MinIO 物理删除可根据业务再补充
            log.info("skip delete minio object: {}", objectName);
            return true;
        } catch (Exception e) {
            log.warn("删除物理文件失败: {}", objectName, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithFileById(Long id) {
        KnowledgeFile kf = getById(id);
        if (kf == null)
            return false;
        cleanupVectorsAndChunks(List.of(kf));
        boolean dbOk = removeById(id);
        deletePhysicalFile(kf.getFilePath());
        return dbOk;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithFileByIds(java.util.List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return true;
        java.util.List<KnowledgeFile> list = listByIds(ids);
        cleanupVectorsAndChunks(list);
        boolean dbOk = removeByIds(ids);
        for (KnowledgeFile kf : list) {
            if (kf != null)
                deletePhysicalFile(kf.getFilePath());
        }
        return dbOk;
    }

    private Long resolveKbId(Long kbId) {
        if (kbId != null) {
            return kbId;
        }
        KnowledgeBase kb = knowledgeBaseMapper.selectOne(
                new QueryWrapper<KnowledgeBase>().lambda()
                        .eq(KnowledgeBase::getName, "默认知识库")
                        .last("limit 1"));
        if (kb != null) {
            return kb.getId();
        }
        KnowledgeBase createKb = new KnowledgeBase();
        createKb.setName("默认知识库");
        createKb.setEmbeddingModel("default");
        createKb.setCollectionName("rag_default_store");
        createKb.setCreatedBy("system");
        createKb.setUpdatedBy("system");
        createKb.setCreateTime(LocalDateTime.now());
        createKb.setUpdateTime(LocalDateTime.now());
        knowledgeBaseMapper.insert(createKb);
        return createKb.getId();
    }

    private void cleanupVectorsAndChunks(List<KnowledgeFile> files) {
        List<Long> docIds = files.stream().filter(java.util.Objects::nonNull).map(KnowledgeFile::getId).toList();
        if (docIds.isEmpty()) {
            return;
        }
        knowledgeChunkMapper.delete(new QueryWrapper<KnowledgeChunk>().lambda().in(KnowledgeChunk::getDocId, docIds));
        for (Long docId : docIds) {
            milvusVectorService.deleteByDocId(docId);
        }
    }

    @Override
    public KnowledgeQaResult qaByKbId(Long kbId, String question, int topK) {
        KnowledgeQaResult result = new KnowledgeQaResult();
        if (question == null || question.isBlank()) {
            result.setQuestion(question);
            result.setChunks(List.of());
            result.setAnswer("请先提供问题。");
            return result;
        }
        Long resolvedKbId = kbId == null ? resolveKbId(null) : kbId;
        result.setQuestion(question);
        List<KnowledgeChunk> chunks = milvusVectorService.searchByKbId(resolvedKbId, aiModelRouterService.embed(question), topK);
        List<KnowledgeChunkView> views = chunks.stream().map(this::toChunkView).toList();
        result.setChunks(views);
        if (views.isEmpty()) {
            result.setAnswer("未检索到相关知识片段。");
            return result;
        }
        StringBuilder context = new StringBuilder();
        for (KnowledgeChunk chunk : chunks) {
            context.append("\n---\n").append(chunk.getContent());
        }
        String prompt = "你是猪病知识助手。请基于下面检索到的知识片段，简洁回答用户问题；如果片段不足，直接说明不确定。\n知识片段:" + context + "\n问题:" + question;
        String answer = aiModelRouterService.chat(prompt);
        result.setAnswer(answer == null || answer.isBlank() ? "暂无可用回答。" : answer.trim());
        return result;
    }

    private KnowledgeChunkView toChunkView(KnowledgeChunk chunk) {
        KnowledgeChunkView view = new KnowledgeChunkView();
        view.setId(chunk.getId());
        view.setKbId(chunk.getKbId());
        view.setDocId(chunk.getDocId());
        view.setChunkIndex(chunk.getChunkIndex());
        view.setContent(chunk.getContent());
        KnowledgeFile file = chunk.getDocId() == null ? null : getById(chunk.getDocId());
        view.setFileName(file == null ? null : file.getFileName());
        return view;
    }

    private String buildAnswerTemplate(String question, List<KnowledgeChunkView> chunks, String answer) {
        StringBuilder sb = new StringBuilder();
        sb.append("【问题】").append(question == null ? "" : question).append('\n');
        sb.append("【结论】").append(answer == null || answer.isBlank() ? "暂无可用回答" : answer).append('\n');
        sb.append("【依据片段】");
        if (chunks == null || chunks.isEmpty()) {
            sb.append("无");
            return sb.toString();
        }
        sb.append('\n');
        for (KnowledgeChunkView chunk : chunks) {
            sb.append("- [docId=").append(chunk.getDocId())
                    .append(", chunkIndex=").append(chunk.getChunkIndex())
                    .append(", fileName=").append(chunk.getFileName())
                    .append("] ")
                    .append(chunk.getContent())
                    .append('\n');
        }
        return sb.toString().trim();
    }

    private void processAndVectorize(MultipartFile file, KnowledgeFile doc, String contentHash) throws IOException {
        String text = extractText(file);
        if (text == null || text.isBlank()) {
            doc.setStatus("failed");
            updateById(doc);
            return;
        }
        List<Document> docs = new ArrayList<>();
        docs.add(new Document(text));
        var chunks = new TokenTextSplitter().apply(docs);
        int index = 0;
        List<KnowledgeChunk> savedChunks = new ArrayList<>();
        for (Document chunkDoc : chunks) {
            String chunkText = chunkDoc.getContent();
            KnowledgeChunk chunk = new KnowledgeChunk();
            chunk.setKbId(doc.getKbId());
            chunk.setDocId(doc.getId());
            chunk.setChunkIndex(index);
            chunk.setContent(chunkText);
            chunk.setContentHash(contentHash);
            chunk.setCharCount(chunkText == null ? 0 : chunkText.length());
            chunk.setCreatedBy(doc.getCreateBy());
            chunk.setUpdatedBy(doc.getCreateBy());
            chunk.setCreateTime(LocalDateTime.now());
            chunk.setUpdateTime(LocalDateTime.now());
            knowledgeChunkMapper.insert(chunk);
            milvusVectorService.upsertChunk(chunk, aiModelRouterService.embed(chunkText));
            savedChunks.add(chunk);
            index++;
        }
        doc.setChunkCount(savedChunks.size());
        doc.setStatus("ready");
        doc.setUpdateTime(LocalDateTime.now());
        updateById(doc);
    }

    private String extractText(MultipartFile file) throws IOException {
        String lowerName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (lowerName.endsWith(".txt") || lowerName.endsWith(".md")) {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        }
        if (lowerName.endsWith(".pdf")) {
            try (PDDocument pdf = PDDocument.load(new ByteArrayInputStream(file.getBytes()))) {
                return new PDFTextStripper().getText(pdf);
            }
        }
        if (lowerName.endsWith(".docx")) {
            try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(file.getBytes()));
                    XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
                return extractor.getText();
            }
        }
        return null;
    }
}
