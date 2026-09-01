package com.example.usercenterbackend.service;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.model.domain.ChatRecord;
import com.example.usercenterbackend.model.domain.NewMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 15927
* @description 针对表【new_msg(b站消息页——聊天信息表)】的数据库操作Service
* @createDate 2026-06-29 19:16:12
*/
public interface NewMsgService extends IService<NewMsg> {
    /**
     * 获取最近的聊天记录
     * @param id 用户id
     */
    BaseResponse<List<ChatRecord>> getRecentChatRecords(Long id);

    /**
     * 获取和单个用户的聊天记录
     */
    BaseResponse<ChatRecord> getOneChatRecord(Long uid,Long anotherId);

    /**
     * 发送消息
     * @param newMsg 消息实体
     * @return 保存后的消息（包含主键、创建时间等数据）
     */
    BaseResponse<NewMsg> sendMessage(NewMsg newMsg) throws Exception;
}
