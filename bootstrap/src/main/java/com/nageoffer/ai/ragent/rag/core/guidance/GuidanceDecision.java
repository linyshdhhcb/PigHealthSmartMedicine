

package com.nageoffer.ai.ragent.rag.core.guidance;

import lombok.Getter;

/**
 * 引导式问答决策结果类
 * 用于表示是否需要向用户输出引导式问答提示
 */
@Getter
public class GuidanceDecision {

    public enum Action {
        NONE,
        PROMPT
    }

    private final Action action;
    private final String prompt;

    private GuidanceDecision(Action action, String prompt) {
        this.action = action;
        this.prompt = prompt;
    }

    public static GuidanceDecision none() {
        return new GuidanceDecision(Action.NONE, null);
    }

    public static GuidanceDecision prompt(String prompt) {
        return new GuidanceDecision(Action.PROMPT, prompt);
    }

    public boolean isPrompt() {
        return action == Action.PROMPT;
    }
}
