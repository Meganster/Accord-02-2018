package ru.mail.park.websocket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.websocket.mechanics.GameRoom;
import ru.mail.park.websocket.mechanics.TaskRunner;
import ru.mail.park.websocket.models.ClientRequestData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Service
public final class GameRoomService {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    // Общая очередь сессий игроков
    // которые ждут онлайн игры
    private final Queue<WebSocketSession> queue = new LinkedList<>();
    private final ConcurrentHashMap<Long, GameRoom> gameRoomsMap = new ConcurrentHashMap<>();
    private final UserDAO userService;

    // Add logger
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRoomService.class);


    private GameRoomService(UserDAO userDAO) {
        userService = userDAO;
    }

    public void addNewGameRoom(WebSocketSession webSocketSession) {
        queue.add(webSocketSession);

        if (queue.size() > 1) {
            LOGGER.warn("2 players found");

            final Long roomId = ID_GENERATOR.getAndIncrement();

            final WebSocketSession firstPlayerSession = queue.poll();
            final WebSocketSession secondPlayerSession = queue.peek();

            if (!isSameUserId(firstPlayerSession, secondPlayerSession)) {
                // извлечем secondPlayerSession из верхушки очереди
                queue.poll();

                firstPlayerSession.getAttributes().put("RoomID", roomId);
                secondPlayerSession.getAttributes().put("RoomID", roomId);

                gameRoomsMap.put(roomId, new GameRoom(firstPlayerSession, secondPlayerSession, userService));
            } else {
                if (!queue.offer(firstPlayerSession)) {
                    LOGGER.warn("Lost session");
                }

                LOGGER.warn("2 sessions from same user. Abort creating GameRoom.");
            }
        }
    }

    boolean isSameUserId(WebSocketSession firstPlayerSession, WebSocketSession secondPlayerSession) {
        final Long firstUserId = (Long) firstPlayerSession.getAttributes().get("UserID");
        final Long secondUserId = (Long) secondPlayerSession.getAttributes().get("UserID");

        return firstUserId.equals(secondUserId);
    }

    public void destroyGameRoom(Long id) {
        LOGGER.warn("Destroy room");

        gameRoomsMap.get(id).stopGame();
        gameRoomsMap.remove(id);
    }

    public void removeFromQueue(WebSocketSession session) {
        queue.remove(session);
    }

    public void updateGameRoomState(ClientRequestData clientData, WebSocketSession session) {
        if (!gameRoomsMap.isEmpty()) {
            final GameRoom gameRoomForUpdate = gameRoomsMap.get((Long) session.getAttributes().get("RoomID"));
            final Long userID = (Long) session.getAttributes().get("UserID");

            TaskRunner.updateGameRoom(gameRoomForUpdate, clientData, userID);
        }
    }
}
