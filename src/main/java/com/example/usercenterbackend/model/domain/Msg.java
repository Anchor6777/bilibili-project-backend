package com.example.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 聊天信息表
 * @TableName msg
 */
@TableName(value ="msg")
@Data
public class Msg {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送信息的用户的id
     */
    private Long userID;

    /**
     * 用户头像url
     */
    private String avatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;
}