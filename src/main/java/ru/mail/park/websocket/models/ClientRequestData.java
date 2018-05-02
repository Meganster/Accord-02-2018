package ru.mail.park.websocket.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mail.park.websocket.models.base.Coordinate;


public class ClientRequestData {
    private Double velocity;
    private Double angle;
    private Coordinate position;
    private Boolean isShoot;

    @JsonCreator
    public ClientRequestData(
            @JsonProperty("velocity") double velocity,
            @JsonProperty("angle") double angle,
            @JsonProperty("position") Coordinate position,
            @JsonProperty("isShoot") boolean isShoot
    ) {
        this.velocity = velocity;
        this.angle = angle;
        this.position = position;
        this.isShoot = isShoot;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public Boolean isShoot() {
        return isShoot;
    }

    public void setIsShoot(Boolean shoot) {
        this.isShoot = shoot;
    }
}
