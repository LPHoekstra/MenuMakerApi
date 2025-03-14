package com.MenuMaker.MenuMakerApi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {
    private final String URL = "http://localhost:5173/";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(_ -> {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of(URL));
            corsConfiguration.setAllowedMethods(List.of("POST"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
