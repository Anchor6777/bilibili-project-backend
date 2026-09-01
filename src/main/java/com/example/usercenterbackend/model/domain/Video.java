package com.example.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 视频信息表
 * @TableName video
 */
@TableName(value ="video")
@Data
public class Video {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 视频url
     */
    private String source_url;

    /**
     * 封面url
     */
    private String cover_url;

    /**
     * 播放量
     */
    private Long play_count;

    /**
     * 播放时长
     */
    private Integer play_time;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 标签
     */
    private String tags;

    /**
     * 发布视频用户的uid
     */
    private Long uid;

    /**
     * 视频简介
     */
    private String video_desc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除 0-正常
     */
    @TableLogic
    private Integer isDelete;
}