package com.linyi.phsm.bootstrap.interceptor;
import com.linyi.phsm.infrastructure.config.DemoModeProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linyi.phsm.framework.convention.Result;
import com.linyi.phsm.framework.errorcode.BaseErrorCode;
import com.linyi.phsm.domain.rag.model.dto.MessageDelta;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.Set;

/**
 * 体验环境只读模式拦截器
 */
@Component
@RequiredArgsConstructor
public class DemoModeInterceptor implements HandlerInterceptor {

    private final DemoModeProperties demoModeProperties;
    private final ObjectMapper objectMapper;

    private static final String DEMO_REJECT_MESSAGE = "体验环境仅支持查询操作";

    /**
     * 需要拦截的 SSE 流式接口路径
     */
    private static final Set<String> SSE_PATHS = Set.of("/rag/v3/chat");

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if (!demoModeProperties.getDemoMode()) {
            return true;
        }
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        String path = request.getRequestURI().substring(request.getContextPath().length());
        boolean isSsePath = SSE_PATHS.contains(path);
        if ("GET".equalsIgnoreCase(method) && !isSsePath) {
            return true;
        }
        if (isSsePath) {
            writeSseReject(response);
        } else {
            writeJsonReject(response);
        }
        return false;
    }

    private void writeSseReject(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        PrintWriter writer = response.getWriter();
        writer.write("event: reject\ndata: " + objectMapper.writeValueAsString(new MessageDelta("response", DEMO_REJECT_MESSAGE)) + "\n\n");
        writer.write("event: done\ndata: \"[DONE]\"\n\n");
        writer.flush();
    }

    private void writeJsonReject(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = new Result<Void>()
                .setCode(BaseErrorCode.CLIENT_ERROR.code())
                .setMessage(DEMO_REJECT_MESSAGE);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
