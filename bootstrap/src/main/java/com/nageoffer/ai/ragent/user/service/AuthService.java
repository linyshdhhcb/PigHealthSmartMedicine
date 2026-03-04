

package com.nageoffer.ai.ragent.user.service;

import com.nageoffer.ai.ragent.user.controller.request.LoginRequest;
import com.nageoffer.ai.ragent.user.controller.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginRequest requestParam);

    void logout();
}
