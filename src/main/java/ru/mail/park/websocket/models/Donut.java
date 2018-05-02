package ru.mail.park.websocket.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mail.park.websocket.models.base.Coordinate;

public class Donut {
    private Coordinate startPosition;
    private Coordinate endPosition;
    private double velocity;
    private double angle;
    private boolean isHit;

    @JsonCreator
    public Donut(@JsonProperty("startPosition") Coordinate startPosition,
                 @JsonProperty("endPosition") Coordinate endPosition,
                 @JsonProperty("velocity") double velocity,
                 @JsonProperty("angle") double angle,
                 @JsonProperty("isHit") boolean isHit) {
        this.startPosition = new Coordinate(startPosition);
        this.endPosition = new Coordinate(endPosition);
        this.velocity = velocity;
        this.angle = angle;
        this.isHit = isHit;
    }

    public Donut(Coordinate startPosition) {
        this.startPosition = new Coordinate(startPosition);
        this.endPosition = new Coordinate(0.0, 0.0);
        this.velocity = 0;
        this.angle = 0;
        this.isHit = false;
    }

    public Coordinate getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Coordinate startPosition) {
        this.startPosition = startPosition;
    }

    public Coordinate getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Coordinate endPosition) {
        this.endPosition = endPosition;
    }


    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }


    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }


    public boolean isHit() {
        return isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    @Override
    public String toString() {
        return '{'
                + "\"startPosition\": " + startPosition.toString()
                + ", \"endPosition\": " + endPosition.toString()
                + ", \"velocity\": " + velocity
                + ", \"angle\": " + angle
                + ", \"isHit\": " + isHit
                + '}';
    }

    /*public JSONObject toJSON() {
        final JSONObject json = new JSONObject();
        json.put("startPosition", startPosition.toJSON());
        json.put("endPosition", endPosition.toJSON());
        json.put("velocity", velocity);
        json.put("angle", angle);
        json.put("isHit", isHit);

        return json;
    }*/
}
