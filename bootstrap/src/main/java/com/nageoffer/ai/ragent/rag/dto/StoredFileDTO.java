

package com.nageoffer.ai.ragent.rag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoredFileDTO {

    private String url;

    private String detectedType;

    private Long size;

    private String originalFilename;
}
