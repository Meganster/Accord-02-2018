package ru.mail.park.models;


@SuppressWarnings("unused")
public class ServerResponse {
    private String status;
    private String message;


    public ServerResponse() {

    }

    public ServerResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServerResponse(ServerResponse serverResponse) {
        this.status = serverResponse.status;
        this.message = serverResponse.message;
    }


    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
