package com.example.usercenterbackend.service;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.model.body.VideoCommentResponse;
import com.example.usercenterbackend.model.domain.VideoComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 15927
* @description 针对表【video_comment(视频评论表)】的数据库操作Service
* @createDate 2026-07-07 10:44:25
*/
public interface VideoCommentService extends IService<VideoComment> {
    /**
     * 获取视频所有评论
     * @param videoID 视频id
     * @return 评论信息（包含二级评论）
     */
    BaseResponse<List<VideoCommentResponse>> getAllComments(Long videoID);

    /**
     * 发送评论
     * @param uid 用户id
     * @param vid 视频id
     * @param pid 父评论id
     * @param reply_to_uid 回复的人的id
     * @param content 评论内容
     * @return 新添加的评论
     */
    BaseResponse<VideoCommentResponse> sendComment(Long uid,Long vid,Long pid,Long reply_to_uid,String content);
}
