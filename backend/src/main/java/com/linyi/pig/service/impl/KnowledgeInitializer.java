package com.linyi.pig.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

@Slf4j
@Component
@RequiredArgsConstructor
class KnowledgeInitializer {

    private final VectorStore vectorStore;

    @Value("classpath:knowledge/*")
    private Resource[] knowledgeFiles;

    @PostConstruct
    void init() {
        try {
            List<Document> documents = new ArrayList<>();
            if (knowledgeFiles == null || knowledgeFiles.length == 0) {
                // 兼容某些环境下的资源解析
                knowledgeFiles = new PathMatchingResourcePatternResolver().getResources("classpath*:knowledge/*");
            }
            for (Resource resource : knowledgeFiles) {
                if (!resource.exists()) {
                    continue;
                }
                String filename = resource.getFilename();
                if (filename == null) {
                    continue;
                }
                String lower = filename.toLowerCase();
                if (lower.endsWith(".txt") || lower.endsWith(".md")) {
                    TextReader reader = new TextReader(resource);
                    reader.getCustomMetadata().put("filename", filename);
                    reader.setCharset(Charset.defaultCharset());
                    documents.addAll(reader.get());
                } else if (lower.endsWith(".pdf")) {
                    try (InputStream is = resource.getInputStream(); PDDocument pdf = PDDocument.load(is)) {
                        PDFTextStripper stripper = new PDFTextStripper();
                        String text = stripper.getText(pdf);
                        if (text != null && !text.isBlank()) {
                            documents.add(new Document(text));
                        }
                    }
                } else if (lower.endsWith(".docx")) {
                    try (InputStream is = resource.getInputStream(); XWPFDocument doc = new XWPFDocument(is)) {
                        try (XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
                            String text = extractor.getText();
                            if (text != null && !text.isBlank()) {
                                documents.add(new Document(text));
                            }
                        }
                    }
                } else {
                    log.warn("暂不支持的知识库文件类型: {}", filename);
                }
            }

            if (!documents.isEmpty()) {
                TokenTextSplitter splitter = new TokenTextSplitter();
                var chunks = splitter.apply(documents);
                vectorStore.add(chunks);
                log.info("知识库向量化完成，原始文档: {}，切分后块数: {}", documents.size(), chunks.size());
            } else {
                log.warn("未发现可加载的知识库文档");
            }
        } catch (Exception e) {
            log.error("知识库初始化失败", e);
        }
    }
}
