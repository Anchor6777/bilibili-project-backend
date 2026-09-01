package com.example.usercenterbackend.model.body;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 4469412640735047607L;
    private String username;
    private String password;
    private String checkPassword;

}
