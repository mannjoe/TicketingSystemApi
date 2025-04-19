package com.catalyst.TicketingSystemApi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public boolean createDatabaseIfNotExists() {
        try {
            // Extract the base URL without database name
            String baseUrl = datasourceUrl.split("\\?")[0];
            DataSource dataSource = DataSourceBuilder.create()
                    .url(baseUrl)
                    .username(username)
                    .password(password)
                    .build();

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            // Check if database exists and create if not
            jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS TicketEase");
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create database", e);
        }
    }
}