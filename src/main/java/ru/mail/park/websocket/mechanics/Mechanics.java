package ru.mail.park.websocket.mechanics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.park.websocket.models.Donut;
import ru.mail.park.websocket.models.Homer;
import ru.mail.park.websocket.models.base.Coordinate;

public class Mechanics {
    private static final Logger LOGGER = LoggerFactory.getLogger(Mechanics.class);

    public static void solveDonutEndPosition(Donut donut, Homer homer, Boolean isLeft) {
        if (isLeft) {
            final Coordinate startPosition = donut.getStartPosition();
            final double endPositionX = homer.getPositionX();
            final double deltaX = endPositionX - startPosition.getPositionX();

            final double deltaY = deltaX * Math.tan(donut.getAngle());
            final double endPositionY = startPosition.getPositionY() - deltaY;

            checkHit(homer, donut, deltaX, deltaY);
            donut.setEndPosition(new Coordinate(endPositionX, endPositionY));
        }
        /*} else {
            final Coordinate startPosition = donut.getStartPosition();
            final double endPositionX = homer.getPositionX();
            final double deltaX = startPosition.getPositionX() - endPositionX;

            final double deltaY = deltaX * Math.tan(donut.getAngle());
            final double endPositionY = deltaY - startPosition.getPositionY();

            checkHit(homer, donut, deltaX, deltaY);
            donut.setEndPosition(new Coordinate(endPositionX, endPositionY));
        }*/
    }

    public static void checkHit(Homer homer, Donut donut, double deltaX, double deltaY) {
        final double hypotenuse = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        final double timeFlight = hypotenuse / donut.getVelocity();

        final Double homerEndPositionY = homer.getPositionY() + homer.getVelocity() * timeFlight;

        if (homerEndPositionY.equals(donut.getEndPosition().getPositionY())) {
            donut.setIsHit(true);
        } else {
            donut.setIsHit(false);
        }
    }

    public static void updateHomerState(Homer homer, long time) {
        if (isReachedBorder(homer)) {
            LOGGER.warn("Turn Homer in back current");

            homer.setVelocity(-1 * homer.getVelocity());
            homer.setPositionY(homer.getPositionY() + homer.getVelocity() * (double) time * 0.001);
        } else {
            homer.setPositionY(homer.getPositionY() + homer.getVelocity() * (double) time * 0.001);
        }
    }

    private static boolean isReachedBorder(Homer homer) {
        // если гомер достиг коориднаты 10.0 и движется наверх
        final Boolean homerUpBorder = homer.getPositionY() <= 10.0 && homer.getVelocity() < 0;
        // или
        // если гомер достиг коориднаты 90.0 и движется вниз
        final Boolean homerDownBorder = homer.getPositionY() >= 90.0 && homer.getVelocity() > 0;
        return homerUpBorder || homerDownBorder;
    }
}
