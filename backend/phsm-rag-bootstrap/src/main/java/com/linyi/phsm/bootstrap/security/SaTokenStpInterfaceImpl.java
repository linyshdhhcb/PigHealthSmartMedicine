package com.linyi.phsm.bootstrap.security;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.StrUtil;
import com.linyi.phsm.domain.rag.model.user.UserDO;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Sa-Token 权限认证接口实现类
 * 用于实现 Sa-Token 框架的权限和角色验证逻辑
 */
@Component
@RequiredArgsConstructor
public class SaTokenStpInterfaceImpl implements StpInterface {

    /**
     * 用户数据访问层
     */
    private final UserMapper userMapper;

    /**
     * 获取用户权限列表
     *
     * @param loginId   登录用户ID
     * @param loginType 登录类型
     * @return 权限列表（当前实现返回空列表）
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.emptyList();
    }

    /**
     * 获取用户角色列表
     *
     * @param loginId   登录用户ID
     * @param loginType 登录类型
     * @return 角色列表，包含用户的角色信息
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (loginId == null) {
            return Collections.emptyList();
        }

        String loginIdStr = loginId.toString();
        if (!StrUtil.isNumeric(loginIdStr)) {
            return Collections.emptyList();
        }

        UserDO user = userMapper.selectById(loginIdStr);
        if (user == null || StrUtil.isBlank(user.getRole())) {
            return Collections.emptyList();
        }

        return List.of(user.getRole());
    }
}
