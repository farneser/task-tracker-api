package dev.farneser.tasktracker.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Slf4j
@Configuration
public class CorsConfig {
    @Value("${application.allowed.origins}")
    private String[] allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();

        for (var allowedOrigin : allowedOrigins) {
            config.addAllowedOrigin(allowedOrigin);
        }

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/api/**", config);

        printCorsConfiguration(config);

        return new CorsFilter(source);
    }

    public static void printCorsConfiguration(CorsConfiguration corsConfiguration) {
        log.info("=== CORS Configuration ===");
        log.info("Allowed Origins: " + corsConfiguration.getAllowedOrigins());
        log.info("Allowed Methods: " + corsConfiguration.getAllowedMethods());
        log.info("Allowed Headers: " + corsConfiguration.getAllowedHeaders());
        log.info("Exposed Headers: " + corsConfiguration.getExposedHeaders());
        log.info("Allow Credentials: " + corsConfiguration.getAllowCredentials());
        log.info("Max Age: " + corsConfiguration.getMaxAge());
        log.info("===========================");
    }
}