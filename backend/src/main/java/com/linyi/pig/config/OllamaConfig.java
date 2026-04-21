package com.linyi.pig.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("'${ai.selection.chat-provider:ollama}' == 'ollama' || '${ai.selection.embedding-provider:ollama}' == 'ollama'")
public class OllamaConfig {
}
