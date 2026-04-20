package com.linyi.phsm.application.rag.core.mcp.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * MCP 客户端配置属性
 */
@Data
@ConfigurationProperties(prefix = "rag.mcp")
public class MCPClientProperties {

    /**
     * MCP Server 列表
     */
    private List<ServerConfig> servers = new ArrayList<>();

    @Data
    public static class ServerConfig {

        /**
         * 服务名称
         */
        private String name;

        /**
         * 服务地址
         */
        private String url;
    }
}
