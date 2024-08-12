package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 用户服务测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("chunchun");
        user.setUserAccount("123");
        user.setAvatarUrl("https://www.code-nav.cn/course/1790943469757837313");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void userRegister() {
        //1 密码不能为空
        String userAccount = "chunchun1";
        String userPassword = "";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        // 2 账号不能小于4 位
        userAccount = "ch";
        userPassword = "12345678";
        checkPassword = "12345678";
        Assertions.assertEquals(-1,result);
        // 3. 密码不能小于8位
        userAccount = "chunchun1";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        // 4. 特殊字符
        userAccount = "c cchun";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        // 5. 密码和重复密码一致
        userAccount = "chuncchun1";
        userPassword = "12345678";
        checkPassword = "12345679";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        // 6. 当前用户已存在
        userAccount = "chunchun";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        // 7. 成功插入数据库
        userAccount = "chuncchun01";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }

    @Test
    public void doLogin() {
        String userAccount = "chunchun";
        String userPassword = "12345678";
        HttpServletRequest request =null ;
        User user = userService.doLogin(userAccount, userPassword, request);
        Assertions.assertNotNull(user);

    }
}