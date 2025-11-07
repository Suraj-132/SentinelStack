package com.sentinelstack.apigateway.service.impl;

import com.sentinelstack.apigateway.entity.RateLimit;
import com.sentinelstack.apigateway.repository.APIRequestRepository;
import com.sentinelstack.apigateway.repository.RateLimitRepository;
import com.sentinelstack.apigateway.service.RateLimitService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    private final RateLimitRepository rateLimitRepository;
    private final APIRequestRepository apiRequestRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    // Default rate limits
    private static final int DEFAULT_REQUESTS_PER_MINUTE = 60;
    private static final int DEFAULT_REQUESTS_PER_HOUR = 1000;
    private static final int DEFAULT_REQUESTS_PER_DAY = 10000;

    public RateLimitServiceImpl(RateLimitRepository rateLimitRepository, 
                                 APIRequestRepository apiRequestRepository,
                                 RedisTemplate<String, Long> redisTemplate) {
        this.rateLimitRepository = rateLimitRepository;
        this.apiRequestRepository = apiRequestRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isRateLimitExceeded(Long userId) {
        RateLimit rateLimit = rateLimitRepository.findByUserId(userId)
                .orElse(getDefaultRateLimit());
        
        // Check minute limit using Redis
        if (rateLimit.getRequestsPerMinute() != null) {
            String minuteKey = "rate_limit:" + userId + "::minute";
            Long count = incrementAndGet(minuteKey, 60);
            if (count > rateLimit.getRequestsPerMinute()) {
                return true;
            }
        }
        
        // Check hour limit using Redis
        if (rateLimit.getRequestsPerHour() != null) {
            String hourKey = "rate_limit:" + userId + "::hour";
            Long count = incrementAndGet(hourKey, 3600);
            if (count > rateLimit.getRequestsPerHour()) {
                return true;
            }
        }
        
        // Check day limit using Redis
        if (rateLimit.getRequestsPerDay() != null) {
            String dayKey = "rate_limit:" + userId + "::day";
            Long count = incrementAndGet(dayKey, 86400);
            if (count > rateLimit.getRequestsPerDay()) {
                return true;
            }
        }
        
        return false;
    }
    
    private Long incrementAndGet(String key, long ttlSeconds) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            // Set expiration only on first increment
            redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
        }
        return count;
    }

    @Override
    public Optional<RateLimit> getRateLimitByUserId(Long userId) {
        return rateLimitRepository.findByUserId(userId);
    }

    @Override
    public RateLimit saveRateLimit(RateLimit rateLimit) {
        return rateLimitRepository.save(rateLimit);
    }

    @Override
    public RateLimit getDefaultRateLimit() {
        RateLimit defaultLimit = new RateLimit();
        defaultLimit.setRequestsPerMinute(DEFAULT_REQUESTS_PER_MINUTE);
        defaultLimit.setRequestsPerHour(DEFAULT_REQUESTS_PER_HOUR);
        defaultLimit.setRequestsPerDay(DEFAULT_REQUESTS_PER_DAY);
        return defaultLimit;
    }
}
