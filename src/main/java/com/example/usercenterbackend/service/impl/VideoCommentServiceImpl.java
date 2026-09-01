package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.model.body.VideoCommentResponse;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.model.domain.VideoComment;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.service.VideoCommentService;
import com.example.usercenterbackend.mapper.VideoCommentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 15927
* @description 针对表【video_comment(视频评论表)】的数据库操作Service实现
* @createDate 2026-07-07 10:44:25
*/
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService{

    @Resource
    private UserService userService;

    @Override
    public BaseResponse<List<VideoCommentResponse>> getAllComments(Long videoID) {
        List<VideoComment> comments = this.baseMapper.selectCommentsByVideoId(videoID);

        List<VideoCommentResponse> resList = new ArrayList<>();
        for (VideoComment parent : comments) {
            List<VideoComment> childComments = this.baseMapper.selectChildComments(parent.getId());

            VideoCommentResponse res = new VideoCommentResponse();
            res.comment = parent;
            res.childComments = childComments;
            resList.add(res);
        }
        return BaseResponse.success(resList);
    }

    @Override
    public BaseResponse<VideoCommentResponse> sendComment(Long uid, Long vid, Long pid, Long reply_to_uid, String content) {
        VideoComment comment = new VideoComment();
        comment.setUid(uid);
        comment.setVideo_id(vid);
        comment.setParent_id(pid);
        comment.setReply_to_uid(reply_to_uid);
        comment.setContent(content);
        if (this.save(comment)) {
            // 获取存储后的记录
            QueryWrapper<VideoComment> wrapper = new QueryWrapper<>();
            wrapper.eq("id", comment.getId());
            VideoComment storedComment = this.getOne(wrapper);

            // 查询用户相关信息
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", storedComment.getUid());
            User user = userService.getOne(queryWrapper);
            storedComment.setUsername(user.getUsername());
            storedComment.setAvatar(user.getAvatar());

            // 包装返回
            VideoCommentResponse res = new VideoCommentResponse();
            res.comment = storedComment;
            res.childComments = new ArrayList<>();
            return BaseResponse.success(res);
        }else {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}




