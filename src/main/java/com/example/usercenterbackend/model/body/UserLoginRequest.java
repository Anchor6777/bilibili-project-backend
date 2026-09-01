package com.example.usercenterbackend.model.body;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2736468666950092684L;
    String username;
    String password;
}
