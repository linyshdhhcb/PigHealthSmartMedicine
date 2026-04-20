package com.linyi.phsm.domain.rag.model.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {

    private String id;
    private String username;
    private String role;
    private String avatar;
    private Date createTime;
    private Date updateTime;
}
