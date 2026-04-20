package com.linyi.phsm.domain.rag.model.dto;

import java.util.List;

import com.linyi.phsm.domain.rag.model.intent.NodeScore;

/**
 * 意图分组（MCP 与 KB）
 *
 * @param mcpIntents MCP 意图列表
 * @param kbIntents  KB 意图列表
 */
public record IntentGroup(List<NodeScore> mcpIntents, List<NodeScore> kbIntents) {
}
