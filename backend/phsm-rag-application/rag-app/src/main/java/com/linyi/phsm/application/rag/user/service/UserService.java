package com.linyi.phsm.application.rag.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.linyi.phsm.domain.rag.model.user.request.ChangePasswordRequest;
import com.linyi.phsm.domain.rag.model.user.request.UserCreateRequest;
import com.linyi.phsm.domain.rag.model.user.request.UserPageRequest;
import com.linyi.phsm.domain.rag.model.user.request.UserUpdateRequest;
import com.linyi.phsm.domain.rag.model.user.vo.UserVO;

public interface UserService {

    /**
     * 分页查询用户列表
     */
    IPage<UserVO> pageQuery(UserPageRequest requestParam);

    /**
     * 创建用户
     */
    String create(UserCreateRequest requestParam);

    /**
     * 更新用户
     */
    void update(String id, UserUpdateRequest requestParam);

    /**
     * 删除用户
     */
    void delete(String id);

    /**
     * 修改当前用户密码
     */
    void changePassword(ChangePasswordRequest requestParam);
}
