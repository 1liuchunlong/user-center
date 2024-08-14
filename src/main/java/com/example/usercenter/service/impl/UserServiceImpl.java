package com.example.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.mapper.UserMapper;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author chunchun
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-08-11 20:20:01
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;
    
    private static final String SALT = "chunchun";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        if (userAccount.length() < 4){
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }
        //2. 账号不能包含特殊字符   应该先校验  再查询数据库
        String validPattern = "[^a-zA-Z0-9]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
           return -1;
        }
        // 密码和重复密码相同
        if (!userPassword.equals(checkPassword)){
            return -1;
        }
        //3. 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        System.out.println();
        // mybatis 自带的count方法 long count = this.count(queryWrapper);
        if (count > 0){
            return -1;
        }
        // 4 密码加密存储 加密存入数据库
       
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //5. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        // mybatis-plus提供的save方法
        boolean saveResult = this.save(user);
//        int insert = userMapper.insert(user);
        if (!saveResult){
           return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount 账号
     * @param userPassword 密码
     * @param request
     * @return 脱敏用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // todo 需要改成统一的返回类 Result
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if(userAccount.length() < 4 || userPassword.length() < 8 ){
            return null;
        }
        //2. 账号不能包含特殊字符   应该先校验  再查询数据库
        String validPattern = "[^a-zA-Z0-9]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return null;
        }
        // 4 密码加密  与数据库中密码比对
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",encryptPassword);
        User user = userMapper.selectOne((queryWrapper));
        if (user == null){
            log.info("user login failed, userAccount can not match userPassword");
            return null;
        }
        User satetyUser = getSafetyUser(user);
        //记录用户信息的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,satetyUser);
        return satetyUser;
    }

    /**
     * 脱敏方法
     * @param user
     * @return
     */
    public User getSafetyUser(User user){
        // 记录用户的登录志
        // 用户信息脱敏
        User satetyUser = new User();
        satetyUser.setId(user.getId());
        satetyUser.setUsername(user.getUsername());
        satetyUser.setUserAccount(user.getUserAccount());
        satetyUser.setAvatarUrl(user.getAvatarUrl());
        satetyUser.setGender(user.getGender());
        satetyUser.setPhone(user.getPhone());
        satetyUser.setEmail(user.getEmail());
        satetyUser.setUserStatus(user.getUserStatus());
        satetyUser.setCreateTime(user.getCreateTime());
        return satetyUser;
    }
}




