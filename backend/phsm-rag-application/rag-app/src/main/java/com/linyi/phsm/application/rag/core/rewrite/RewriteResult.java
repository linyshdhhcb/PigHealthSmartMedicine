package com.linyi.phsm.application.rag.core.rewrite;

import java.util.List;

public record RewriteResult(String rewrittenQuestion, List<String> subQuestions) {

}
