package com.linyi.pig.entity.vo.illnessKind;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linyi.pig.common.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: IllnessKindQueryVo
* @Version: 1.0
* @Description: 疾病种类查询实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "疾病种类查询实体")
public class IllnessKindQueryVo extends PageResponse implements Serializable {

    /**
     * 分类名称
     */
    @TableField("name")
    @Schema(name = "name",description = "分类名称",type = "varchar")
    private String name;

    /**
     * 描述
     */
    @TableField("info")
    @Schema(name = "info",description = "描述",type = "varchar")
    private String info;

}
