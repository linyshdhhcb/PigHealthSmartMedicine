

package com.nageoffer.ai.ragent.infra.http;

import com.nageoffer.ai.ragent.infra.config.AIModelProperties;
import com.nageoffer.ai.ragent.infra.enums.ModelCapability;

import java.util.Map;

/**
 * 模型URL解析器
 * 用于解析AI模型的完整URL地址，支持从候选模型配置或提供商配置中获取URL
 */
public class ModelUrlResolver {

    /**
     * 私有构造函数，防止实例化工具类
     */
    private ModelUrlResolver() {
    }

    /**
     * 解析模型URL地址
     * 优先级：候选模型URL > 提供商基础URL + 端点路径
     *
     * @param provider   提供商配置，包含基础URL和端点配置
     * @param candidate  候选模型配置，可能包含自定义URL
     * @param capability 模型能力类型，用于确定使用哪个端点
     * @return 完整的模型URL地址
     * @throws IllegalStateException 当提供商基础URL缺失或端点配置缺失时抛出
     */
    public static String resolveUrl(
            AIModelProperties.ProviderConfig provider,
            AIModelProperties.ModelCandidate candidate,
            ModelCapability capability) {
        if (candidate != null && candidate.getUrl() != null && !candidate.getUrl().isBlank()) {
            return candidate.getUrl();
        }
        if (provider == null || provider.getUrl() == null || provider.getUrl().isBlank()) {
            throw new IllegalStateException("Provider baseUrl is missing");
        }

        Map<String, String> endpoints = provider.getEndpoints();
        String key = capability.name().toLowerCase();
        String path = endpoints == null ? null : endpoints.get(key);
        if (path == null || path.isBlank()) {
            throw new IllegalStateException("Provider endpoint is missing: " + key);
        }

        return joinUrl(provider.getUrl(), path);
    }

    /**
     * 拼接基础URL和路径
     * 智能处理URL和路径之间的斜杠，确保拼接结果正确
     *
     * @param baseUrl 基础URL
     * @param path    路径
     * @return 拼接后的完整URL
     */
    private static String joinUrl(String baseUrl, String path) {
        if (baseUrl.endsWith("/") && path.startsWith("/")) {
            return baseUrl + path.substring(1);
        }
        if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
            return baseUrl + "/" + path;
        }
        return baseUrl + path;
    }
}
