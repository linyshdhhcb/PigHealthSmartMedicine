/*
 * 
 * Could not load the following classes:
 *  cn.hutool.core.collection.CollUtil
 *  com.linyi.phsm.framework.errorcode.BaseErrorCode
 *  com.linyi.phsm.framework.errorcode.ErrorCode
 *  com.linyi.phsm.framework.exception.RemoteException
 *  com.linyi.phsm.framework.model.ChatRequest
 *  com.linyi.phsm.framework.trace.RagTraceNode
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.context.annotation.Primary
 *  org.springframework.stereotype.Service
 *  org.springframework.util.StringUtils
 */
package com.linyi.phsm.infrastructure.ai.chat;

import cn.hutool.core.collection.CollUtil;
import com.linyi.phsm.framework.errorcode.BaseErrorCode;
import com.linyi.phsm.framework.errorcode.ErrorCode;
import com.linyi.phsm.framework.exception.RemoteException;
import com.linyi.phsm.framework.model.ChatRequest;
import com.linyi.phsm.framework.trace.RagTraceNode;
import com.linyi.phsm.infrastructure.ai.chat.ChatClient;
import com.linyi.phsm.infrastructure.ai.chat.LLMService;
import com.linyi.phsm.infrastructure.ai.stream.ProbeStreamBridge;
import com.linyi.phsm.infrastructure.ai.stream.StreamCallback;
import com.linyi.phsm.infrastructure.ai.stream.StreamCancellationHandle;
import com.linyi.phsm.infrastructure.ai.common.ModelCapability;
import com.linyi.phsm.infrastructure.ai.routing.ModelHealthStore;
import com.linyi.phsm.infrastructure.ai.routing.ModelRoutingExecutor;
import com.linyi.phsm.infrastructure.ai.routing.ModelSelector;
import com.linyi.phsm.infrastructure.ai.routing.ModelTarget;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
public class RoutingLlmService
implements LLMService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(RoutingLlmService.class);
    private static final int FIRST_PACKET_TIMEOUT_SECONDS = 60;
    private static final String STREAM_INTERRUPTED_MESSAGE = "\u6d41\u5f0f\u8bf7\u6c42\u88ab\u4e2d\u65ad";
    private static final String STREAM_NO_PROVIDER_MESSAGE = "\u65e0\u53ef\u7528\u5927\u6a21\u578b\u63d0\u4f9b\u8005";
    private static final String STREAM_START_FAILED_MESSAGE = "\u6d41\u5f0f\u8bf7\u6c42\u542f\u52a8\u5931\u8d25";
    private static final String STREAM_TIMEOUT_MESSAGE = "\u6d41\u5f0f\u9996\u5305\u8d85\u65f6";
    private static final String STREAM_NO_CONTENT_MESSAGE = "\u6d41\u5f0f\u8bf7\u6c42\u672a\u8fd4\u56de\u5185\u5bb9";
    private static final String STREAM_ALL_FAILED_MESSAGE = "\u5927\u6a21\u578b\u8c03\u7528\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5...";
    private final ModelSelector selector;
    private final ModelHealthStore healthStore;
    private final ModelRoutingExecutor executor;
    private final Map<String, ChatClient> clientsByProvider;

    public RoutingLlmService(ModelSelector selector, ModelHealthStore healthStore, ModelRoutingExecutor executor, List<ChatClient> clients) {
        super();
        this.selector = selector;
        this.healthStore = healthStore;
        this.executor = executor;
        this.clientsByProvider = clients.stream().collect(Collectors.toMap(ChatClient::provider, Function.identity()));
    }

    @Override
    @RagTraceNode(name="llm-chat-routing", type="LLM_ROUTING")
    public String chat(ChatRequest request) {
        return this.executor.executeWithFallback(ModelCapability.CHAT, this.selector.selectChatCandidates(Boolean.TRUE.equals(request.getThinking())), target -> this.clientsByProvider.get(target.candidate().getProvider()), (client, target) -> client.chat(request, target));
    }

    @Override
    public String chat(ChatRequest request, String modelId) {
        if (!StringUtils.hasText((String)modelId)) {
            return this.chat(request);
        }
        return this.executor.executeWithFallback(ModelCapability.CHAT, List.of(this.resolveTarget(modelId, Boolean.TRUE.equals(request.getThinking()))), target -> this.clientsByProvider.get(target.candidate().getProvider()), (client, target) -> client.chat(request, target));
    }

    @Override
    @RagTraceNode(name="llm-stream-routing", type="LLM_ROUTING")
    public StreamCancellationHandle streamChat(ChatRequest request, StreamCallback callback) {
        List<ModelTarget> targets = this.selector.selectChatCandidates(Boolean.TRUE.equals(request.getThinking()));
        if (CollUtil.isEmpty(targets)) {
            throw new RemoteException(STREAM_NO_PROVIDER_MESSAGE);
        }
        String label = ModelCapability.CHAT.getDisplayName();
        Throwable lastError = null;
        for (ModelTarget target : targets) {
            StreamCancellationHandle handle;
            ChatClient client = this.resolveClient(target, label);
            if (client == null || !this.healthStore.allowCall(target.id())) continue;
            ProbeStreamBridge bridge = new ProbeStreamBridge(callback);
            try {
                handle = client.streamChat(request, bridge, target);
            }
            catch (Exception e) {
                this.healthStore.markFailure(target.id());
                lastError = e;
                log.warn("{} \u6d41\u5f0f\u8bf7\u6c42\u542f\u52a8\u5931\u8d25\uff0c\u5207\u6362\u4e0b\u4e00\u4e2a\u6a21\u578b\u3002modelId\uff1a{}\uff0cprovider\uff1a{}", new Object[]{label, target.id(), target.candidate().getProvider(), e});
                continue;
            }
            if (handle == null) {
                this.healthStore.markFailure(target.id());
                lastError = new RemoteException(STREAM_START_FAILED_MESSAGE, (ErrorCode)BaseErrorCode.REMOTE_ERROR);
                log.warn("{} \u6d41\u5f0f\u8bf7\u6c42\u672a\u8fd4\u56de\u53d6\u6d88\u53e5\u67c4\uff0c\u5207\u6362\u4e0b\u4e00\u4e2a\u6a21\u578b\u3002modelId\uff1a{}\uff0cprovider\uff1a{}", new Object[]{label, target.id(), target.candidate().getProvider()});
                continue;
            }
            ProbeStreamBridge.ProbeResult result = this.awaitFirstPacket(bridge, handle, callback);
            if (result.isSuccess()) {
                this.healthStore.markSuccess(target.id());
                return handle;
            }
            this.healthStore.markFailure(target.id());
            handle.cancel();
            lastError = this.buildLastErrorAndLog(result, target, label);
        }
        throw this.notifyAllFailed(callback, lastError);
    }

    private ChatClient resolveClient(ModelTarget target, String label) {
        ChatClient client = this.clientsByProvider.get(target.candidate().getProvider());
        if (client == null) {
            log.warn("{} \u63d0\u4f9b\u5546\u5ba2\u6237\u7aef\u7f3a\u5931: provider\uff1a{}\uff0cmodelId\uff1a{}", new Object[]{label, target.candidate().getProvider(), target.id()});
        }
        return client;
    }

    private ProbeStreamBridge.ProbeResult awaitFirstPacket(ProbeStreamBridge bridge, StreamCancellationHandle handle, StreamCallback callback) {
        try {
            return bridge.awaitFirstPacket(60L, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            handle.cancel();
            RemoteException interruptedException = new RemoteException(STREAM_INTERRUPTED_MESSAGE, (Throwable)e, (ErrorCode)BaseErrorCode.REMOTE_ERROR);
            callback.onError((Throwable)interruptedException);
            throw interruptedException;
        }
    }

    private Throwable buildLastErrorAndLog(ProbeStreamBridge.ProbeResult result, ModelTarget target, String label) {
        switch (result.getType()) {
            case ERROR: {
                Throwable error = result.getError() != null ? result.getError() : new RemoteException("\u6d41\u5f0f\u8bf7\u6c42\u5931\u8d25", (ErrorCode)BaseErrorCode.REMOTE_ERROR);
                log.warn("{} \u5931\u8d25\u6a21\u578b: modelId={}, provider={}\uff0c\u539f\u56e0: \u6d41\u5f0f\u8bf7\u6c42\u5931\u8d25\uff0c\u5207\u6362\u4e0b\u4e00\u4e2a\u6a21\u578b", new Object[]{label, target.id(), target.candidate().getProvider(), error});
                return error;
            }
            case TIMEOUT: {
                RemoteException timeout = new RemoteException(STREAM_TIMEOUT_MESSAGE, (ErrorCode)BaseErrorCode.REMOTE_ERROR);
                log.warn("{} \u5931\u8d25\u6a21\u578b: modelId={}, provider={}\uff0c\u539f\u56e0: \u6d41\u5f0f\u8bf7\u6c42\u8d85\u65f6\uff0c\u5207\u6362\u4e0b\u4e00\u4e2a\u6a21\u578b", new Object[]{label, target.id(), target.candidate().getProvider()});
                return timeout;
            }
            case NO_CONTENT: {
                RemoteException noContent = new RemoteException(STREAM_NO_CONTENT_MESSAGE, (ErrorCode)BaseErrorCode.REMOTE_ERROR);
                log.warn("{} \u5931\u8d25\u6a21\u578b: modelId={}, provider={}\uff0c\u539f\u56e0: \u6d41\u5f0f\u8bf7\u6c42\u65e0\u5185\u5bb9\u5b8c\u6210\uff0c\u5207\u6362\u4e0b\u4e00\u4e2a\u6a21\u578b", new Object[]{label, target.id(), target.candidate().getProvider()});
                return noContent;
            }
        }
        RemoteException unknown = new RemoteException("\u6d41\u5f0f\u8bf7\u6c42\u5931\u8d25", (ErrorCode)BaseErrorCode.REMOTE_ERROR);
        log.warn("{} \u5931\u8d25\u6a21\u578b: modelId={}, provider={}\uff0c\u539f\u56e0: \u6d41\u5f0f\u8bf7\u6c42\u5931\u8d25\uff08\u672a\u77e5\u7c7b\u578b\uff09\uff0c\u5207\u6362\u4e0b\u4e00\u4e2a\u6a21\u578b", new Object[]{label, target.id(), target.candidate().getProvider()});
        return unknown;
    }

    private RemoteException notifyAllFailed(StreamCallback callback, Throwable lastError) {
        RemoteException finalException = new RemoteException(STREAM_ALL_FAILED_MESSAGE, lastError, (ErrorCode)BaseErrorCode.REMOTE_ERROR);
        callback.onError((Throwable)finalException);
        return finalException;
    }

    private ModelTarget resolveTarget(String modelId, boolean deepThinking) {
        return this.selector.selectChatCandidates(deepThinking).stream().filter(target -> modelId.equals(target.id())).findFirst().orElseThrow(() -> new RemoteException("Chat \u6a21\u578b\u4e0d\u53ef\u7528: " + modelId));
    }
}
