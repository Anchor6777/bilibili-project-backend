package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.model.domain.NamedVideo;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.model.domain.Video;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.service.VideoService;
import com.example.usercenterbackend.mapper.VideoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 15927
 * @description 针对表【video(视频信息表)】的数据库操作Service实现
 * @createDate 2026-07-05 11:29:44
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {

    @Resource
    private UserService userService;

    @Override
    public BaseResponse<Video> getVideoByVideoId(Long id) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Video video = this.getOne(wrapper);
        if (video == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "视频不存在");
        }
        return BaseResponse.success(video);
    }

    @Override
    public BaseResponse<List<NamedVideo>> getRecommendVideoList(Long count) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("updateTime")
                .last("limit " + count);

        List<Video> result = this.baseMapper.selectList(wrapper);
        List<NamedVideo> res =  new ArrayList<>();
        result.forEach(item -> {
            NamedVideo nv = new NamedVideo();

            QueryWrapper<User> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("id", item.getUid());
            User user = userService.getOne(wrapper2);
            nv.setUsername(user.getUsername());
            nv.setVideo(item);
            res.add(nv);
        });
        return BaseResponse.success(res);
    }

    @Override
    public BaseResponse<List<Video>> getVideoListByUserID(Long uid,Long count) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid)
                .orderByDesc("updateTime")
                .last("limit " + count);
        List<Video> result = this.baseMapper.selectList(wrapper);
        return BaseResponse.success(result);
    }
}




