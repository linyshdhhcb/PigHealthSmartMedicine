package com.linyi.phsm.framework.mq.producer;

import org.apache.rocketmq.client.producer.SendResult;

import java.util.function.Consumer;

/**
 * 消息队列生产者接口
 */
public interface MessageQueueProducer {

    /**
     * 发送消息
     *
     * @param topic   目标 topic
     * @param keys    业务 key，可用于幂等判断
     * @param bizDesc 业务描述，用于日志标识
     * @param body    业务载荷
     * @return RocketMQ 发送结果，包含 msgId、sendStatus 等信息
     */
    SendResult send(String topic, String keys, String bizDesc, Object body);

    /**
     * 发送事务消息
     * <p>
     * 流程：发送 half 消息 → 执行本地事务 → 根据结果 commit/rollback
     * <p>
     * 事务回查由按 topic 注册的 {@link TransactionChecker} 处理，需提前通过
     * {@link DelegatingTransactionListener#registerChecker(String, TransactionChecker)} 注册
     *
     * @param topic            目标 topic
     * @param keys             业务 key
     * @param bizDesc          业务描述
     * @param body             业务载荷
     * @param localTransaction 本地事务逻辑，在 half 消息发送成功后执行；抛异常则回滚消息
     */
    void sendInTransaction(String topic, String keys, String bizDesc, Object body,
                           Consumer<Object> localTransaction);
}
