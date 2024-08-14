package com.example.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -5980506817076757540L;
    private String userAccount;
    private String userPassword;
}
