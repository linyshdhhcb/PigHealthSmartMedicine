

package com.nageoffer.ai.ragent.knowledge.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 定时任务执行状态
 */
@Getter
@RequiredArgsConstructor
public enum ScheduleRunStatus {

    /**
     * 正在运行
     */
    RUNNING("running"),

    /**
     * 执行成功
     */
    SUCCESS("success"),

    /**
     * 执行失败
     */
    FAILED("failed"),

    /**
     * 已跳过
     */
    SKIPPED("skipped");

    private final String code;
}
