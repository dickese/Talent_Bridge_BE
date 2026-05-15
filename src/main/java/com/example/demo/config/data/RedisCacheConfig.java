package com.example.demo.config.data;

import com.example.demo.model.SessionMeta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SocketOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
//@EnableCaching
public class RedisCacheConfig {

    @Value("${spring.data.redis.host:}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.url:}")
    private String redisUrl;

    @Bean
    CommandLineRunner testRedis(StringRedisTemplate redisTemplate) {
        return args -> {
            try {
                redisTemplate.opsForValue().set("test", "hello");
                String value = redisTemplate.opsForValue().get("test");
                log.info("Redis Connection Success! Test value: {}", value);
                log.info("Redis Configuration - Host: {}, Port: {}", redisHost, redisPort);
            } catch (Exception e) {
                log.error("Redis Connection Failed: {}", e.getMessage(), e);
            }
        };
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (redisUrl != null && !redisUrl.isBlank()) {
            RedisURI redisURI = RedisURI.create(redisUrl);

            LettuceClientConfiguration.LettuceClientConfigurationBuilder builder =
                    LettuceClientConfiguration.builder();

            //Check if url has prefix (rediss) for external url when develop and test out-site Render environment
            if ("rediss".equals(redisURI.toURI().getScheme())) {
                builder.useSsl();
            }

            LettuceClientConfiguration clientConfig = builder.build();

            RedisStandaloneConfiguration redisConfig =
                    new RedisStandaloneConfiguration(
                            redisURI.getHost(),
                            redisURI.getPort()
                    );

            redisConfig.setUsername(redisURI.getUsername());

            if (redisURI.getPassword() != null) {
                redisConfig.setPassword(
                        RedisPassword.of(redisURI.getPassword())
                );
            }

            return new LettuceConnectionFactory(
                    redisConfig,
                    clientConfig
            );
        }

        // Fallback: if profile doesn't has url, use host and port to connect redis
        log.info("🔧 Connecting to Redis using host:port format (Development): {}:{}", redisHost, redisPort);
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        if (redisPassword != null && !redisPassword.isBlank()) {
            serverConfig.setPassword(RedisPassword.of(redisPassword));
        }

        return new LettuceConnectionFactory(serverConfig);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean
    public RedisTemplate<String, Object> redisSessionMetaTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // KEY
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // VALUE
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        RedisSerializer<Object> valueSerializer = RedisSerializer.json();

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);


        return template;
    }
}