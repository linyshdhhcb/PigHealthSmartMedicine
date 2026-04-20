package com.linyi.phsm.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Ragent 核心应用启动类
 */
@SpringBootApplication(scanBasePackages = "com.linyi.phsm")
@EnableScheduling
@MapperScan(basePackages = {
        "com.linyi.phsm.infrastructure.persistence.rag.mapper",
        "com.linyi.phsm.infrastructure.persistence.ingestion.mapper",
        "com.linyi.phsm.infrastructure.persistence.knowledge.mapper",
        "com.linyi.phsm.infrastructure.persistence.rag.mapper"
})
public class RagentApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagentApplication.class, args);
    }
}
