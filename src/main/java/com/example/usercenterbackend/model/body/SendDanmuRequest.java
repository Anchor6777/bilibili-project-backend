package com.example.usercenterbackend.model.body;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SendDanmuRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2965184866632097768L;
    public Long id;
    public String author;
    public Double time;
    public String text;
    public Long color;
    public Integer type;
}
