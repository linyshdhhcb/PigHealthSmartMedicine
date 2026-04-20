package com.linyi.phsm.application.ingestion.fetcher;

import com.linyi.phsm.domain.ingestion.model.context.DocumentSource;
import com.linyi.phsm.domain.ingestion.model.enums.SourceType;

/**
 * 文档提取接口，用于从不同源获取文档数据
 */
public interface DocumentFetcher {

    /**
     * 获取支持的源类型
     *
     * @return 对应的源类型
     */
    SourceType supportedType();

    /**
     * 从给定的源中抓取文档
     *
     * @param source 文档数据源
     * @return 抓取结果，包含文档内容及其元数据
     */
    FetchResult fetch(DocumentSource source);
}
