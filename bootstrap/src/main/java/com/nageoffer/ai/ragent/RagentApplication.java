

package com.nageoffer.ai.ragent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Ragent 核心应用启动类
 */
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = {
        "com.nageoffer.ai.ragent.rag.dao.mapper",
        "com.nageoffer.ai.ragent.ingestion.dao.mapper",
        "com.nageoffer.ai.ragent.knowledge.dao.mapper",
        "com.nageoffer.ai.ragent.user.dao.mapper",
        "com.nageoffer.ai.ragent.pig.dao.mapper"
})
public class RagentApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagentApplication.class, args);
    }
}
