package com.linyi.phsm.application.rag.core.prompt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromptBuildPlan {

    private PromptScene scene;

    private String baseTemplate;

    private String mcpContext;

    private String kbContext;

    private String question;
}
