package ru.mail.park.models;

import java.util.List;

public class ScoreBoard {
    private int currentPage;
    private int numberOfPages;
    private List<User> scoreBoard;

    @SuppressWarnings("unused")
    public ScoreBoard() {
        this.currentPage = 0;
        this.numberOfPages = 0;
        //noinspection ConstantConditions
        this.scoreBoard = null;
    }

    public ScoreBoard(int currentPage, int numberOfPages, List<User> usersList) {
        this.currentPage = currentPage;
        this.numberOfPages = numberOfPages;
        this.scoreBoard = usersList;
    }

    @SuppressWarnings("unused")
    public int getcurrentPage() {
        return this.currentPage;
    }

    @SuppressWarnings("unused")
    public int getnumberOfPages() {
        return this.numberOfPages;
    }

    @SuppressWarnings("unused")
    public List<User> getScoreBoard() {
        return this.scoreBoard;
    }

    @SuppressWarnings("unused")
    public void setcurrentPage(int newCurrentPage) {
        this.currentPage = newCurrentPage;
    }

    @SuppressWarnings("unused")
    public void setnumberOfPages(int newNumberOfPages) {
        this.numberOfPages = newNumberOfPages;
    }

    @SuppressWarnings("unused")
    public void setScoreBoard(List<User> newListUserInfo) {
        this.scoreBoard = newListUserInfo;
    }
}
