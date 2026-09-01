package com.example.usercenterbackend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8000","http://192.144.235.21","http://localhost:5173")
                .allowedMethods("*")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}
