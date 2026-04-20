package com.linyi.phsm.application.rag.core.intent;

import com.linyi.phsm.domain.rag.model.intent.IntentNode;
import com.linyi.phsm.domain.rag.model.intent.NodeScore;

import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * NodeScore 过滤工具类
 * 统一 KB / MCP 意图的过滤逻辑，避免多处重复定义
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class NodeScoreFilters {

    /**
     * 过滤 MCP 类型意图（node 非空、kind=MCP、mcpToolId 非空）
     */
    public static List<NodeScore> mcp(List<NodeScore> scores) {
        return scores.stream()
                .filter(ns -> ns.getNode() != null && ns.getNode().isMCP())
                .filter(ns -> StrUtil.isNotBlank(ns.getNode().getMcpToolId()))
                .toList();
    }

    /**
     * 过滤 KB 类型意图（node 非空、kind 为 null 或 KB）
     */
    public static List<NodeScore> kb(List<NodeScore> scores) {
        return scores.stream()
                .filter(ns -> ns.getNode() != null && ns.getNode().isKB())
                .toList();
    }
}
