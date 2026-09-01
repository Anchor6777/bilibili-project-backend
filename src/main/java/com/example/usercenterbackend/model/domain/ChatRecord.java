package com.example.usercenterbackend.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChatRecord {
    public User user;
    public List<NewMsg> msgList;
}
