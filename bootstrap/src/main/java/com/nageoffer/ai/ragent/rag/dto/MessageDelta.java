

package com.nageoffer.ai.ragent.rag.dto;

/**
 * 消息增量记录类
 * 用于表示消息的类型和增量数据
 */
public record MessageDelta(String type, String delta) {

    /**
     * 消息类型
     */
    public String type() {
        return type;
    }

    /**
     * 增量数据
     */
    public String delta() {
        return delta;
    }
}
