package com.linyi.pig.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // 设置 openapi 基础参数
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("面向生猪健康管理的智慧医药系统 API 文档")
                        .description("这是一个用于面向生猪健康管理的智慧医药系统API文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("linyi")
                                .email("linyi@example.com")
                                .url("https://linyi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .termsOfService("https://example.com/terms"));
    }
}
