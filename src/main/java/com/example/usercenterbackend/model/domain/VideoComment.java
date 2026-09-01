package com.example.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 视频评论表
 * @TableName video_comment
 */
@TableName(value ="video_comment")
@Data
public class VideoComment {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 视频ID
     */
    private Long video_id;

    /**
     * 0表示一级评论，非0表示二级回复，值为父评论ID）
     */
    private Long parent_id;

    /**
     * 二级回复时，被@的用户ID
     */
    private Long reply_to_uid;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 用户昵称
     * 该字段数据库中不存在，用于接收联表查询结果
     */
    @TableField(exist = false)
    private String username;

    /**
     * 用户头像url
     * 该字段数据库中不存在，用于接收联表查询结果
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * 
     */
    private Date create_time;

    /**
     * 
     */
    private Integer is_delete;
}