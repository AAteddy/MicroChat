package com.tedsaasfaha.MicroChat.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitingConfig {

    // Limit: 3 password reset requests per hour per Ip
    @Bean(name = "ipRateLimiter")
    public Bucket ipRateLimiter() {
        Bandwidth limit = Bandwidth.classic(
                3, // 3 requests
                Refill.intervally(3, Duration.ofHours(1)) // Refill 3 tokens every hour
        );
        return Bucket.builder().addLimit(limit).build();
    }

    // Limit: 5 password reset requests per hour per user
    @Bean(name = "userRateLimiter")
    public Bucket userRateLimiter() {
        Bandwidth limit = Bandwidth.classic(
                5, // 5 requests
                Refill.intervally(5, Duration.ofHours(1)) // Refill 4 tokens every hour
        );
        return Bucket.builder().addLimit(limit).build();
    }
}
