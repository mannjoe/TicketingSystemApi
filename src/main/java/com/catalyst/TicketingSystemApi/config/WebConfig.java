package com.catalyst.TicketingSystemApi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Add OPTIONS
                .allowedHeaders("*")
                .exposedHeaders("Authorization") // Explicitly expose Authorization header
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}