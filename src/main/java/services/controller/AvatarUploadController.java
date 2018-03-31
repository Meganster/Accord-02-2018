package services.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import services.dao.UserDAO;
import services.exceptions.DatabaseConnectionException;
import services.model.ServerResponse;
import services.model.User;

import javax.servlet.http.HttpSession;
import java.io.*;

import static services.Application.PATH_AVATARS_FOLDER;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:8000"})
public class AvatarUploadController {
    private static final String SESSION_KEY = "SESSION_KEY";
    private static UserDAO userService;

    @SuppressWarnings("AccessStaticViaInstance")
    public AvatarUploadController(UserDAO userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/updateAvatar")
    public ResponseEntity<?> provideUploadInfo(HttpSession httpSession) {
        final User userFromSession = (User) httpSession.getAttribute(SESSION_KEY);

        if (userFromSession != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ServerResponse("Ok", "Ready to load your avatar"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ServerResponse("Error", "You are not login"));
        }
    }

    @SuppressWarnings({"OverlyBroadCatchBlock", "ConstantConditions", "IOResourceOpenedButNotSafelyClosed"})
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity<?> handleFileUpload(@RequestParam("avatar") MultipartFile file, HttpSession httpSession) {
        final User userFromSession = (User) httpSession.getAttribute(SESSION_KEY);
        final String oldFileName = file.getOriginalFilename();
        final String typeOfAvatar = oldFileName.substring(oldFileName.lastIndexOf('.'));
        final String nameFile = String.valueOf(userFromSession.getId()) + typeOfAvatar;

        if (userFromSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ServerResponse("Error", "You are not login"));
        }

        if (!file.isEmpty()) {
            try {
                final byte[] bytes = file.getBytes();
                @SuppressWarnings("resource") final BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File(PATH_AVATARS_FOLDER, nameFile)));
                stream.write(bytes);
                stream.close();

                userFromSession.setAvatar(nameFile);

                try {
                    if (!userService.updateAvatar(userFromSession)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ServerResponse("Error", "Bad file"));
                    }
                } catch (DatabaseConnectionException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServerResponse("Error",
                            e.getMessage()));
                }

                return ResponseEntity.status(HttpStatus.OK).body(new ServerResponse("Ok", "Successful loading"));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ServerResponse("Error", "Unsuccessful loading"));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerResponse("Error", "File is empty"));
    }
}
