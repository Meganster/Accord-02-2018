package ru.mail.park.websocket;

public class Config {
    // STEP_TIME for GameLoop
    public static final long STEP_TIME = 40;

    // THREADS_NUMBER for TaskRunner
    public static final int THREADS_NUMBER = 5;

    public static final int SCORES_TO_WIN = 100;

    public static final String[] TRUSTED_URLS = new String[]{
            "https://backend-accord-02-2018.herokuapp.com",
            "http://127.0.0.1:8000"
    };

    public static final String SESSION_KEY = "SESSION_KEY";
}
