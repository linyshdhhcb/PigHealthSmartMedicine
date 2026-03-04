

package com.nageoffer.ai.ragent.core.parser;

import com.nageoffer.ai.ragent.framework.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Apache Tika 文档解析器
 * <p>
 * 支持多种文档格式：PDF、Word、Excel、PPT、HTML、XML 等
 * 使用 Apache Tika 库进行文档解析和文本提取
 */
@Slf4j
@Component
public class TikaDocumentParser implements DocumentParser {

    private static final Tika TIKA = new Tika();

    @Override
    public String getParserType() {
        return ParserType.TIKA.getType();
    }

    @Override
    public ParseResult parse(byte[] content, String mimeType, Map<String, Object> options) {
        if (content == null || content.length == 0) {
            return ParseResult.ofText("");
        }

        try (ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            String text = TIKA.parseToString(is);
            String cleaned = TextCleanupUtil.cleanup(text);
            return ParseResult.ofText(cleaned);
        } catch (Exception e) {
            log.error("Tika 解析失败，MIME 类型: {}", mimeType, e);
            throw new ServiceException("文档解析失败: " + e.getMessage());
        }
    }

    @Override
    public String extractText(InputStream stream, String fileName) {
        try {
            String text = TIKA.parseToString(stream);
            return TextCleanupUtil.cleanup(text);
        } catch (Exception e) {
            log.error("从文件中提取文本内容失败: {}", fileName, e);
            throw new ServiceException("解析文件失败: " + fileName);
        }
    }

    @Override
    public boolean supports(String mimeType) {
        // Tika 支持大部分常见文档格式
        return mimeType != null && !mimeType.startsWith("text/markdown");
    }
}
