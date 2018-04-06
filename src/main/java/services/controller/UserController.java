package services.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import services.dao.UserDAO;
import services.exceptions.DatabaseConnectionException;
import services.model.ServerResponse;
import services.model.User;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.*;

import static services.Application.PATH_AVATARS_FOLDER;


@RestController
@CrossOrigin(origins = {"*", "http://127.0.0.1:8000"})
public class UserController {
    private static final String SESSION_KEY = "SESSION_KEY";
    private static final String ERROR_EMAIL = "Empty email";
    private static final String ERROR_PASSWORD = "Empty password";
    private static final String ERROR_NICKNAME = "Empty nickname";
    private static final int MAX_LENGTH_PASSWORD = 255;
    private static UserDAO userService;

    @SuppressWarnings("AccessStaticViaInstance")
    public UserController(UserDAO userService) {
        this.userService = userService;
    }


    private boolean isEmptyField(String field) {
        return ((field == null) || field.isEmpty());
    }

    private boolean isValidField(Integer field) {
        return ((field != null) && (field >= 0));
    }


    @GetMapping(value = "/connection", produces = "application/json")
    public ResponseEntity<?> connection() {
        return ResponseEntity.status(HttpStatus.OK).body(new ServerResponse("OK",
                "Congratulations, its successful connection"));
    }

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody @NotNull User userToRegister, HttpSession httpSession) {
        final StringBuilder errorString = new StringBuilder();

        if (isEmptyField(userToRegister.getEmail())) {
            errorString.append(ERROR_EMAIL);
        }

        if (isEmptyField(userToRegister.getPassword())) {
            errorString.append(' ' + ERROR_PASSWORD);
        }

        if (isEmptyField(userToRegister.getNickname())) {
            errorString.append(' ' + ERROR_NICKNAME);
        }

        if (errorString.length() > 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse("Error",
                    errorString.toString()));
        }

        if (!userService.register(userToRegister)) {
            // если попали в этот блок
            // значит такой юзер с таким мейлом уже существует
            // поэтому просто вернем ошибку
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse("Error",
                    "User with same email already exists"));
        }

        final User userForSession = userService.getUser(userToRegister.getEmail());
        httpSession.setAttribute(SESSION_KEY, userForSession);

        return ResponseEntity.status(HttpStatus.OK).body(userForSession);
    }


    @GetMapping(value = "/getUser", produces = "application/json")
    public ResponseEntity<?> getUser(HttpSession httpSession) {
        final User userFromSession = (User) httpSession.getAttribute(SESSION_KEY);

        if (userFromSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new
                    ServerResponse("Error", "You are not login"));
        }

        final User userForReturn;
        try {
            userForReturn = userService.getUser(userFromSession.getEmail());
        } catch (DatabaseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServerResponse("Error",
                    e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(userForReturn);
    }

    @GetMapping(value = "/avatar/{avatar:.+}")
    public void getAvatar(@PathVariable("avatar") String avatar, HttpServletResponse response) {
        @SuppressWarnings("TooBroadScope") final File imageForReturn = new File(PATH_AVATARS_FOLDER, avatar);

        //noinspection OverlyBroadCatchBlock
        try {
            final InputStream in = new FileInputStream(imageForReturn);

            //noinspection ConstantConditions
            if (in == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                response.setStatus(HttpServletResponse.SC_OK);
                IOUtils.copy(in, response.getOutputStream());
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PutMapping(value = "/updateUser", produces = "application/json")
    public ResponseEntity<?> update(@RequestBody @NotNull User updateData, HttpSession httpSession) {
        final User userFromSession = (User) httpSession.getAttribute(SESSION_KEY);

        if (userFromSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new
                    ServerResponse("Error", "You are not login"));
        }

        if (isValidField(updateData.getRating())) {
            userFromSession.setRating(updateData.getRating());
        }

        if (!isEmptyField(updateData.getPassword())) {
            userFromSession.setPassword(updateData.getPassword());
        }

        if (!isEmptyField(updateData.getNickname())) {
            userFromSession.setNickname(updateData.getNickname());
        }
        try {
            if (!userService.updateUser(userFromSession)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                        ServerResponse("Error", "Unsuccessful update"));
            }
        } catch (DatabaseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServerResponse("Error",
                    e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new
                ServerResponse("Ok", "Successful update"));
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody User userToLogin, HttpSession httpSession) {
        final StringBuilder errorString = new StringBuilder();


        if (isEmptyField(userToLogin.getEmail())) {
            errorString.append(ERROR_EMAIL);
        }

        if (isEmptyField(userToLogin.getPassword()) || userToLogin.getPassword().length() > MAX_LENGTH_PASSWORD) {
            errorString.append(' ' + ERROR_PASSWORD);
        }

        if (errorString.length() > 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse("Error",
                    errorString.toString()));
        }

        try {
            if (!userService.login(userToLogin)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse("Error",
                        "Invalid email or password"));
            }
        } catch (DatabaseConnectionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServerResponse("Error",
                    e.getMessage()));
        }

        final User userForSession = userService.getUser(userToLogin.getEmail());
        httpSession.setAttribute(SESSION_KEY, userForSession);

        return ResponseEntity.status(HttpStatus.OK).body(userForSession);
    }

    @DeleteMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        if (httpSession.getAttribute(SESSION_KEY) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServerResponse("Error", "Unsuccessful logout"));
        }

        httpSession.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(new ServerResponse("Ok", "Successful logout"));
    }
}