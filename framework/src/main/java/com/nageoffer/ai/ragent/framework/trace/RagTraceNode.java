

package com.nageoffer.ai.ragent.framework.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 RAG 链路中的普通节点
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RagTraceNode {

    /**
     * 节点名称（用于展示）
     */
    String name() default "";

    /**
     * 节点类型（用于分组统计）
     */
    String type() default "METHOD";
}
