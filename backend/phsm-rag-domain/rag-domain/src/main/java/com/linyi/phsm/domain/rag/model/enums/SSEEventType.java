package com.linyi.phsm.domain.rag.model.enums;

import lombok.RequiredArgsConstructor;

/**
 * SSE 事件类型枚举
 */
@RequiredArgsConstructor
public enum SSEEventType {

    /**
     * 会话与任务的元信息事件
     */
    META("meta"),

    /**
     * 增量消息事件
     */
    MESSAGE("message"),

    /**
     * 模型回复完成事件
     */
    FINISH("finish"),

    /**
     * 完成事件
     */
    DONE("done"),

    /**
     * 取消事件
     */
    CANCEL("cancel"),

    /**
     * 拒绝事件
     */
    REJECT("reject");

    private final String value;

    /**
     * SSE 事件名称（与前端约定一致）
     */
    public String value() {
        return value;
    }
}
