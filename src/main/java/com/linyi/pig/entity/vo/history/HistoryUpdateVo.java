package com.linyi.pig.entity.vo.history;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: HistoryUpdateVo
* @Version: 1.0
* @Description: 操作记录修改实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "操作记录修改实体")
public class HistoryUpdateVo implements Serializable {
    /**
    * 主键ID
    */
    private Serializable id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(name = "userId",description = "用户ID",type = "int")
    private Integer userId;

    /**
     * 搜索关键字
     */
    @TableField("keyword")
    @Schema(name = "keyword",description = "搜索关键字",type = "varchar")
    private String keyword;

    /**
     * 类型：1搜索，2科目，3药品
     */
    @TableField("operate_type")
    @Schema(name = "operateType",description = "类型：1搜索，2科目，3药品",type = "int")
    private Integer operateType;

}
