package com.example.usercenterbackend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.domain.ChatRecord;
import com.example.usercenterbackend.model.domain.NewMsg;
import com.example.usercenterbackend.mapper.NewMsgMapper;
import com.example.usercenterbackend.model.domain.TypedMessage;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.service.NewMsgService;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.websocket.NewChatHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 15927
 * @description 针对表【new_msg(b站消息页——聊天信息表)】的数据库操作Service实现
 * @createDate 2026-06-29 19:16:12
 */
@Service
public class NewMsgServiceImpl extends ServiceImpl<NewMsgMapper, NewMsg>
        implements NewMsgService {

    @Resource
    private UserService userService;


    @Override
    public BaseResponse<List<ChatRecord>> getRecentChatRecords(Long id) {
        List<User> list = this.baseMapper.selectRecentChatUsers(id);
        List<ChatRecord> res = new ArrayList<>();
        for (User user : list) {
            QueryWrapper<NewMsg> wrapper = new QueryWrapper<>();
            wrapper.eq("receiver_id", user.getId()).and(w -> w.eq("sender_id", id))
                    .or()
                    .eq("receiver_id", id).and(w -> w.eq("sender_id", user.getId()));
            List<NewMsg> msgList = this.list(wrapper);
            ChatRecord record = new ChatRecord();
            record.user = user;
            record.msgList = msgList;
            res.add(record);
        }
        return BaseResponse.success(res);
    }

    @Override
    public BaseResponse<ChatRecord> getOneChatRecord(Long uid, Long anotherId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",anotherId);
        User user = userService.getOne(queryWrapper);

        QueryWrapper<NewMsg> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver_id", anotherId).and(w -> w.eq("sender_id", uid))
                .or()
                .eq("receiver_id", uid).and(w -> w.eq("sender_id", anotherId));
        List<NewMsg> msgList = this.list(wrapper);
        ChatRecord record = new ChatRecord();
        record.user = user;
        record.msgList = msgList;

        return BaseResponse.success(record);
    }

    @Override
    public BaseResponse<NewMsg> sendMessage(NewMsg newMsg) throws Exception {
        // 校验参数是否为空
        if (newMsg.getSender_id() == null || newMsg.getReceiver_id() == null || newMsg.getContent() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数存在空值");
        }
        // 1.保存新消息
        boolean res = this.save(newMsg);
        if (!res) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "数据保存失败");
        }
        // 2.通过ws，分发消息
        WebSocketSession receiverSession = NewChatHandler.clients.get(newMsg.getReceiver_id());
        WebSocketSession senderSession = NewChatHandler.clients.get(newMsg.getSender_id());
        TypedMessage tm = new TypedMessage();
        tm.type = Constant.CHAT_MSG;
        tm.data = newMsg;
        if (receiverSession != null) {
            receiverSession.sendMessage(new TextMessage(JSON.toJSONString(tm)));
        }
        if (senderSession != null) {
            senderSession.sendMessage(new TextMessage(JSON.toJSONString(tm)));
        }
        return BaseResponse.success(newMsg);
    }
}




