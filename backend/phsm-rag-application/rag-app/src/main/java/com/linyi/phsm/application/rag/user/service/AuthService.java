package com.linyi.phsm.application.rag.user.service;

import com.linyi.phsm.domain.rag.model.user.request.LoginRequest;
import com.linyi.phsm.domain.rag.model.user.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginRequest requestParam);

    void logout();
}
