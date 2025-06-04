package com.evonapulse.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    private final Environment env;

    public CorsConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        if (List.of(env.getActiveProfiles()).contains("dev")) {
            config.setAllowedOriginPatterns(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));

            source.registerCorsConfiguration("/**", config);
        } else {
            config.setAllowedOriginPatterns(List.of("*"));
            config.setAllowedMethods(List.of("POST"));
            config.setAllowedHeaders(List.of("*"));

            source.registerCorsConfiguration("/api/ingest/**", config);
        }

        return new CorsFilter(source);
    }
}
