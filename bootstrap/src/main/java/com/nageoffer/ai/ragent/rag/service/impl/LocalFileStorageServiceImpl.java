

package com.nageoffer.ai.ragent.rag.service.impl;

import cn.hutool.core.lang.Assert;
import com.nageoffer.ai.ragent.rag.dto.StoredFileDTO;
import com.nageoffer.ai.ragent.rag.service.FileStorageService;
import com.nageoffer.ai.ragent.rag.util.FileTypeDetector;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final S3Client s3Client;

    private static final Tika TIKA = new Tika();

    @Override
    @SneakyThrows
    public StoredFileDTO upload(String bucketName, MultipartFile file) {
        Assert.notBlank(bucketName, "bucketName 不能为空");
        Assert.isFalse(file == null || file.isEmpty(), "上传文件不能为空");

        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();

        String detected;
        try (InputStream is = file.getInputStream()) {
            detected = TIKA.detect(is, originalFilename);
        }

        try (InputStream uploadIs = file.getInputStream()) {
            return uploadInternal(bucketName, uploadIs, size, originalFilename, detected);
        }
    }

    @Override
    public StoredFileDTO upload(String bucketName, byte[] content, String originalFilename, String contentType) {
        Assert.notBlank(bucketName, "bucketName 不能为空");
        Assert.notNull(content, "上传内容不能为空");
        String detected = contentType;
        if (detected == null || detected.isBlank()) {
            detected = TIKA.detect(content, originalFilename);
        }
        return uploadInternal(bucketName, new java.io.ByteArrayInputStream(content), content.length, originalFilename, detected);
    }

    @Override
    public InputStream openStream(String url) {
        S3Location loc = parseS3Url(url);
        return s3Client.getObject(b -> b.bucket(loc.bucket()).key(loc.key()));
    }

    @Override
    @SneakyThrows
    public void deleteByUrl(String url) {
        FileSystemUtils.deleteRecursively(Path.of(url));
    }

    private String toS3Url(String bucket, String key) {
        return "s3://" + bucket + "/" + key;
    }

    private S3Location parseS3Url(String url) {
        try {
            URI uri = URI.create(url);
            if (!"s3".equalsIgnoreCase(uri.getScheme())) {
                throw new IllegalArgumentException("Unsupported url scheme: " + url);
            }

            String bucket = uri.getHost();
            String path = uri.getPath(); // /key...
            if (bucket == null || bucket.isBlank()) {
                throw new IllegalArgumentException("Invalid s3 url(bucket missing): " + url);
            }

            String key = (path != null && path.startsWith("/")) ? path.substring(1) : path;
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("Invalid s3 url(key missing): " + url);
            }

            return new S3Location(bucket, key);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid s3 url: " + url, e);
        }
    }

    private record S3Location(String bucket, String key) {
    }

    private StoredFileDTO uploadInternal(String bucketName,
                                         InputStream inputStream,
                                         long size,
                                         String originalFilename,
                                         String detectedContentType) {
        String safeName = originalFilename == null ? "" : originalFilename;
        String suffix = extractSuffix(safeName);

        String s3Key = UUID.randomUUID().toString().replace("-", "")
                + (suffix.isBlank() ? "" : "." + suffix);

        s3Client.putObject(
                b -> b.bucket(bucketName)
                        .key(s3Key)
                        .contentType(detectedContentType)
                        .build(),
                RequestBody.fromInputStream(inputStream, size)
        );

        String url = toS3Url(bucketName, s3Key);
        String detectedType = FileTypeDetector.detectType(originalFilename, detectedContentType);

        return StoredFileDTO.builder()
                .url(url)
                .detectedType(detectedType)
                .size(size)
                .originalFilename(originalFilename)
                .build();
    }

    private String extractSuffix(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            return "";
        }
        return filename.substring(idx + 1).trim();
    }

}
