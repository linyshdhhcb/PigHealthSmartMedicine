/*
 * 
 * Could not load the following classes:
 *  cn.hutool.core.collection.CollUtil
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.linyi.phsm.framework.model.ChatMessage
 *  com.linyi.phsm.framework.model.ChatMessage$Role
 *  com.linyi.phsm.framework.model.ChatRequest
 *  lombok.Generated
 *  okhttp3.Call
 *  okhttp3.MediaType
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  okhttp3.ResponseBody
 *  okio.BufferedSource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.linyi.phsm.infrastructure.ai.chat;

import cn.hutool.core.collection.CollUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linyi.phsm.framework.model.ChatMessage;
import com.linyi.phsm.framework.model.ChatRequest;
import com.linyi.phsm.infrastructure.ai.chat.ChatClient;
import com.linyi.phsm.infrastructure.ai.chat.OpenAiStyleSseParser;
import com.linyi.phsm.infrastructure.ai.stream.StreamAsyncExecutor;
import com.linyi.phsm.infrastructure.ai.stream.StreamCallback;
import com.linyi.phsm.infrastructure.ai.stream.StreamCancellationHandle;
import com.linyi.phsm.infrastructure.ai.AiModelProperties;
import com.linyi.phsm.infrastructure.ai.common.ModelCapability;
import com.linyi.phsm.infrastructure.ai.http.HttpMediaTypes;
import com.linyi.phsm.infrastructure.ai.http.HttpResponseHelper;
import com.linyi.phsm.infrastructure.ai.common.ModelClientErrorType;
import com.linyi.phsm.infrastructure.ai.common.ModelClientException;
import com.linyi.phsm.infrastructure.ai.http.ModelUrlResolver;
import com.linyi.phsm.infrastructure.ai.routing.ModelTarget;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOpenAiStyleChatClient
implements ChatClient {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(AbstractOpenAiStyleChatClient.class);
    protected final OkHttpClient httpClient;
    protected final Executor modelStreamExecutor;
    protected final Gson gson = new Gson();

    protected AbstractOpenAiStyleChatClient(OkHttpClient httpClient, Executor modelStreamExecutor) {
        super();
        this.httpClient = httpClient;
        this.modelStreamExecutor = modelStreamExecutor;
    }

    protected boolean isReasoningEnabledForStream(ChatRequest request) {
        return Boolean.TRUE.equals(request.getThinking());
    }

    protected void customizeRequestBody(JsonObject body, ChatRequest request) {
        if (Boolean.TRUE.equals(request.getThinking())) {
            body.addProperty("enable_thinking", Boolean.valueOf(true));
        }
    }

    protected boolean requiresApiKey() {
        return true;
    }

    protected String doChat(ChatRequest request, ModelTarget target) {
        JsonObject respJson;
        AiModelProperties.ProviderConfig provider = HttpResponseHelper.requireProvider(target, this.provider());
        if (this.requiresApiKey()) {
            HttpResponseHelper.requireApiKey(provider, this.provider());
        }
        JsonObject reqBody = this.buildRequestBody(request, target, false);
        Request requestHttp = this.newAuthorizedRequest(provider, target).post(RequestBody.create((String)reqBody.toString(), (MediaType)HttpMediaTypes.JSON)).build();
        try (Response response = this.httpClient.newCall(requestHttp).execute();){
            if (!response.isSuccessful()) {
                String body = HttpResponseHelper.readBody(response.body());
                log.warn("{} \u540c\u6b65\u8bf7\u6c42\u5931\u8d25: status={}, body={}", new Object[]{this.provider(), response.code(), body});
                throw new ModelClientException(this.provider() + " \u540c\u6b65\u8bf7\u6c42\u5931\u8d25: HTTP " + response.code(), ModelClientErrorType.fromHttpStatus(response.code()), response.code());
            }
            respJson = HttpResponseHelper.parseJson(response.body(), this.provider());
        }
        catch (IOException e) {
            throw new ModelClientException(this.provider() + " \u540c\u6b65\u8bf7\u6c42\u5931\u8d25: " + e.getMessage(), ModelClientErrorType.NETWORK_ERROR, null, e);
        }
        return this.extractChatContent(respJson);
    }

    protected StreamCancellationHandle doStreamChat(ChatRequest request, StreamCallback callback, ModelTarget target) {
        AiModelProperties.ProviderConfig provider = HttpResponseHelper.requireProvider(target, this.provider());
        if (this.requiresApiKey()) {
            HttpResponseHelper.requireApiKey(provider, this.provider());
        }
        JsonObject reqBody = this.buildRequestBody(request, target, true);
        Request streamRequest = this.newAuthorizedRequest(provider, target).post(RequestBody.create((String)reqBody.toString(), (MediaType)HttpMediaTypes.JSON)).addHeader("Accept", "text/event-stream").build();
        Call call = this.httpClient.newCall(streamRequest);
        boolean reasoningEnabled = this.isReasoningEnabledForStream(request);
        return StreamAsyncExecutor.submit(this.modelStreamExecutor, call, callback, cancelled -> this.doStream(call, callback, (AtomicBoolean)cancelled, reasoningEnabled));
    }

    private void doStream(Call call, StreamCallback callback, AtomicBoolean cancelled, boolean reasoningEnabled) {
        try (Response response = call.execute();){
            String line;
            if (!response.isSuccessful()) {
                String body = HttpResponseHelper.readBody(response.body());
                throw new ModelClientException(this.provider() + " \u6d41\u5f0f\u8bf7\u6c42\u5931\u8d25: HTTP " + response.code() + " - " + body, ModelClientErrorType.fromHttpStatus(response.code()), response.code());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new ModelClientException(this.provider() + " \u6d41\u5f0f\u54cd\u5e94\u4e3a\u7a7a", ModelClientErrorType.INVALID_RESPONSE, null);
            }
            BufferedSource source = body.source();
            boolean completed = false;
            while (!cancelled.get() && (line = source.readUtf8Line()) != null) {
                if (line.isBlank()) continue;
                try {
                    OpenAiStyleSseParser.ParsedEvent event = OpenAiStyleSseParser.parseLine(line, this.gson, reasoningEnabled);
                    if (event.hasReasoning()) {
                        callback.onThinking(event.reasoning());
                    }
                    if (event.hasContent()) {
                        callback.onContent(event.content());
                    }
                    if (!event.completed()) continue;
                    callback.onComplete();
                    completed = true;
                    break;
                }
                catch (Exception parseEx) {
                    log.warn("{} \u6d41\u5f0f\u54cd\u5e94\u89e3\u6790\u5931\u8d25: line={}", new Object[]{this.provider(), line, parseEx});
                }
            }
            if (cancelled.get()) {
                log.info("{} \u6d41\u5f0f\u54cd\u5e94\u5df2\u88ab\u53d6\u6d88", (Object)this.provider());
                return;
            }
            if (!completed) {
                throw new ModelClientException(this.provider() + " \u6d41\u5f0f\u54cd\u5e94\u5f02\u5e38\u7ed3\u675f", ModelClientErrorType.INVALID_RESPONSE, null);
            }
        }
        catch (Exception e) {
            if (!cancelled.get()) {
                callback.onError(e);
            }
            log.info("{} \u6d41\u5f0f\u54cd\u5e94\u53d6\u6d88\u671f\u95f4\u4ea7\u751f\u5f02\u5e38\uff08\u53ef\u5ffd\u7565\uff09: {}", (Object)this.provider(), (Object)e.getMessage());
        }
    }

    protected JsonObject buildRequestBody(ChatRequest request, ModelTarget target, boolean stream) {
        JsonObject body = new JsonObject();
        body.addProperty("model", HttpResponseHelper.requireModel(target, this.provider()));
        if (stream) {
            body.addProperty("stream", Boolean.valueOf(true));
        }
        body.add("messages", (JsonElement)this.buildMessages(request));
        if (request.getTemperature() != null) {
            body.addProperty("temperature", (Number)request.getTemperature());
        }
        if (request.getTopP() != null) {
            body.addProperty("top_p", (Number)request.getTopP());
        }
        if (request.getTopK() != null) {
            body.addProperty("top_k", (Number)request.getTopK());
        }
        if (request.getMaxTokens() != null) {
            body.addProperty("max_tokens", (Number)request.getMaxTokens());
        }
        this.customizeRequestBody(body, request);
        return body;
    }

    private JsonArray buildMessages(ChatRequest request) {
        JsonArray arr = new JsonArray();
        List<ChatMessage> messages = request.getMessages();
        if (CollUtil.isNotEmpty(messages)) {
            for (ChatMessage m : messages) {
                JsonObject msg = new JsonObject();
                msg.addProperty("role", this.toOpenAiRole(m.getRole()));
                msg.addProperty("content", m.getContent());
                arr.add((JsonElement)msg);
            }
        }
        return arr;
    }

    private String toOpenAiRole(ChatMessage.Role role) {
        return switch (role) {
            case SYSTEM -> "system";
            case USER -> "user";
            case ASSISTANT -> "assistant";
        };
    }

    private Request.Builder newAuthorizedRequest(AiModelProperties.ProviderConfig provider, ModelTarget target) {
        Request.Builder builder = new Request.Builder().url(ModelUrlResolver.resolveUrl(provider, target.candidate(), ModelCapability.CHAT));
        if (this.requiresApiKey()) {
            builder.addHeader("Authorization", "Bearer " + provider.getApiKey());
        }
        return builder;
    }

    private String extractChatContent(JsonObject respJson) {
        if (respJson == null || !respJson.has("choices")) {
            throw new ModelClientException(this.provider() + " \u54cd\u5e94\u7f3a\u5c11 choices", ModelClientErrorType.INVALID_RESPONSE, null);
        }
        JsonArray choices = respJson.getAsJsonArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new ModelClientException(this.provider() + " \u54cd\u5e94 choices \u4e3a\u7a7a", ModelClientErrorType.INVALID_RESPONSE, null);
        }
        JsonObject choice0 = choices.get(0).getAsJsonObject();
        if (choice0 == null || !choice0.has("message")) {
            throw new ModelClientException(this.provider() + " \u54cd\u5e94\u7f3a\u5c11 message", ModelClientErrorType.INVALID_RESPONSE, null);
        }
        JsonObject message = choice0.getAsJsonObject("message");
        if (message == null || !message.has("content") || message.get("content").isJsonNull()) {
            throw new ModelClientException(this.provider() + " \u54cd\u5e94\u7f3a\u5c11 content", ModelClientErrorType.INVALID_RESPONSE, null);
        }
        return message.get("content").getAsString();
    }
}
