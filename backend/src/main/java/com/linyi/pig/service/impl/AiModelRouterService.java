package com.linyi.pig.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.linyi.pig.config.AiProviderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiModelRouterService {
    private final RestTemplate restTemplate;
    private final AiProviderProperties properties;

    public String chat(String prompt) {
        String providerName = properties.getSelection().getChatProvider();
        AiProviderProperties.Provider provider = resolveProvider(providerName);
        String model = modelOrDefault(properties.getChat().getDefaultModel(), "qwen-plus-latest");
        String url = trimSlash(provider.getUrl()) + provider.getEndpoints().getChat();
        HttpHeaders headers = buildHeaders(providerName, provider.getApiKey());
        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        body.put("temperature", 0.4);
        JSONObject response = restTemplate.postForObject(url, new HttpEntity<>(body, headers), JSONObject.class);
        if (response == null) {
            return "";
        }
        JSONArray choices = response.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            return "";
        }
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        return message == null ? "" : message.getString("content");
    }

    public List<Float> embed(String text) {
        String providerName = properties.getSelection().getEmbeddingProvider();
        AiProviderProperties.Provider provider = resolveProvider(providerName);
        String model = modelOrDefault(properties.getEmbedding().getDefaultModel(), "Qwen/Qwen3-Embedding-8B");
        String url = trimSlash(provider.getUrl()) + provider.getEndpoints().getEmbedding();
        HttpHeaders headers = buildHeaders(providerName, provider.getApiKey());
        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("input", text);
        JSONObject response = restTemplate.postForObject(url, new HttpEntity<>(body, headers), JSONObject.class);
        if (response == null) {
            return List.of();
        }
        JSONArray data = response.getJSONArray("data");
        if (data == null || data.isEmpty()) {
            return List.of();
        }
        JSONObject vectorObj = data.getJSONObject(0);
        JSONArray embedding = vectorObj == null ? null : vectorObj.getJSONArray("embedding");
        if (embedding == null) {
            return List.of();
        }
        List<Float> vector = new ArrayList<>(embedding.size());
        for (int i = 0; i < embedding.size(); i++) {
            vector.add(embedding.getFloat(i));
        }
        return vector;
    }

    private AiProviderProperties.Provider resolveProvider(String provider) {
        if (provider == null) {
            return properties.getProviders().getOllama();
        }
        return switch (provider.toLowerCase()) {
            case "bailian" -> properties.getProviders().getBailian();
            case "siliconflow" -> properties.getProviders().getSiliconflow();
            default -> properties.getProviders().getOllama();
        };
    }

    private HttpHeaders buildHeaders(String provider, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!"ollama".equalsIgnoreCase(provider) && apiKey != null && !apiKey.isBlank()) {
            headers.setBearerAuth(apiKey);
        }
        return headers;
    }

    private String trimSlash(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public String getEmbeddingProvider() {
        return properties.getSelection().getEmbeddingProvider();
    }

    public String getEmbeddingModel() {
        return modelOrDefault(properties.getEmbedding().getDefaultModel(), "Qwen/Qwen3-Embedding-8B");
    }

    private String modelOrDefault(String configured, String fallback) {
        return (configured == null || configured.isBlank()) ? fallback : configured;
    }
}
