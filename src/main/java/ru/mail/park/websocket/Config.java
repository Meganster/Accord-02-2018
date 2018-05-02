package ru.mail.park.websocket;

public class Config {
    // STEP_TIME for GameLoop
    public static final long STEP_TIME = 40;

    // THREADS_NUMBER for TaskRunner
    public static final int THREADS_NUMBER = 5;

    // middle of all game field
    public static final double MIDDLE_OF_BOARD = 50.0;

    // accuracy for homer step
    public static final double ACCURACY_OF_STEP = 0.001;

    public static final int SCORES_TO_WIN = 100;

    public static final String[] TRUSTED_URLS = new String[]{
            "https://backend-accord-02-2018.herokuapp.com",
            "http://127.0.0.1:8000"
    };

    public static final String SESSION_KEY = "SESSION_KEY";
}
