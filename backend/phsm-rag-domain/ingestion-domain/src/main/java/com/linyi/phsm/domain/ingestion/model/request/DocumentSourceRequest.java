package com.linyi.phsm.domain.ingestion.model.request;

import com.linyi.phsm.domain.ingestion.model.enums.SourceType;
import lombok.Data;

import java.util.Map;

/**
 * 文档源请求对象
 * 用于接收创建摄取任务时的文档来源信息，包括来源类型、位置、文件名及访问凭证
 */
@Data
public class DocumentSourceRequest {

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
