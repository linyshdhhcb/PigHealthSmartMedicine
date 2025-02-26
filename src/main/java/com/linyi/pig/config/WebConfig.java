package com.linyi.pig.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: linyi
 * @Date: 2025/2/14
 * @ClassName: WebConfig
 * @Version: 1.0
 * @Description: 跨域配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 开启跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                //.allowedOrigins("/*")
                .allowedOriginPatterns("*")//生产环境下慎用，否则可能会被攻击
                // 设置允许的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // 允许的头
                .allowCredentials(true); // 是否允许携带凭证
    }

}

