package com.example.usercenterbackend.model.body;

import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.model.domain.VideoComment;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class VideoCommentResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -339693131568274349L;
    public VideoComment comment;
    public List<VideoComment> childComments;
}
