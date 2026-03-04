

package com.nageoffer.ai.ragent.rag.dto;

import java.util.List;

import com.nageoffer.ai.ragent.rag.core.intent.NodeScore;

/**
 * 意图分组（MCP 与 KB）
 *
 * @param mcpIntents MCP 意图列表
 * @param kbIntents  KB 意图列表
 */
public record IntentGroup(List<NodeScore> mcpIntents, List<NodeScore> kbIntents) {
}
