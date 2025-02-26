package com.linyi.pig.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.User;
import com.linyi.pig.entity.vo.user.*;
import com.linyi.pig.exception.LinyiException;
import com.linyi.pig.mapper.UserMapper;
import com.linyi.pig.service.UserService;
import com.linyi.pig.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.linyi.pig.constant.MedicalConstants.USER_REGISTER_CODE;

/**
 * @Author: linyi
 * @Date: 2025-02-25 17:38:38
 * @ClassName: UserServiceImpl
 * @Version: 1.0
 * @Description: 用户 服务实现层
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult<User> userPage(UserQueryVo userQueryVo) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<User> page = new Page<>(userQueryVo.getPageNum(), userQueryVo.getPageSize());
        //查询数据
        Page<User> pageNew = userMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), userQueryVo.getPageNum(), userQueryVo.getPageSize());
    }

    @Override
    public Boolean userAdd(UserAddVo userAddVo) {
        //创建实体对象
        User user = new User();
        //复制属性
        BeanUtils.copyProperties(userAddVo, user);
        //插入数据
        return userMapper.insert(user) > 0 ? true : false;
    }

    @Override
    public Boolean userUpdate(UserUpdateVo userUpdateVo) {
        //根据ID查询数据
        User byId = this.getById(userUpdateVo.getId());
        //判断数据是否存在
        if (Optional.ofNullable(byId).isEmpty()) {
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(userUpdateVo, byId);
        //修改数据
        return userMapper.updateById(byId) > 0 ? true : false;
    }

    @Override
    public User register(RegisterVo registerVo) {
        if (!registerVo.getUserPwd().equals(registerVo.getUserPwds())) {
            throw new LinyiException("两次密码不一致");
        }
        String code = (String) redisTemplate.opsForValue().get(USER_REGISTER_CODE +registerVo.getUserEmail()+ registerVo.getKey());
        if (!registerVo.getCode().equals(code)){
            throw new LinyiException("验证码错误");
        }
        User user = new User();
        BeanUtils.copyProperties(registerVo, user);
        user.setRoleStatus(0);
        user.setImgPath("https://tse4-mm.cn.bing.net/th/id/OIP-C.Xd88RmKtrH3ORAfPnL3gwAAAAA?w=168&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7");
        //密码加密
        String encodedPassword = PasswordUtil.encodePassword(registerVo.getUserPwd());
        user.setUserPwd(encodedPassword);
        return userMapper.insert(user) > 0 ? user : null;

    }

    @Override
    public Boolean emailLogin(String email, String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserEmail, email));
        if (Optional.ofNullable(user).isEmpty()){
            throw new LinyiException("用户不存在");
        }
        if (!PasswordUtil.matchesPassword(password, user.getUserPwd())){
            throw new LinyiException("密码错误");
        }
        StpUtil.login(user.getId());
        return true;
    }

    @Override
    public Boolean login(String userAccount, String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));
        if (Optional.ofNullable(user).isEmpty()){
            throw new LinyiException("用户不存在");
        }
        if (!PasswordUtil.matchesPassword(password, user.getUserPwd())){
            throw new LinyiException("密码错误");
        }
        StpUtil.login(user.getId());
        return true;
    }

    @Override
    public Boolean saveProfile(MeUpdateVo meUpdateVo) {
        User byId = this.getById(meUpdateVo.getId());
        if (Optional.ofNullable(byId).isEmpty()){
            throw new LinyiException("用户不存在");
        }
        //复制属性
        BeanUtils.copyProperties(meUpdateVo, byId);
        return userMapper.updateById(byId) > 0 ? true : false;
    }

    @Override
    public Boolean savePassword(String oldPass, String newPass) {
        Integer loginId = Integer.valueOf(StpUtil.getLoginId().toString());
        User byId = this.getById(loginId);
        if (!PasswordUtil.matchesPassword(oldPass, byId.getUserPwd())){
            throw new LinyiException("密码错误");
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, StpUtil.getLoginId()));
        user.setUserPwd(PasswordUtil.encodePassword(newPass));
        return userMapper.updateById(user) > 0 ? true : false;
    }


}
