package services.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.dao.UserDAO;
import services.exceptions.DatabaseConnectionException;
import services.model.ScoreBoard;
import services.model.ServerResponse;
import services.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@CrossOrigin(origins = {"*", "http://127.0.0.1:8000"})
public class ScoreBoardController {
    private static final int USER_PER_PAGE = 10;
    private static UserDAO userService;

    public ScoreBoardController(UserDAO userService) {
        //noinspection AccessStaticViaInstance
        this.userService = userService;
    }


    @GetMapping(path = "/scoreboard/{page}")
    public ResponseEntity<?> getLeaders(@PathVariable("page") @NotNull Integer page) {
        final int numberOfPages = userService.getLastPage(USER_PER_PAGE);

        if (page == null || page < 1) {
            page = 1;
        } else {
            if (page > numberOfPages) {
                page = numberOfPages;
            }
        }

        final List<User> userInfoList;
        try {
            userInfoList = userService.getSortedUsersInfoByRating(USER_PER_PAGE, page);
        } catch (DatabaseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ServerResponse("Error", e.getMessage()));
        }

        final ScoreBoard scoreBoardResponse = new ScoreBoard(page, numberOfPages, userInfoList);
        return ResponseEntity.status(HttpStatus.OK).body(scoreBoardResponse);
    }
}
