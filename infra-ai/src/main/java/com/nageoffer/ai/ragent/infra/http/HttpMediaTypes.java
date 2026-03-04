

package com.nageoffer.ai.ragent.infra.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;

/**
 * HTTP 媒体类型常量类
 * 提供常用的 HTTP Content-Type 媒体类型定义
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpMediaTypes {

    /**
     * JSON 媒体类型，使用 UTF-8 字符集
     * 用于 OkHttp 请求中的 MediaType 对象
     */
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * JSON 媒体类型字符串，使用 UTF-8 字符集
     * 用于 HTTP 请求头的 Content-Type 值
     */
    public static final String JSON_UTF8_HEADER = "application/json; charset=UTF-8";
}
