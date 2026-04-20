package com.linyi.phsm.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 体验环境只读模式配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class DemoModeProperties {

    /**
     * 是否开启体验环境只读模式，默认关闭
     */
    private Boolean demoMode = false;
}
