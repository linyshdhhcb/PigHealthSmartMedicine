package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.User;
import com.linyi.pig.entity.vo.user.*;

/**
* @Author: linyi
* @Date: 2025-02-25 17:38:38
* @ClassName: UserService
* @Version: 1.0
* @Description: 用户 服务层
*/
public interface UserService extends IService<User> {
    /**
     * 分页查询
     *
     * @param userQueryVo 分页查询实体
     * @return PageResult<User>
     */
    PageResult<User> userPage(UserQueryVo userQueryVo);

    /**
     * 新增
     *
     * @param userAddVo 新增实体
     * @return Boolean
     */
    Boolean userAdd(UserAddVo userAddVo);

    /**
     * 修改
     *
     * @param userUpdateVo 修改实体
     * @return Boolean
     */
    Boolean userUpdate(UserUpdateVo userUpdateVo);

    /**
     * 注册
     * @param registerVo 注册实体
     * @return User
     */
    User register(RegisterVo registerVo);

    /**
     * 邮箱密码登录
     * @param email 邮箱
     * @param password 密码
     * @return
     */
    Boolean emailLogin(String email, String password);

    /**
     * 账号密码登录
     * @param userAccount 账号
     * @param password 密码
     * @return Result<Boolean> 返回结果(true/false)
     */
    Boolean login(String userAccount, String password);

    /**
     * 修改个人信息
     * @param meUpdateVo
     * @return
     */
    Boolean saveProfile(MeUpdateVo meUpdateVo);

    /**
     * 修改密码
     * @param oldPass 旧密码
     * @param newPass 新密码
     * @return Result<Boolean> 返回结果(true/false)
     */
    Boolean savePassword(String oldPass, String newPass);
}
