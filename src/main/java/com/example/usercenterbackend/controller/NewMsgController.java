package com.example.usercenterbackend.controller;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.domain.ChatRecord;
import com.example.usercenterbackend.model.domain.NewMsg;
import com.example.usercenterbackend.model.body.SendMsgRequest;
import com.example.usercenterbackend.service.NewMsgService;
import com.example.usercenterbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/new_msg")
@Tag(name = "实时聊天室", description = "...")
public class NewMsgController {
    @Resource
    private NewMsgService msgService;

    @PostMapping("/send")
    public BaseResponse<NewMsg> sendNewMsg(@RequestBody SendMsgRequest msgInfo, HttpServletRequest request) throws Exception {
        if(!UserService.isLogin(request)){
            throw new BusinessException(ErrorCode.NOT_LOGIN,"登录后才能发送消息");
        }
        NewMsg newMsg = new NewMsg();
        newMsg.setContent(msgInfo.getContent());
        newMsg.setSender_id(msgInfo.getSender_id());
        newMsg.setReceiver_id(msgInfo.getReceiver_id());
        return msgService.sendMessage(newMsg);
    }

    @GetMapping("/recent")
    public BaseResponse<List<ChatRecord>> getRecentChatRecords(Long id) {
        return msgService.getRecentChatRecords(id);
    }

    @GetMapping("/one")
    public BaseResponse<ChatRecord> getRecentOneChatRecord(Long uid,Long anotherId) {
        return msgService.getOneChatRecord(uid, anotherId);
    }
}
