package com.example.usercenter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserCenterApplicationTests {
    final String SALT = "chunchun";

    @Test
    public void testdDigest(){
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + "mypassword").getBytes());
        System.out.println(encryptPassword);
    }

}
