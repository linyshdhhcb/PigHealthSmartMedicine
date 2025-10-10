package com.linyi.pig.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.entity.User;
import com.linyi.pig.entity.vo.user.*;

import com.linyi.pig.service.UserService;
import com.linyi.pig.config.EmailConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.linyi.pig.constant.MedicalConstants.USER_REGISTER_CODE;

/**
 * @Author: linyi
 * @Date: 2025-02-25 17:38:38
 * @ClassName: UserController
 * @Version: 1.0
 * @Description: 用户 控制层
 */

@Tag(name = "用户管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailConfig emailConfig;

    /**
     * 分页查询用户
     *
     * @param userQueryVo 分页查询实体
     * @return Result<PageResult<User>> 返回分页数据
     */
    @Operation(summary = "分页查询用户")
    @PostMapping("/userPage")
    public Result<PageResult<User>> userPage(@RequestBody UserQueryVo userQueryVo) {
        return Result.success(userService.userPage(userQueryVo));
    }

    /**
     * 新增用户
     *
     * @param userAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增用户")
    @PostMapping("/userAdd")
    public Result<Boolean> userAdd(@RequestBody UserAddVo userAddVo) {
        return Result.success(userService.userAdd(userAddVo));
    }

    /**
     * 根据主键ID删除用户
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除用户")
    @DeleteMapping("userDelete")
    public Result<Boolean> userDelete(@RequestParam Serializable id) {
        return Result.success(userService.removeById(id));
    }

    /**
     * 根据主键ID批量删除用户
     *
     * @param ids 主键id集合
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID批量删除用户")
    @DeleteMapping("userListDelete")
    public Result<Boolean> userListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    /**
     * 根据主键ID修改用户
     *
     * @param userUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改用户")
    @PutMapping("userUpdate")
    public Result<Boolean> userUpdate(@RequestBody UserUpdateVo userUpdateVo) {
        return Result.success(userService.userUpdate(userUpdateVo));
    }

    /**
     * 根据主键ID查询用户
     *
     * @param id 主键id
     * @return Result<User> 返回用户
     */
    @Operation(summary = "根据主键ID查询用户")
    @GetMapping("/getInfo")
    public Result<User> userUpdate(@RequestParam Serializable id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 登录校验
     * 
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "登录校验")
    @GetMapping("/checkLogin")
    public Result<Boolean> login() {
        return Result.success(StpUtil.isLogin());
    }

    /**
     * 注册
     * 
     * @param registerVo 注册实体
     * @return Result<User> 返回用户
     */
    @Operation(summary = "注册")
    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterVo registerVo) {

        return Result.success("注册成功", userService.register(registerVo));
    }

    /**
     * 账号密码登录
     * 
     * @param userAccount 账号
     * @param password    密码
     * @return Result<LoginResponse> 返回登录结果
     */
    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String userAccount, @RequestParam String password) {
        Boolean loginResult = userService.login(userAccount, password);
        if (loginResult) {
            Map<String, Object> response = new HashMap<>();
            response.put("loginId", StpUtil.getLoginId());
            response.put("tokenValue", StpUtil.getTokenValue());
            return Result.success(response);
        }
        return Result.error("登录失败");
    }

    /**
     * 邮箱密码登录
     * 
     * @param email    邮箱
     * @param password 密码
     * @return
     */
    @Operation(summary = "邮箱密码登录")
    @PostMapping("/emailLogin")
    public Result<Boolean> emailLogin(@RequestParam String email, @RequestParam String password) {
        return Result.success(userService.emailLogin(email, password));
    }

    /**
     * 退出登录
     * 
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }

    /**
     * 发送邮箱验证码
     * 
     * @param email
     * @return Result<CodeVo> 返回codevo
     */
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/sendEmailCode")
    public Result<CodeVo> sendEmailCode(@RequestParam String email) {
        if (Optional.ofNullable(email).isEmpty()) {
            return Result.error("邮箱不可为空");
        }
        String code = emailConfig.sendEmailCode(email);
        String key = RandomStringUtils.randomAlphanumeric(15);
        log.info("验证码：{},key:{}", code, key);
        CodeVo codeVo = CodeVo.builder().code(code).key(key).build();
        redisTemplate.opsForValue().set(USER_REGISTER_CODE + email + key, code, 30, TimeUnit.MINUTES);
        return Result.success(codeVo);
    }

    /**
     * 修改个人信息
     * 
     * @param meUpdateVo
     * @return
     */
    @Operation(summary = "修改个人信息")
    @PutMapping("/saveProfile")
    public Result<Boolean> saveProfile(@RequestBody MeUpdateVo meUpdateVo) {
        return Result.success(userService.saveProfile(meUpdateVo));
    }

    /**
     * 修改密码
     * 
     * @param oldPass 旧密码
     * @param newPass 新密码
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "修改密码")
    @PostMapping("/savePassword")
    public Result<Boolean> savePassword(@RequestParam String oldPass, @RequestParam String newPass) {
        return Result.success(userService.savePassword(oldPass, newPass));
    }

}
