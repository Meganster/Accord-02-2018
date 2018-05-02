package ru.mail.park.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.mail.park.models.User;
import ru.mail.park.websocket.models.ClientRequestData;
import ru.mail.park.websocket.services.GameRoomService;

import javax.validation.constraints.NotNull;
import java.io.IOException;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;


public class SocketHandler extends TextWebSocketHandler {
    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");
    private final ObjectMapper objectMapper;
    private final GameRoomService gameRoomService;

    private static final String SESSION_KEY = Config.SESSION_KEY;

    // Add logger
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);


    public SocketHandler(ObjectMapper objectMapper, GameRoomService gameRoomService) {
        this.gameRoomService = gameRoomService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        final User user = checkUser(session);

        if (user != null) {
            session.getAttributes().put("UserID", user.getId());
            gameRoomService.addNewGameRoom(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if (!session.isOpen()) {
            return;
        }

        if (checkUser(session) == null) {
            return;
        }

        handleMessage(session, message);
    }

    private User checkUser(WebSocketSession session) {
        final User user = (User) session.getAttributes().get(SESSION_KEY);

        if (user == null) {
            LOGGER.warn("User requested websocket is not registred or not logged in. "
                    + "Openning websocket session is denied.");

            closeSessionSilently(session, ACCESS_DENIED);
            return null;
        }

        return user;
    }


    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(WebSocketSession session, TextMessage text) {
        try {
            final ClientRequestData clientData = objectMapper.readValue(text.getPayload(),
                    ClientRequestData.class);

            if (session.getAttributes().get("RoomID") != null) {
                LOGGER.warn("Message to GameRoom #" + session.getAttributes().get("RoomID").toString());

                gameRoomService.updateGameRoomState(clientData, session);
            }
        } catch (IOException ex) {
            LOGGER.error("Wrong json format at game response", ex);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        closeSessionSilently(webSocketSession, ACCESS_DENIED);
    }

    private void closeSessionSilently(@NotNull WebSocketSession session,
                                      @SuppressWarnings("SameParameterValue")
                                      @Nullable CloseStatus closeStatus) {
        final CloseStatus status = closeStatus;

        if (status == null) {
            closeStatus = SERVER_ERROR;
        }

        //noinspection CatchMayIgnoreException
        try {
            if (session.getAttributes().get("RoomID") != null) {
                gameRoomService.destroyGameRoom((Long) session.getAttributes().get("RoomID"));
            } else {
                gameRoomService.removeFromQueue(session);
            }

            session.close(status);
        } catch (IOException ignore) {
            LOGGER.debug("Ignore", ignore);
        }
    }
}