package com.example.usercenterbackend.websocket;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.commons.JwtUtils;
import com.example.usercenterbackend.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * 握手拦截器，在websocket握手之前，保存id—>session映射保存到{@link NewChatHandler#clients}中
 * 用于后面分发消息
 */
@Slf4j
@Component
public class ChatHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams().getFirst("token");
        if (token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        // 根据token获取用户id
        try {
            Map<String,Object> claims = JwtUtils.verifyToken(token);
            Integer id = (Integer) claims.get("id");
            Long uid = id.longValue();
            attributes.put(Constant.USER_LOGIN_STATUS,uid);
            return true;
        }catch (TokenExpiredException e){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
