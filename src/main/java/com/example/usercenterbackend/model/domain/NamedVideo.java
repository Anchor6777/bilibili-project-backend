package com.example.usercenterbackend.model.domain;

import lombok.Data;

@Data
public class NamedVideo {
    private Video video;
    private String username;
}
