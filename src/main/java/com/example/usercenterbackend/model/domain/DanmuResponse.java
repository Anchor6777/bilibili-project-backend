package com.example.usercenterbackend.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class DanmuResponse {
    public int code;
    public List<Object> data;
}
