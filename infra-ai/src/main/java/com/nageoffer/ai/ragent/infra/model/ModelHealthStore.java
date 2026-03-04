

package com.nageoffer.ai.ragent.infra.model;

import com.nageoffer.ai.ragent.infra.config.AIModelProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模型健康状态存储器
 * 用于管理和跟踪各个 AI 模型的健康状况，实现断路器模式
 */
@Component
@RequiredArgsConstructor
public class ModelHealthStore {

    private final AIModelProperties properties;

    private final Map<String, ModelHealth> healthById = new ConcurrentHashMap<>();

    public boolean isOpen(String id) {
        ModelHealth health = healthById.get(id);
        if (health == null) {
            return false;
        }
        return health.state == State.OPEN && health.openUntil > System.currentTimeMillis();
    }

    public boolean allowCall(String id) {
        if (id == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        final boolean[] allowed = {false};
        healthById.compute(id, (k, v) -> {
            if (v == null) {
                v = new ModelHealth();
            }
            if (v.state == State.OPEN) {
                if (v.openUntil > now) {
                    return v;
                }
                v.state = State.HALF_OPEN;
                v.halfOpenInFlight = true;
                allowed[0] = true;
                return v;
            }
            if (v.state == State.HALF_OPEN) {
                if (v.halfOpenInFlight) {
                    return v;
                }
                v.halfOpenInFlight = true;
                allowed[0] = true;
                return v;
            }
            allowed[0] = true;
            return v;
        });
        return allowed[0];
    }

    public void markSuccess(String id) {
        if (id == null) {
            return;
        }
        healthById.compute(id, (k, v) -> {
            if (v == null) {
                return new ModelHealth();
            }
            v.state = State.CLOSED;
            v.consecutiveFailures = 0;
            v.openUntil = 0L;
            v.halfOpenInFlight = false;
            return v;
        });
    }

    public void markFailure(String id) {
        if (id == null) {
            return;
        }
        long now = System.currentTimeMillis();
        healthById.compute(id, (k, v) -> {
            if (v == null) {
                v = new ModelHealth();
            }
            if (v.state == State.HALF_OPEN) {
                v.state = State.OPEN;
                v.openUntil = now + properties.getSelection().getOpenDurationMs();
                v.consecutiveFailures = 0;
                v.halfOpenInFlight = false;
                return v;
            }
            v.consecutiveFailures++;
            if (v.consecutiveFailures >= properties.getSelection().getFailureThreshold()) {
                v.state = State.OPEN;
                v.openUntil = now + properties.getSelection().getOpenDurationMs();
                v.consecutiveFailures = 0;
            }
            return v;
        });
    }

    private static class ModelHealth {
        private int consecutiveFailures;
        private long openUntil;
        private boolean halfOpenInFlight;
        private State state;

        private ModelHealth() {
            this.consecutiveFailures = 0;
            this.openUntil = 0L;
            this.halfOpenInFlight = false;
            this.state = State.CLOSED;
        }
    }

    private enum State {
        CLOSED,
        OPEN,
        HALF_OPEN
    }
}
