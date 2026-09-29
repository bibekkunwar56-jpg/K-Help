package com.example.demo.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Normalizes PostgreSQL connection URLs from cloud providers like Render, Heroku, Supabase.
 * Render injects: postgresql://user:pass@host:port/db or postgres://...
 * Spring Boot JDBC requires: jdbc:postgresql://host:port/db
 */
public class DatabaseUrlEnvironmentPostProcessor implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();
        String rawUrl = env.getProperty("SPRING_DATASOURCE_URL");

        if (rawUrl == null || rawUrl.isBlank()) {
            return;
        }

        // If it already starts with jdbc:, do nothing
        if (rawUrl.startsWith("jdbc:")) {
            return;
        }

        try {
            // e.g. postgres://user:password@host:port/database or postgresql://...
            URI uri = URI.create(rawUrl);
            String host = uri.getHost();
            int port = uri.getPort() != -1 ? uri.getPort() : 5432;
            String path = uri.getPath(); // /dbname
            String query = uri.getQuery();

            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path + (query != null ? "?" + query : "");

            Map<String, Object> overrides = new HashMap<>();
            overrides.put("spring.datasource.url", jdbcUrl);

            String userInfo = uri.getUserInfo();
            if (userInfo != null && userInfo.contains(":")) {
                String[] parts = userInfo.split(":", 2);
                overrides.put("spring.datasource.username", parts[0]);
                overrides.put("spring.datasource.password", parts[1]);
            }

            env.getPropertySources().addFirst(new MapPropertySource("cloudDatabaseOverrides", overrides));
            System.out.println("[K-Help Config] Normalized database URL to JDBC format: jdbc:postgresql://" + host + ":" + port + path);
        } catch (Exception e) {
            System.err.println("[K-Help Config] Failed to parse database URL: " + e.getMessage());
        }
    }
}
