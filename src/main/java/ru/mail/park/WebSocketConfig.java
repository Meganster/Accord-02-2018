package ru.mail.park;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.validation.constraints.NotNull;

@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final @NotNull WebSocketHandler webSocketHandler;

    public WebSocketConfig(@NotNull WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/mgame")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*"); //* //http://127.0.0.1:8000
    }
}