

package com.nageoffer.ai.ragent.infra.chat;

import com.nageoffer.ai.ragent.infra.http.ModelClientErrorType;
import com.nageoffer.ai.ragent.infra.http.ModelClientException;
import okhttp3.Call;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * 流式任务异步执行器
 * 统一处理线程池提交、拒绝兜底和取消句柄构建逻辑
 */
final class StreamAsyncExecutor {

    private static final String STREAM_BUSY_MESSAGE = "流式线程池繁忙";

    private StreamAsyncExecutor() {
    }

    static StreamCancellationHandle submit(Executor executor,
                                           Call call,
                                           StreamCallback callback,
                                           Consumer<AtomicBoolean> streamTask) {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        try {
            CompletableFuture.runAsync(() -> streamTask.accept(cancelled), executor);
        } catch (RejectedExecutionException ex) {
            call.cancel();
            callback.onError(new ModelClientException(STREAM_BUSY_MESSAGE, ModelClientErrorType.SERVER_ERROR, null, ex));
            return StreamCancellationHandles.noop();
        }
        return StreamCancellationHandles.fromOkHttp(call, cancelled);
    }
}
