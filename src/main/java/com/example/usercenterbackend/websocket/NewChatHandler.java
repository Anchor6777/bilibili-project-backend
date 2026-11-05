package com.example.usercenterbackend.websocket;

import com.alibaba.fastjson2.JSON;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.domain.NewMsg;
import com.example.usercenterbackend.model.domain.TypedMessage;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.service.NewMsgService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NewChatHandler extends TextWebSocketHandler {
    // 用户id -> WebSocketSession实例
    public static final Map<Long, WebSocketSession> clients = new ConcurrentHashMap<>();

    @Resource
    private NewMsgService msgService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        TypedMessage tmsg = JSON.parseObject(message.getPayload(), TypedMessage.class);
        switch (tmsg.type) {
            case "chat_msg":
                NewMsg msg = JSON.parseObject(JSON.toJSONString(tmsg.data), NewMsg.class);
                msgService.sendMessage(msg);
                break;
            case "heartbeat":
                TypedMessage hbmsg = new TypedMessage();
                hbmsg.type = "heartbeat";
                session.sendMessage(new TextMessage(JSON.toJSONString(hbmsg)));
                break;
            default:
                break;
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long uid = (Long) session.getAttributes().get(Constant.USER_LOGIN_STATUS);
        clients.put(uid, session);
        System.out.println("id为"+uid+"的用户，已连接ws");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long uid = (Long) session.getAttributes().get(Constant.USER_LOGIN_STATUS);
        clients.remove(uid);
        System.out.println("id为"+uid+"的用户，已断开连接");
    }
}
