package com.linyi.phsm.application.ingestion.fetcher;

/**
 * 抓取结果实体类
 *
 * @param content  抓取到的内容字节数组
 * @param mimeType 内容的 MIME 类型
 * @param fileName 文件名称
 */
public record FetchResult(byte[] content, String mimeType, String fileName) {
}
