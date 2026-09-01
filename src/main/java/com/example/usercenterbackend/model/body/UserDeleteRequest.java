package com.example.usercenterbackend.model.body;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserDeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 5040557236354946340L;
    private Long id;
}
