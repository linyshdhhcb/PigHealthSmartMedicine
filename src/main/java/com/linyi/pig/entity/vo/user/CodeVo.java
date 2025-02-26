package com.linyi.pig.entity.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: linyi
 * @Date: 2025/2/25
 * @ClassName: CodeVo
 * @Version: 1.0
 * @Description: 验证码
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "验证码")
public class CodeVo {

    @NotBlank(message = "验证码不能为空")
    @Schema(name = "code",description = "验证码",type = "varchar")
    private String code;

    @Schema(name = "key",description = "验证码key",type = "varchar")
    private String key;
}
