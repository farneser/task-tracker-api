package dev.farneser.tasktracker.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Slf4j
@Configuration
@EnableWebMvc
public class CorsConfig {
    @Value("${application.allowed.origins:http://localhost:3000, http://client:3000}")
    private String[] allowedOrigins;

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

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        log.debug("Starting CORS configuration");

        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();

        for (String allowedOrigin : allowedOrigins) {
            log.debug("Adding allowed origin {}", allowedOrigin);

            config.addAllowedOrigin(allowedOrigin);
        }
        log.debug("Adding allowed methods");

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        log.debug("Set allow credentials to true");
        config.setAllowCredentials(true);

        log.debug("Set allowed headers to *");
        config.setAllowedHeaders(List.of("*"));

        source.registerCorsConfiguration("/api/**", config);

        var bean = new FilterRegistrationBean<>(new CorsFilter(source));

        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        printCorsConfiguration(config);

        return bean;
    }
}