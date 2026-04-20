package com.linyi.phsm.infrastructure.ai.token;

/**
 * Token 统计服务接口
 */
public interface TokenCounterService {

    /**
     * 统计文本的 Token 数
     *
     * @param text 文本内容
     * @return Token 数（无法计算时返回 null）
     */
    Integer countTokens(String text);
}
