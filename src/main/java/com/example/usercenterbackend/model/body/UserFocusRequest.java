package com.example.usercenterbackend.model.body;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserFocusRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1249359893375101072L;
    public Long id;
    public Long toId;
}
