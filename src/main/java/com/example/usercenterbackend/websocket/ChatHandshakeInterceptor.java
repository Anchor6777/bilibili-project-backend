package com.example.usercenterbackend.websocket;

import com.example.usercenterbackend.constant.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;

@Component
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    public ChatHandshakeInterceptor() {
        // 从 HttpSession 复制属性到 WebSocketSession
        super(List.of(Constant.USER_LOGIN_STATUS));
    }
}
