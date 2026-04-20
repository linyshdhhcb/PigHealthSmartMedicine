package com.linyi.phsm.application.ingestion.fetcher;

import com.linyi.phsm.framework.exception.ServiceException;
import com.linyi.phsm.domain.ingestion.model.context.DocumentSource;
import com.linyi.phsm.domain.ingestion.model.enums.SourceType;
import com.linyi.phsm.application.ingestion.util.MimeTypeDetector;
import com.linyi.phsm.application.rag.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStream;

/**
 * S3对象存储文档提取器
 * 支持从S3兼容的对象存储（如RustFS）中获取文档，示例路径：s3://biz/5fb28010e16c4083ab07ca41f29804b0.md
 */
@Component
@RequiredArgsConstructor
public class S3Fetcher implements DocumentFetcher {

    private final FileStorageService fileStorageService;

    @Override
    public SourceType supportedType() {
        return SourceType.S3;
    }

    @Override
    public FetchResult fetch(DocumentSource source) {
        String location = source.getLocation();
        if (!StringUtils.hasText(location)) {
            throw new ServiceException("S3路径不能为空");
        }

        if (!location.startsWith("s3://")) {
            throw new ServiceException("无效的S3路径格式，应以 s3:// 开头: " + location);
        }

        try {
            byte[] bytes;
            try (InputStream is = fileStorageService.openStream(location)) {
                bytes = is.readAllBytes();
            }

            String fileName = source.getFileName();
            if (!StringUtils.hasText(fileName)) {
                fileName = extractFileName(location);
            }

            String mimeType = MimeTypeDetector.detect(bytes, fileName);
            return new FetchResult(bytes, mimeType, fileName);
        } catch (Exception e) {
            throw new ServiceException("从S3读取文件失败: " + location + ", 错误: " + e.getMessage());
        }
    }

    /**
     * 从S3路径中提取文件名
     * 例如：s3://biz/5fb28010e16c4083ab07ca41f29804b0.md -> 5fb28010e16c4083ab07ca41f29804b0.md
     */
    private String extractFileName(String location) {
        int idx = location.lastIndexOf('/');
        return idx >= 0 ? location.substring(idx + 1) : location;
    }
}
