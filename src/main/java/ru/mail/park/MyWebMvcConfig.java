package ru.mail.park;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.mail.park.websocket.Config;

// нужен чтобы не поломать CORS
@SuppressWarnings("deprecation")
public class MyWebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true).allowedOrigins(Config.TRUSTED_URLS)
                .allowedMethods("GET", "HEAD", "POST", "PATCH", "DELETE", "OPTIONS");
    }
}