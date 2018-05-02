package ru.mail.park.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;


public class MessageSender {
    private final ObjectMapper mapper;

    public MessageSender() {
        mapper = new ObjectMapper();
    }

    private <T> TextMessage getMessage(T messageForSend) {
        try {
            final String result = mapper.writeValueAsString(messageForSend);
            return (new TextMessage(result));
        } catch (JsonProcessingException e) {
            return null;
        }

    }

    public <T> boolean send(WebSocketSession session, @NotNull T messageForSend) {
        try {
            session.sendMessage(getMessage(messageForSend));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
