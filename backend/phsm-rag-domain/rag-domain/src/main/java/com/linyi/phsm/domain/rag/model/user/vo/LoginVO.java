package com.linyi.phsm.domain.rag.model.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private String userId;

    private String role;

    private String token;

    private String avatar;
}
