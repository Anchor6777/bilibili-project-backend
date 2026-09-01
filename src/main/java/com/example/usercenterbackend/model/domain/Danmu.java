package com.example.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 弹幕表
 * @TableName danmu
 */
@TableName(value ="danmu")
@Data
public class Danmu {
    /**
     * 弹幕id，主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 视频id
     */
    private Long vid;

    /**
     * 发布弹幕用户的昵称
     */
    private String author_name;

    /**
     * 在视频中出现的时间
     */
    private Double time;

    /**
     * 弹幕内容
     */
    private String content;

    /**
     * 弹幕颜色
     */
    private Long color;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Integer isDelete;

    /**
     * 弹幕类型。0-普通,1-顶部,2-底部
     */
    private Integer type;
}