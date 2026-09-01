package com.example.usercenterbackend.model.body;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SendCommentRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6429763682409327952L;
    public Long uid;
    public Long video_id;
    public Long parent_id;
    public Long reply_to_uid;
    public String content;
}
