package com.linyi.pig.entity.vo.articleTypes;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: ArticleTypesAddVo
* @Version: 1.0
* @Description: 文章类型新增实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "文章类型新增实体")
public class ArticleTypesAddVo implements Serializable {

    /**
     * 文章类型名称
     */
    @TableField("type_name")
    @Schema(name = "typeName",description = "文章类型名称",type = "varchar")
    private String typeName;

}
