package com.linyi.pig.entity.vo.files;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: FilesUpdateVo
* @Version: 1.0
* @Description: 文件信息修改实体
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "文件信息修改实体")
public class FilesUpdateVo implements Serializable {
    /**
    * 主键ID
    */
    private Serializable id;

}
