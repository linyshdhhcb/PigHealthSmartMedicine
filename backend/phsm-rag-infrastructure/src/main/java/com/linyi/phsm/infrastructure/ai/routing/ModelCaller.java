package com.linyi.phsm.infrastructure.ai.routing;

/**
 * 模型调用器函数式接口
 * <p>
 * 该接口定义了一个通用的模型调用方法，用于执行模型相关的操作。
 * 通过泛型参数支持不同类型的客户端和返回值，提供了灵活的扩展能力。
 * </p>
 *
 * @param <C> 客户端类型，表示用于调用模型的客户端实例
 * @param <T> 返回值类型，表示模型调用后的返回结果
 */
@FunctionalInterface
public interface ModelCaller<C, T> {

    /**
     * 执行模型调用
     *
     * @param client 模型客户端实例，用于与模型进行交互
     * @param target 模型目标配置，包含模型调用所需的目标信息
     * @return 模型调用的结果
     * @throws Exception 当模型调用过程中发生错误时抛出异常
     */
    T call(C client, ModelTarget target) throws Exception;
}
