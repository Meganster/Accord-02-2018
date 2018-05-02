package ru.mail.park.websocket.mechanics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.park.websocket.Config;

import java.time.Clock;

public class GameLoop implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLoop.class);

    private static final long STEP_TIME = Config.STEP_TIME;
    private final Clock clock = Clock.systemDefaultZone();
    private final GameRoom gameMechanics;


    public GameLoop(GameRoom gameMechanics) {
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void run() {
        mainCycle();
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }

    private void mainCycle() {
        while (true) {
            try {
                final long before = clock.millis();

                if (!gameMechanics.checkPlayersConnection()) {
                    gameMechanics.stopGame();
                }

                if (gameMechanics.tryFinishGame()) {
                    gameMechanics.stopGame();
                }

                gameMechanics.updateHomerState(STEP_TIME);
                gameMechanics.sendHomerState();

                final long after = clock.millis();

                try {
                    final long sleepingTime = Math.max(0, STEP_TIME - (after - before));
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    LOGGER.warn("interupted");
                    break;
                }

                if (Thread.currentThread().isInterrupted()) {
                    LOGGER.warn("interupted");
                    break;
                }
            } catch (RuntimeException e) {
                LOGGER.warn("GameMechanics failed");
                break;
            }
        }
    }
}