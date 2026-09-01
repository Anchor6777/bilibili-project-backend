package com.example.usercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * b站消息页——聊天信息表
 * @TableName new_msg
 */
@TableName(value ="new_msg")
@Data
public class NewMsg {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送信息的用户的id
     */
    private Long sender_id;

    /**
     * 接收信息的用户的id
     */
    private Long receiver_id;

    /**
     * 消息内容
     */
    private String content;

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