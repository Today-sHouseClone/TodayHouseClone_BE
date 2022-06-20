package com.hanghae.Today.sHouse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
//                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "HEAD", "OPTIONS")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }
}