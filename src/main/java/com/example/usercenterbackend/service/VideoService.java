package com.example.usercenterbackend.service;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.PageResponse;
import com.example.usercenterbackend.model.domain.NamedVideo;
import com.example.usercenterbackend.model.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 15927
* @description 针对表【video(视频信息表)】的数据库操作Service
* @createDate 2026-07-05 11:29:44
*/
public interface VideoService extends IService<Video> {

    BaseResponse<Video> getVideoByVideoId(Long id);

    BaseResponse<PageResponse<NamedVideo>> getRecommendVideoList(Long pageNum, Long pageSize);

    BaseResponse<List<Video>> getVideoListByUserID(Long uid,Long count);
}
