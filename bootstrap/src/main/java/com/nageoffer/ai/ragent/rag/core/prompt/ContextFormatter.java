

package com.nageoffer.ai.ragent.rag.core.prompt;

import com.nageoffer.ai.ragent.framework.convention.RetrievedChunk;
import com.nageoffer.ai.ragent.rag.core.intent.NodeScore;
import com.nageoffer.ai.ragent.rag.core.mcp.MCPResponse;

import java.util.List;
import java.util.Map;

public interface ContextFormatter {

    String formatKbContext(List<NodeScore> kbIntents, Map<String, List<RetrievedChunk>> rerankedByIntent, int topK);

    String formatMcpContext(List<MCPResponse> responses, List<NodeScore> mcpIntents);
}
