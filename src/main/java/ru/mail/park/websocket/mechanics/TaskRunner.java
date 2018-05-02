package ru.mail.park.websocket.mechanics;

import ru.mail.park.websocket.Config;
import ru.mail.park.websocket.models.ClientRequestData;
import ru.mail.park.websocket.models.Donut;
import ru.mail.park.websocket.models.Homer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class TaskRunner {
    private static final TaskRunner INSTANCE = new TaskRunner();
    private static final int THREADS_NUMBER = Config.THREADS_NUMBER;
    private final Executor tickExecutor;

    private TaskRunner() {
        tickExecutor = Executors.newFixedThreadPool(THREADS_NUMBER);
    }

    public static void updateGameRoom(GameRoom gameRoom, ClientRequestData clientData, Long userID) {
        INSTANCE.tickExecutor.execute(() -> {
            gameRoom.updatePlayersState(clientData, userID);
        });
    }

    public static void solveDonutEndPosition(Donut donut, Homer homer, Boolean isLeft) {
        INSTANCE.tickExecutor.execute(() -> {
            Mechanics.solveDonutEndPosition(donut, homer, isLeft);
        });
    }

    public static void updateHomerState(Homer homer, long time) {
        INSTANCE.tickExecutor.execute(() -> {
            Mechanics.updateHomerState(homer, time);
        });
    }
}
