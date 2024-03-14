package dev.farneser.tasktracker.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        try {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);

            log.debug("Redis host:port = {}:{}", redisHost, redisPort);

            log.debug("Start redis connection factory");

            return new JedisConnectionFactory(redisStandaloneConfiguration);
        } catch (Exception e) {
            log.error("Redis connection error: {}", e.getMessage());

            return null;
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        try {
            RedisTemplate<String, Object> template = new RedisTemplate<>();

            log.debug("Redis template created");

            template.setConnectionFactory(jedisConnectionFactory());
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

            log.debug("Redis template value serializer set to GenericJackson2JsonRedisSerializer");

            return template;
        } catch (Exception e) {
            log.error("Redis template error: {}", e.getMessage());

            return null;
        }
    }
}
