package com.example.usercenterbackend.controller;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.model.domain.Msg;
import com.example.usercenterbackend.service.MsgService;
import com.example.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource
    private MsgService msgService;
    @Resource
    private UserService userService;

    @GetMapping("/recent")
    public BaseResponse<List<Msg>> getRecentMsgList(int count) {

        return msgService.getRecentMsg(count);
    }
}
