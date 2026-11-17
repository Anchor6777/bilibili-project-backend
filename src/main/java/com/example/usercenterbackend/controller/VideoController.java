package com.example.usercenterbackend.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenterbackend.commons.*;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.body.SendCommentRequest;
import com.example.usercenterbackend.model.body.SendDanmuRequest;
import com.example.usercenterbackend.model.body.VideoCommentResponse;
import com.example.usercenterbackend.model.domain.Danmu;
import com.example.usercenterbackend.model.domain.DanmuResponse;
import com.example.usercenterbackend.model.domain.NamedVideo;
import com.example.usercenterbackend.model.domain.Video;
import com.example.usercenterbackend.service.DanmuService;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.service.VideoCommentService;
import com.example.usercenterbackend.service.VideoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Resource
    private VideoService videoService;

    @Resource
    private DanmuService danmuService;

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private UserService userService;

    // 获取首页推荐视频列表
    @GetMapping("/video_list")
    public BaseResponse<PageResponse<NamedVideo>> getRecommendVideoList(@RequestParam Long pageNum, @RequestParam Long pageSize, HttpServletRequest request) {
        return videoService.getRecommendVideoList(pageNum, pageSize);
    }

    // 根据视频vid获取视频信息
    @GetMapping("/one")
    public BaseResponse<Video> getVideoByVideoID(@RequestParam Long videoID, HttpServletRequest request) {
        return videoService.getVideoByVideoId(videoID);
    }

    // 获取用户{@uid}发布的视频列表
    @GetMapping("/user_video_list")
    public BaseResponse<List<Video>> getVideoListByUserID(@RequestParam Long uid, @RequestParam Long count, HttpServletRequest request) {
        return videoService.getVideoListByUserID(uid, count);
    }

    @GetMapping("/get_all_comment")
    public BaseResponse<List<VideoCommentResponse>> getAllComments(@RequestParam Long videoID, HttpServletRequest request) {
        return videoCommentService.getAllComments(videoID);
    }

    @PostMapping("/send_comment")
    public BaseResponse<VideoCommentResponse> sendComment(@RequestBody SendCommentRequest commentRequest, HttpServletRequest request) {
        if (!UserService.isLogin(request)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "登录后才能发送评论");
        }
        return videoCommentService.sendComment(commentRequest.uid, commentRequest.video_id, commentRequest.parent_id,
                commentRequest.reply_to_uid, commentRequest.content);
    }

    @GetMapping("/danmu/v3/")
    public DanmuResponse getDanmu(@RequestParam Long id, HttpServletRequest request) {
        DanmuResponse res = new DanmuResponse();
        res.code = 0;
        res.data = new ArrayList<>();

        QueryWrapper<Danmu> wrapper = new QueryWrapper<>();
        wrapper.eq("vid", id);
        List<Danmu> danmuList = danmuService.list(wrapper);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

        danmuList.forEach(danmu -> {
            Object[] obj = new Object[]{danmu.getTime(), danmu.getType(), danmu.getColor(), danmu.getAuthor_name(), danmu.getContent(), sdf.format(danmu.getCreateTime())};
            res.data.add(obj);
        });
        return res;
    }

    @PostMapping("/danmu/v3/")
    public BaseResponse<Boolean> sendDanmu(@RequestBody SendDanmuRequest danmuRequest, HttpServletRequest request) {
//        DPlayer发送弹幕的请求不支持设置cookie，只支持通过token登录。先设置不登录也可以发送弹幕
//        if (request.getSession().getAttribute(Constant.USER_LOGIN_STATUS) == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN, "登录后才能发送弹幕");
//        }
        Danmu danmu = new Danmu();
        danmu.setVid(danmuRequest.id);
        danmu.setAuthor_name(danmuRequest.author);
        danmu.setTime(danmuRequest.time);
        danmu.setContent(danmuRequest.text);
        danmu.setColor(danmuRequest.color);
        danmu.setType(danmuRequest.type);
        System.out.println("===" + JSON.toJSONString(danmuRequest));

        return BaseResponse.success(danmuService.save(danmu));
    }
}
