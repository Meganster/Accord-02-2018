package ru.mail.park.websocket.models.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//CHECKSTYLE:OFF
public class Coordinate {
    private Double x;
    private Double y;

    @JsonCreator
    public Coordinate(
            @JsonProperty("x") Double x,
            @JsonProperty("y") Double y
    ) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate other) {
        this.x = other.getPositionX();
        this.y = other.getPositionY();
    }

    public double getPositionX() {
        return x;
    }

    public void setPositionX(double x) {
        this.x = x;
    }

    public double getPositionY() {
        return y;
    }

    public void setPositionY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return '{'
                + "'x': " + x
                + ", 'y': " + y
                + '}';
    }

    /*public JSONObject toJSON() {
        final JSONObject json = new JSONObject();
        json.put("x", this.x);
        json.put("y", this.y);

        return json;
    }*/
}
