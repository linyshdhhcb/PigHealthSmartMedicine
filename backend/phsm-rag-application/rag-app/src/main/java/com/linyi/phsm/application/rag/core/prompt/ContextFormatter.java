package com.linyi.phsm.application.rag.core.prompt;

import com.linyi.phsm.framework.model.RetrievedChunk;
import com.linyi.phsm.domain.rag.model.intent.NodeScore;
import com.linyi.phsm.application.rag.core.mcp.MCPResponse;

import java.util.List;
import java.util.Map;

public interface ContextFormatter {

    String formatKbContext(List<NodeScore> kbIntents, Map<String, List<RetrievedChunk>> rerankedByIntent, int topK);

    String formatMcpContext(List<MCPResponse> responses, List<NodeScore> mcpIntents);
}
