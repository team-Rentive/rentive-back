package com.rent.rentservice;

import lombok.Builder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:400")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH");
    }

}
