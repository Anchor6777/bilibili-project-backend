package com.example.usercenterbackend.model.body;

import com.example.usercenterbackend.model.domain.User;
import lombok.Data;

import java.util.Map;

@Data
public class UserLoginResponse {
    User userInfo;
    String token;
    String refreshToken;
}
