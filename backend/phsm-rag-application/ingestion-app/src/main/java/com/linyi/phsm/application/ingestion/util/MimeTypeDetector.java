package com.linyi.phsm.application.ingestion.util;

import org.apache.tika.Tika;

/**
 * MimeType 探测器，用于识别文件或字节数组的媒体类型
 */
public final class MimeTypeDetector {

    private static final Tika TIKA = new Tika();

    private MimeTypeDetector() {
    }

    public static String detect(byte[] bytes, String fileName) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        if (fileName == null) {
            return TIKA.detect(bytes);
        }
        return TIKA.detect(bytes, fileName);
    }
}
