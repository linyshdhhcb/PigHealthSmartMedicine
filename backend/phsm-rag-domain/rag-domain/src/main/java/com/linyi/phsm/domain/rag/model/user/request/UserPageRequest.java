package com.linyi.phsm.domain.rag.model.user.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 用户分页查询请求
 */
@Data
public class UserPageRequest extends Page {

    /**
     * 关键词（支持匹配用户名/角色）
     */
    private String keyword;
}
