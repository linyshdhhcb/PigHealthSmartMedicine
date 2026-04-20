package com.linyi.phsm.framework.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 RAG Trace 根节点（一次完整请求）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RagTraceRoot {

    /**
     * 链路名称（用于展示）
     */
    String name() default "";

    /**
     * 会话 ID 参数名
     */
    String conversationIdArg() default "conversationId";

    /**
     * 任务 ID 参数名
     */
    String taskIdArg() default "taskId";
}
