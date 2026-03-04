

package com.nageoffer.ai.ragent.user.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.StrUtil;
import com.nageoffer.ai.ragent.user.dao.entity.UserDO;
import com.nageoffer.ai.ragent.user.dao.mapper.UserMapper;
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
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (loginId == null) {
            return Collections.emptyList();
        }

        String loginIdStr = loginId.toString();
        if (!StrUtil.isNumeric(loginIdStr)) {
            return Collections.emptyList();
        }

        UserDO user = userMapper.selectById(Long.parseLong(loginIdStr));
        if (user == null) {
            return Collections.emptyList();
        }

        List<String> permissions = new java.util.ArrayList<>();

        // 根据系统角色分配权限
        if ("admin".equals(user.getRole())) {
            // 管理员拥有所有权限
            permissions.add("pig:*");
            permissions.add("case:*");
            permissions.add("disease:*");
            permissions.add("drug:*");
            permissions.add("article:*");
            permissions.add("farm:*");
            permissions.add("diagnosis:*");
            permissions.add("user:*");
            return permissions;
        }

        // 根据业务角色类型分配权限
        String roleType = user.getRoleType();
        if (StrUtil.isBlank(roleType)) {
            // 如果没有业务角色，给予基础权限
            permissions.add("disease:view");
            permissions.add("drug:view");
            permissions.add("article:view");
            return permissions;
        }

        switch (roleType) {
            case "ADMIN":
                // 业务管理员拥有所有权限
                permissions.add("pig:*");
                permissions.add("case:*");
                permissions.add("disease:*");
                permissions.add("drug:*");
                permissions.add("article:*");
                permissions.add("farm:*");
                permissions.add("diagnosis:*");
                permissions.add("user:*");
                break;

            case "VETERINARIAN":
                // 兽医权限
                permissions.add("pig:view");
                permissions.add("case:*");
                permissions.add("disease:view");
                permissions.add("drug:view");
                permissions.add("article:create");
                permissions.add("article:update");
                permissions.add("article:view");
                permissions.add("diagnosis:*");
                permissions.add("farm:view");
                break;

            case "FARMER":
                // 养殖户权限
                permissions.add("pig:view");
                permissions.add("pig:create");
                permissions.add("pig:update");
                permissions.add("pig:delete");
                permissions.add("case:view");
                permissions.add("case:create");
                permissions.add("disease:view");
                permissions.add("drug:view");
                permissions.add("article:view");
                permissions.add("diagnosis:view");
                permissions.add("farm:view");
                break;

            default:
                // 默认基础权限
                permissions.add("disease:view");
                permissions.add("drug:view");
                permissions.add("article:view");
                break;
        }

        return permissions;
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

        UserDO user = userMapper.selectById(Long.parseLong(loginIdStr));
        if (user == null) {
            return Collections.emptyList();
        }

        List<String> roles = new java.util.ArrayList<>();

        // 添加系统角色
        if (StrUtil.isNotBlank(user.getRole())) {
            roles.add(user.getRole());
        }

        // 添加业务角色（转换为小写）
        if (StrUtil.isNotBlank(user.getRoleType())) {
            roles.add(user.getRoleType().toLowerCase());
        }

        return roles;
    }
}
