

package com.nageoffer.ai.ragent.infra.chat;

import okhttp3.Call;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * StreamCancellationHandle 工具类
 * 用于构建常见的取消句柄，统一幂等取消语义
 */
public final class StreamCancellationHandles {

    private static final StreamCancellationHandle NOOP = () -> {
    };

    private StreamCancellationHandles() {
    }

    public static StreamCancellationHandle noop() {
        return NOOP;
    }

    public static StreamCancellationHandle fromOkHttp(Call call, AtomicBoolean cancelled) {
        return new OkHttpCancellationHandle(call, cancelled);
    }

    private static final class OkHttpCancellationHandle implements StreamCancellationHandle {

        private final Call call;
        private final AtomicBoolean cancelled;
        private final AtomicBoolean once = new AtomicBoolean(false);

        private OkHttpCancellationHandle(Call call, AtomicBoolean cancelled) {
            this.call = call;
            this.cancelled = cancelled;
        }

        @Override
        public void cancel() {
            if (!once.compareAndSet(false, true)) {
                return;
            }
            if (cancelled != null) {
                cancelled.set(true);
            }
            if (call != null) {
                call.cancel();
            }
        }
    }
}
