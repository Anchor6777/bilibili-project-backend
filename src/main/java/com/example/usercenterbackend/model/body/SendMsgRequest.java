package com.example.usercenterbackend.model.body;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SendMsgRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7908626302687263503L;
    public Long sender_id;
    public Long receiver_id;
    public String content;
}
