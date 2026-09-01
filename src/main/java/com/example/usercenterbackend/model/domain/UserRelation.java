package com.example.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 用户关注关系表
 * @TableName user_relation
 */
@TableName(value ="user_relation")
@Data
public class UserRelation {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关注者ID（粉丝）
     */
    private Long follower_id;

    /**
     * 被关注者ID（博主/用户）
     */
    private Long followee_id;

    /**
     * 状态：0-正常关注，1-取消关注，2-拉黑
     */
    private Integer status;

    /**
     * 关注时间
     */
    private Date create_time;

    /**
     * 最后更新时间
     */
    private Date update_time;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;
}