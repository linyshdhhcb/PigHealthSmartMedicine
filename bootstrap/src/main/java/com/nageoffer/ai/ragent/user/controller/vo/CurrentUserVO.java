

package com.nageoffer.ai.ragent.user.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserVO {

    private String userId;

    private String username;

    private String role;

    private String avatar;
}
