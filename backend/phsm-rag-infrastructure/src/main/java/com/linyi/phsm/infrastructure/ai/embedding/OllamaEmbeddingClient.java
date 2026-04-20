package com.linyi.phsm.infrastructure.ai.embedding;

import cn.hutool.core.collection.CollUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linyi.phsm.infrastructure.ai.AiModelProperties;
import com.linyi.phsm.infrastructure.ai.common.ModelCapability;
import com.linyi.phsm.infrastructure.ai.common.ModelClientErrorType;
import com.linyi.phsm.infrastructure.ai.common.ModelClientException;
import com.linyi.phsm.infrastructure.ai.common.ModelProvider;
import com.linyi.phsm.infrastructure.ai.http.HttpMediaTypes;
import com.linyi.phsm.infrastructure.ai.http.HttpResponseHelper;
import com.linyi.phsm.infrastructure.ai.http.ModelUrlResolver;
import com.linyi.phsm.infrastructure.ai.routing.ModelTarget;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Service
//public class OllamaEmbeddingClient extends AbstractOpenAiStyleEmbeddingClient {
//
//    public OllamaEmbeddingClient(OkHttpClient httpClient) {
//        super(httpClient);
//    }
//
//    @Override
//    public String provider() {
//        return ModelProvider.OLLAMA.getId();
//    }
//
//    @Override
//    protected boolean requiresApiKey() {
//        return false;
//    }
//
//    @Override
//    protected void customizeRequestBody(JsonObject body, ModelTarget target) {
//        // Ollama 不需要 encoding_format 字段
//    }
//}
@Service
public class OllamaEmbeddingClient implements EmbeddingClient {

    private final OkHttpClient httpClient;

    public OllamaEmbeddingClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String provider() {
        return ModelProvider.OLLAMA.getId();
    }

    @Override
    public List<Float> embed(String text, ModelTarget target) {
        List<List<Float>> result = this.embedBatch(List.of(text), target);
        return result.get(0);
    }

    @Override
    public List<List<Float>> embedBatch(List<String> texts, ModelTarget target) {
        if (CollUtil.isEmpty(texts)) {
            return Collections.emptyList();
        }

        AiModelProperties.ProviderConfig provider = HttpResponseHelper.requireProvider(target, provider());
        String url = ModelUrlResolver.resolveUrl(provider, target.candidate(), ModelCapability.EMBEDDING);

        List<List<Float>> results = new ArrayList<>(texts.size());
        for (String text : texts) {
            JsonObject body = new JsonObject();
            body.addProperty("model", HttpResponseHelper.requireModel(target, provider()));
            body.addProperty("prompt", text);

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(body.toString(), HttpMediaTypes.JSON))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errBody = HttpResponseHelper.readBody(response.body());
                    throw new ModelClientException(
                            provider() + " embedding 请求失败: HTTP " + response.code(),
                            ModelClientErrorType.fromHttpStatus(response.code()),
                            response.code()
                    );
                }

                JsonObject json = HttpResponseHelper.parseJson(response.body(), provider());
                JsonArray embedding = json.getAsJsonArray("embedding");

                if (embedding == null || embedding.isEmpty()) {
                    throw new ModelClientException(
                            provider() + " embedding 响应中缺少 embedding 字段",
                            ModelClientErrorType.INVALID_RESPONSE,
                            null
                    );
                }

                List<Float> vector = new ArrayList<>(embedding.size());
                for (JsonElement v : embedding) {
                    vector.add(v.getAsFloat());
                }
                results.add(vector);
            } catch (IOException e) {
                throw new ModelClientException(
                        provider() + " embedding 请求失败: " + e.getMessage(),
                        ModelClientErrorType.NETWORK_ERROR,
                        null,
                        e
                );
            }
        }

        return results;
    }
}
