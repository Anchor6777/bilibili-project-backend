package com.example.usercenterbackend;

import com.example.usercenterbackend.websocket.ChatHandler;
import com.example.usercenterbackend.websocket.ChatHandshakeInterceptor;
import com.example.usercenterbackend.websocket.NewChatHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
@EnableWebSocket
@MapperScan("com.example.usercenterbackend.mapper")
public class UserCenterBackendApplication {
    @Bean
    WebSocketConfigurer createWebSocketConfigurer(@Autowired ChatHandler chatHandler,
                                                  @Autowired NewChatHandler newChatHandler,
                                                  @Autowired ChatHandshakeInterceptor chatHandshakeInterceptor) {
        return new WebSocketConfigurer() {
            @Override
            public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
                registry.addHandler(chatHandler, "/chat")
                        .addHandler(newChatHandler,"/new_chat")
                        .addInterceptors(chatHandshakeInterceptor)
                        .setAllowedOrigins("*"); // 配置跨域
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBackendApplication.class, args);
    }

}
