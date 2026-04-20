/*
 * 
 * Could not load the following classes:
 *  cn.hutool.core.collection.CollUtil
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  lombok.Generated
 *  okhttp3.MediaType
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.linyi.phsm.infrastructure.ai.embedding;

import cn.hutool.core.collection.CollUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linyi.phsm.infrastructure.ai.AiModelProperties;
import com.linyi.phsm.infrastructure.ai.embedding.EmbeddingClient;
import com.linyi.phsm.infrastructure.ai.common.ModelCapability;
import com.linyi.phsm.infrastructure.ai.http.HttpMediaTypes;
import com.linyi.phsm.infrastructure.ai.http.HttpResponseHelper;
import com.linyi.phsm.infrastructure.ai.common.ModelClientErrorType;
import com.linyi.phsm.infrastructure.ai.common.ModelClientException;
import com.linyi.phsm.infrastructure.ai.http.ModelUrlResolver;
import com.linyi.phsm.infrastructure.ai.routing.ModelTarget;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOpenAiStyleEmbeddingClient
implements EmbeddingClient {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(AbstractOpenAiStyleEmbeddingClient.class);
    protected final OkHttpClient httpClient;

    protected AbstractOpenAiStyleEmbeddingClient(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    protected boolean requiresApiKey() {
        return true;
    }

    protected void customizeRequestBody(JsonObject body, ModelTarget target) {
        body.addProperty("encoding_format", "float");
    }

    protected int maxBatchSize() {
        return 0;
    }

    @Override
    public List<Float> embed(String text, ModelTarget target) {
        List<List<Float>> result = this.doEmbed(List.of(text), target);
        return result.get(0);
    }

    @Override
    public List<List<Float>> embedBatch(List<String> texts, ModelTarget target) {
        if (CollUtil.isEmpty(texts)) {
            return Collections.emptyList();
        }
        int batch = this.maxBatchSize();
        if (batch <= 0 || texts.size() <= batch) {
            return this.doEmbed(texts, target);
        }
        ArrayList<List<Float>> results = new ArrayList<>(Collections.nCopies(texts.size(), null));
        int n = texts.size();
        for (int i = 0; i < n; i += batch) {
            int end = Math.min(i + batch, n);
            List<String> slice = texts.subList(i, end);
            List<List<Float>> part = this.doEmbed(slice, target);
            for (int k = 0; k < part.size(); ++k) {
                results.set(i + k, part.get(k));
            }
        }
        return results;
    }

    protected List<List<Float>> doEmbed(List<String> texts, ModelTarget target) {
        JsonObject json;
        AiModelProperties.ProviderConfig provider = HttpResponseHelper.requireProvider(target, this.provider());
        if (this.requiresApiKey()) {
            HttpResponseHelper.requireApiKey(provider, this.provider());
        }
        String url = ModelUrlResolver.resolveUrl(provider, target.candidate(), ModelCapability.EMBEDDING);
        JsonObject body = new JsonObject();
        body.addProperty("model", HttpResponseHelper.requireModel(target, this.provider()));
        JsonArray inputArray = new JsonArray();
        for (String text : texts) {
            inputArray.add(text);
        }
        body.add("input", (JsonElement)inputArray);
        body.addProperty("dimensions", (Number)target.candidate().getDimension());
        this.customizeRequestBody(body, target);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(RequestBody.create((String)body.toString(), (MediaType)HttpMediaTypes.JSON));
        if (this.requiresApiKey()) {
            requestBuilder.addHeader("Authorization", "Bearer " + provider.getApiKey());
        }
        Request request = requestBuilder.build();
        try (Response response = this.httpClient.newCall(request).execute();){
            if (!response.isSuccessful()) {
                String errBody = HttpResponseHelper.readBody(response.body());
                log.warn("{} embedding \u8bf7\u6c42\u5931\u8d25: status={}, body={}", new Object[]{this.provider(), response.code(), errBody});
                throw new ModelClientException(this.provider() + " embedding \u8bf7\u6c42\u5931\u8d25: HTTP " + response.code(), ModelClientErrorType.fromHttpStatus(response.code()), response.code());
            }
            json = HttpResponseHelper.parseJson(response.body(), this.provider());
        }
        catch (IOException e) {
            throw new ModelClientException(this.provider() + " embedding \u8bf7\u6c42\u5931\u8d25: " + e.getMessage(), ModelClientErrorType.NETWORK_ERROR, null, e);
        }
        if (json.has("error")) {
            JsonObject err = json.getAsJsonObject("error");
            String code = err.has("code") ? err.get("code").getAsString() : "unknown";
            String msg = err.has("message") ? err.get("message").getAsString() : "unknown";
            throw new ModelClientException(this.provider() + " embedding \u9519\u8bef: " + code + " - " + msg, ModelClientErrorType.PROVIDER_ERROR, null);
        }
        JsonArray data = json.getAsJsonArray("data");
        if (data == null || data.isEmpty()) {
            throw new ModelClientException(this.provider() + " embedding \u54cd\u5e94\u4e2d\u7f3a\u5c11 data \u6570\u7ec4", ModelClientErrorType.INVALID_RESPONSE, null);
        }
        ArrayList<List<Float>> results = new ArrayList<List<Float>>(data.size());
        for (JsonElement el : data) {
            JsonObject obj = el.getAsJsonObject();
            JsonArray emb = obj.getAsJsonArray("embedding");
            if (emb == null || emb.isEmpty()) {
                throw new ModelClientException(this.provider() + " embedding \u54cd\u5e94\u4e2d\u7f3a\u5c11 embedding \u5b57\u6bb5", ModelClientErrorType.INVALID_RESPONSE, null);
            }
            ArrayList<Float> vector = new ArrayList<Float>(emb.size());
            for (JsonElement v : emb) {
                vector.add(Float.valueOf(v.getAsFloat()));
            }
            results.add(vector);
        }
        return results;
    }
}
