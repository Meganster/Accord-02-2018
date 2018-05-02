package ru.mail.park.websocket.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.socket.WebSocketSession;
import ru.mail.park.websocket.models.base.Coordinate;

public class Player {
    private static final Coordinate START_POSITION_UP_LEFT = new Coordinate(0.0, 0.0);
    private static final Coordinate START_POSITION_UP_RIGHT = new Coordinate(100.0, 0.0);

    private final Long userID;
    private WebSocketSession session;
    private int score;
    private Coordinate position;
    private Donut donut;

    public Player(Long userID, WebSocketSession session, boolean isLeft) {
        this.userID = userID;
        this.session = session;
        this.score = 0;
        if (isLeft) {
            this.position = START_POSITION_UP_LEFT;
            this.donut = new Donut(START_POSITION_UP_LEFT);
        } else {
            this.position = START_POSITION_UP_RIGHT;
            this.donut = new Donut(START_POSITION_UP_RIGHT);
        }
    }

    @JsonCreator
    public Player(@JsonProperty("userID") Long userID,
                  @JsonProperty("score") int score,
                  @JsonProperty("position") Coordinate position,
                  @JsonProperty("donut") Donut donut) {
        this.userID = userID;
        this.score = score;
        this.position = new Coordinate(position);
        this.donut = donut;
    }

    public Long getUserID() {
        return userID;
    }

    public WebSocketSession getSession() {
        return session;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public double getPositionY() {
        return position.getPositionY();
    }

    public void setPositionY(double positionY) {
        this.position.setPositionY(positionY);
    }

    public double getPositionX() {
        return position.getPositionX();
    }

    public void setPositionX(double positionX) {
        this.position.setPositionX(positionX);
    }


    public Donut getDonut() {
        return donut;
    }

    public void setDonut(Donut donut) {
        this.donut = donut;
    }

    @Override
    public String toString() {
        return '{'
                + "'userID': " + userID
                + ", 'score': " + score
                + ", 'position': " + position.toString()
                + ", 'donut': " + donut.toString()
                + '}';
    }

    /*public JSONObject toJSON() {
        final JSONObject json = new JSONObject();
        json.put("userID", userID);
        json.put("score", score);
        json.put("position", position.toJSON());
        json.put("donut", donut.toJSON());

        return json;
    }*/
}
