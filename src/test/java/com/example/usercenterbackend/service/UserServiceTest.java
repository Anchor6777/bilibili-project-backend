package com.example.usercenterbackend.service;

import com.example.usercenterbackend.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testAddUser(){
        User user = userService.getById(1);
        System.out.println(user.getUsername());
    }
}