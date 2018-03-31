package services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.nio.file.Paths;

@SpringBootApplication
public class Application {
    public static final String PATH_AVATARS_FOLDER = Paths.get("uploads")
            .toAbsolutePath().toString() + '/';

    // метод необходимый для загрузки аватарок
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        final MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("2MB");
        factory.setMaxRequestSize("2MB");
        return factory.createMultipartConfig();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) {
        // создадим папочку в которой будем хранить все аватарки
        final File avatarFolder = new File(PATH_AVATARS_FOLDER);
        if (!avatarFolder.exists()) {
            avatarFolder.mkdir();
        }

        SpringApplication.run(Application.class, args);
    }
}