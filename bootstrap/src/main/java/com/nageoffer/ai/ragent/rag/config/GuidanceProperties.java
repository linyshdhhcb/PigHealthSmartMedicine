

package com.nageoffer.ai.ragent.rag.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rag.guidance")
public class GuidanceProperties {

    /**
     * 是否启用引导式问答
     */
    private Boolean enabled = true;

    /**
     * 触发引导式问答的最低分数相对阈值（次高分 >= 最高分 * ratio）
     */
    private Double ambiguityScoreRatio = 0.8D;

    /**
     * 单次最多展示的选项数量
     */
    private Integer maxOptions = 6;
}
