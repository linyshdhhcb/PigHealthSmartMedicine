package com.linyi.phsm.infrastructure.ai.rerank;

import com.linyi.phsm.framework.model.RetrievedChunk;
import com.linyi.phsm.infrastructure.ai.common.ModelCapability;
import com.linyi.phsm.infrastructure.ai.routing.ModelRoutingExecutor;
import com.linyi.phsm.infrastructure.ai.routing.ModelSelector;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 路由式重排服务实现类
 * <p>
 * 该服务通过模型路由机制动态选择合适的重排客户端，并支持失败降级策略
 * 作为主要的重排服务实现，用于对检索到的文档块进行相关性重新排序
 */
@Service
@Primary
public class RoutingRerankService implements RerankService {

    private final ModelSelector selector;
    private final ModelRoutingExecutor executor;
    private final Map<String, RerankClient> clientsByProvider;

    public RoutingRerankService(ModelSelector selector, ModelRoutingExecutor executor, List<RerankClient> clients) {
        this.selector = selector;
        this.executor = executor;
        this.clientsByProvider = clients.stream()
                .collect(Collectors.toMap(RerankClient::provider, Function.identity()));
    }

    @Override
    public List<RetrievedChunk> rerank(String query, List<RetrievedChunk> candidates, int topN) {
        return executor.executeWithFallback(
                ModelCapability.RERANK,
                selector.selectRerankCandidates(),
                target -> clientsByProvider.get(target.candidate().getProvider()),
                (client, target) -> client.rerank(query, candidates, topN, target)
        );
    }
}
