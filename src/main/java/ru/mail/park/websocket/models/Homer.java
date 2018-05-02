package ru.mail.park.websocket.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mail.park.websocket.models.base.Coordinate;

public class Homer {
    private static final Coordinate START_POSITION_UP = new Coordinate(50.0, 0.0);
    private static final Coordinate START_POSITION_CENTER = new Coordinate(50.0, 50.0);
    private static final double STANDART_VELOCITY = 1;

    @SuppressWarnings("CanBeFinal")
    private Boolean isActive;
    private Coordinate position;
    private double velocity;

    public Homer() {
        isActive = true;
        position = new Coordinate(START_POSITION_UP);
        velocity = STANDART_VELOCITY;
    }

    @JsonCreator
    public Homer(@JsonProperty("isActive") Boolean isActive,
                 @JsonProperty("position") Coordinate position,
                 @JsonProperty("velocity") Double velocity) {
        this.isActive = isActive;
        this.position = new Coordinate(position);
        this.velocity = velocity;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public Double getPositionY() {
        return position.getPositionY();
    }

    public void setPositionY(double positionY) {
        this.position.setPositionY(positionY);
    }

    public Double getPositionX() {
        return position.getPositionX();
    }

    public void setPositionX(double positionX) {
        this.position.setPositionX(positionX);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public Double getVelocity() {
        return velocity;
    }

    /*public JSONObject toJSON() {
        final JSONObject json = new JSONObject();
        json.put("position", position.toJSON());

        return json;
    }*/

    @Override
    public String toString() {
        return '{'
                + "\"position\": " + position.toString()
                + '}';
    }
}
