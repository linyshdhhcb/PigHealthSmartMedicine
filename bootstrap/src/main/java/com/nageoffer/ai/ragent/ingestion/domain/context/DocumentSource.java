

package com.nageoffer.ai.ragent.ingestion.domain.context;

import com.nageoffer.ai.ragent.ingestion.domain.enums.SourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 文档源实体类
 * 描述文档的来源信息，包括源类型、访问位置、文件名称以及访问凭证等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSource {

    /**
     * 文档源类型
     * 如 file、url、feishu、s3 等
     */
    private SourceType type;

    /**
     * 文档的访问位置
     * 可以是文件路径、URL地址或第三方平台的资源标识
     */
    private String location;

    /**
     * 文档的文件名
     */
    private String fileName;

    /**
     * 访问文档所需的凭证信息
     * 如 API Token、用户名密码等键值对
     */
    private Map<String, String> credentials;
}
