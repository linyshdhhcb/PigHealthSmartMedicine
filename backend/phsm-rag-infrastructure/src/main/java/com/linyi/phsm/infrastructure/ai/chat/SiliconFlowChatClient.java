package com.linyi.phsm.infrastructure.ai.chat;

import com.linyi.phsm.framework.model.ChatRequest;
import com.linyi.phsm.framework.trace.RagTraceNode;
import com.linyi.phsm.infrastructure.ai.common.ModelProvider;
import com.linyi.phsm.infrastructure.ai.routing.ModelTarget;
import com.linyi.phsm.infrastructure.ai.stream.StreamCallback;
import com.linyi.phsm.infrastructure.ai.stream.StreamCancellationHandle;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Slf4j
@Service
public class SiliconFlowChatClient extends AbstractOpenAiStyleChatClient {

    public SiliconFlowChatClient(OkHttpClient httpClient, Executor modelStreamExecutor) {
        super(httpClient, modelStreamExecutor);
    }

    @Override
    public String provider() {
        return ModelProvider.SILICON_FLOW.getId();
    }

    @Override
    @RagTraceNode(name = "siliconflow-chat", type = "LLM_PROVIDER")
    public String chat(ChatRequest request, ModelTarget target) {
        return doChat(request, target);
    }

    @Override
    @RagTraceNode(name = "siliconflow-stream-chat", type = "LLM_PROVIDER")
    public StreamCancellationHandle streamChat(ChatRequest request, StreamCallback callback, ModelTarget target) {
        return doStreamChat(request, callback, target);
    }
}
