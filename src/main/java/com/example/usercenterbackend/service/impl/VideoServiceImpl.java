package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.commons.PageResponse;
import com.example.usercenterbackend.model.domain.NamedVideo;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.model.domain.Video;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.service.VideoService;
import com.example.usercenterbackend.mapper.VideoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    public BaseResponse<PageResponse<NamedVideo>> getRecommendVideoList(Long pageNum, Long pageSize) {
        long current = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        long size = (pageSize == null || pageSize < 1) ? 12 : Math.min(pageSize, 50); // pageSize默认12，封顶50

        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("updateTime").orderByDesc("id"); // updateTime 可能重复，补 id 做第二排序键，保证翻页顺序稳定

        IPage<Video> page = this.page(new Page<>(current, size), wrapper);
        List<Video> videos = page.getRecords();

        Map<Long, String> usernameMap = new HashMap<>();
        if (!videos.isEmpty()){
            Set<Long> uids = videos.stream().map(Video::getUid).filter(Objects::nonNull).collect(Collectors.toSet());
            usernameMap = userService.listByIds(uids).stream().collect(Collectors.toMap(User::getId, User::getUsername,(a, b) -> a));
        }

        Map<Long, String> finalMap = usernameMap;
        List<NamedVideo> records = videos.stream().map(v -> {
            NamedVideo nv = new NamedVideo();
            nv.setVideo(v);
            // 作者被逻辑删除时 listByIds 查不到，别 NPE（原来 user.getUsername() 就会炸）
            nv.setUsername(finalMap.getOrDefault(v.getUid(), "已注销用户"));
            return nv;
        }).toList();

        PageResponse<NamedVideo> res = new PageResponse<>();
        res.setRecords(records);
        res.setTotal(page.getTotal());
        res.setPageNum(page.getCurrent());
        res.setPageSize(page.getSize());
        res.setHasMore(page.getCurrent() * page.getSize() <= page.getTotal());

        return  BaseResponse.success(res);

    }

//    @Override
//    public BaseResponse<List<NamedVideo>> getRecommendVideoList(Long count) {
//        QueryWrapper<Video> wrapper = new QueryWrapper<>();
//        wrapper.orderByDesc("updateTime")
//                .last("limit " + count);
//
//        List<Video> result = this.baseMapper.selectList(wrapper);
//        List<NamedVideo> res =  new ArrayList<>();
//        result.forEach(item -> {
//            NamedVideo nv = new NamedVideo();
//
//            QueryWrapper<User> wrapper2 = new QueryWrapper<>();
//            wrapper2.eq("id", item.getUid());
//            User user = userService.getOne(wrapper2);
//            nv.setUsername(user.getUsername());
//            nv.setVideo(item);
//            res.add(nv);
//        });
//        return BaseResponse.success(res);
//    }

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




