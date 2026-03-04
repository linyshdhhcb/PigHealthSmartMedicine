

package com.nageoffer.ai.ragent.ingestion.util;

import java.util.Map;

/**
 * 提示词模板渲染器
 */
public final class PromptTemplateRenderer {

    private PromptTemplateRenderer() {
    }

    public static String render(String template, Map<String, Object> variables) {
        if (template == null || template.isBlank()) {
            return template;
        }
        String out = template;
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String key = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() == null ? "" : String.valueOf(entry.getValue());
                out = out.replace(key, value);
            }
        }
        return out;
    }
}
