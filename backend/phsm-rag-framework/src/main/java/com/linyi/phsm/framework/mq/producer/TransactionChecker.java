package com.linyi.phsm.framework.mq.producer;

import com.linyi.phsm.framework.mq.MessageWrapper;

/**
 * 事务消息回查接口，按 topic 注册到 {@link DelegatingTransactionListener}
 * <p>
 * 回查时 Broker 可能将请求发送到任意实例，因此实现类必须基于消息内容（而非内存状态）查询 DB 判断本地事务是否已提交。
 */
public interface TransactionChecker {

    /**
     * 检查本地事务是否已提交
     *
     * @param message 消息体，包含业务载荷，可从中提取业务参数查询 DB
     * @return true 表示本地事务已提交（消息可投递），false 表示已回滚（消息丢弃）
     */
    boolean check(MessageWrapper<?> message);
}
