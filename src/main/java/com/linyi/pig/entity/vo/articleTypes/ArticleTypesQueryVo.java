package com.linyi.pig.entity.vo.articleTypes;

import com.linyi.pig.common.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: ArticleTypesQueryVo
* @Version: 1.0
* @Description: 文章类型查询实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "文章类型查询实体")
public class ArticleTypesQueryVo extends PageResponse implements Serializable {

}
