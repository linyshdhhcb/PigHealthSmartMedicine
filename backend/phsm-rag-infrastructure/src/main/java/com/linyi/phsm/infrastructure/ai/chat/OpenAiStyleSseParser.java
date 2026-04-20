/*
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  lombok.Generated
 */
package com.linyi.phsm.infrastructure.ai.chat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Generated;

public final class OpenAiStyleSseParser {
    private static final String DATA_PREFIX = "data:";
    private static final String DONE_MARKER = "[DONE]";

    static ParsedEvent parseLine(String line, Gson gson, boolean reasoningEnabled) {
        if (line == null || line.isBlank()) {
            return ParsedEvent.empty();
        }
        String payload = line.trim();
        if (payload.startsWith(DATA_PREFIX)) {
            payload = payload.substring(DATA_PREFIX.length()).trim();
        }
        if (DONE_MARKER.equalsIgnoreCase(payload)) {
            return ParsedEvent.done();
        }
        JsonObject obj = (JsonObject)gson.fromJson(payload, JsonObject.class);
        JsonArray choices = obj.getAsJsonArray("choices");
        if (choices == null || choices.isEmpty()) {
            return ParsedEvent.empty();
        }
        JsonObject choice0 = choices.get(0).getAsJsonObject();
        String content = OpenAiStyleSseParser.extractText(choice0, "content");
        String reasoning = reasoningEnabled ? OpenAiStyleSseParser.extractText(choice0, "reasoning_content") : null;
        boolean completed = OpenAiStyleSseParser.hasFinishReason(choice0);
        return new ParsedEvent(content, reasoning, completed);
    }

    private static boolean hasFinishReason(JsonObject choice) {
        if (choice == null || !choice.has("finish_reason")) {
            return false;
        }
        JsonElement finishReason = choice.get("finish_reason");
        return finishReason != null && !finishReason.isJsonNull();
    }

    private static String extractText(JsonObject choice, String fieldName) {
        JsonObject message;
        JsonElement value;
        JsonObject delta;
        if (choice == null) {
            return null;
        }
        if (choice.has("delta") && choice.get("delta").isJsonObject() && (delta = choice.getAsJsonObject("delta")).has(fieldName) && (value = delta.get(fieldName)) != null && !value.isJsonNull()) {
            return value.getAsString();
        }
        if (choice.has("message") && choice.get("message").isJsonObject() && (message = choice.getAsJsonObject("message")).has(fieldName) && (value = message.get(fieldName)) != null && !value.isJsonNull()) {
            return value.getAsString();
        }
        return null;
    }

    private OpenAiStyleSseParser() {
        super();
    }

    record ParsedEvent(String content, String reasoning, boolean completed) {

        static ParsedEvent empty() {
            return new ParsedEvent(null, null, false);
        }

        static ParsedEvent done() {
            return new ParsedEvent(null, null, true);
        }

        boolean hasContent() {
            return this.content != null && !this.content.isEmpty();
        }

        boolean hasReasoning() {
            return this.reasoning != null && !this.reasoning.isEmpty();
        }
    }
}
