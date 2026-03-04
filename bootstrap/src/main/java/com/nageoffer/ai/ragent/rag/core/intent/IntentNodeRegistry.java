

package com.nageoffer.ai.ragent.rag.core.intent;

/**
 * 意图节点注册表
 * 用于在运行期快速获取意图树和节点信息
 */
public interface IntentNodeRegistry {

    /**
     * 根据节点 ID 获取节点
     */
    IntentNode getNodeById(String id);
}
