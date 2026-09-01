package com.example.usercenterbackend.websocket;

import com.alibaba.fastjson2.JSON;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.domain.ChatMessage;
import com.example.usercenterbackend.model.domain.Msg;
import com.example.usercenterbackend.model.domain.TypedMessage;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.service.MsgService;
import com.example.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler extends TextWebSocketHandler {
    public static final Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    @Resource
    private MsgService msgService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Msg newMsg = JSON.parseObject(message.getPayload(), Msg.class);
        // 保存新消息到数据库
        msgService.saveNewMsg(newMsg);
        System.out.println("new msg: " + JSON.toJSONString(newMsg));

        TypedMessage typedMessage = new TypedMessage();
        typedMessage.type = Constant.CHAT_MSG;
        typedMessage.data = newMsg;
        TextMessage msg = new TextMessage(JSON.toJSONString(typedMessage));

        // 广播给所有在线用户
        for (String sessionId : clients.keySet()) {
            clients.get(sessionId).sendMessage(msg);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connection established: " + session.getId());
        System.out.println(session.getAttributes());
        clients.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("connection closed: " + session.getId());
        clients.remove(session.getId());
    }

    public void notifyOnlineInfoChange() throws Exception {
        TypedMessage typedMessage = new TypedMessage();
        typedMessage.type = Constant.UPDATE_ONLINE_INFO;
        TextMessage msg = new TextMessage(JSON.toJSONString(typedMessage));
        // 广播给所有在线用户
        for (String sessionId : clients.keySet()) {
            clients.get(sessionId).sendMessage(msg);
        }
    }
}
