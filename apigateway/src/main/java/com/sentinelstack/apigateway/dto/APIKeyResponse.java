package com.sentinelstack.apigateway.dto;

import java.time.LocalDateTime;

public class APIKeyResponse {
    
    private Long id;
    private String name;
    private String keyPrefix;
    private String fullKey; // Only shown once during creation
    private String status;
    private Integer requestsPerMinute;
    private Integer requestsPerDay;
    private LocalDateTime lastUsedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public APIKeyResponse() {}

    public APIKeyResponse(Long id, String name, String keyPrefix, String status,
                         Integer requestsPerMinute, Integer requestsPerDay,
                         LocalDateTime lastUsedAt, LocalDateTime expiresAt,
                         LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.keyPrefix = keyPrefix;
        this.status = status;
        this.requestsPerMinute = requestsPerMinute;
        this.requestsPerDay = requestsPerDay;
        this.lastUsedAt = lastUsedAt;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKeyPrefix() { return keyPrefix; }
    public void setKeyPrefix(String keyPrefix) { this.keyPrefix = keyPrefix; }

    public String getFullKey() { return fullKey; }
    public void setFullKey(String fullKey) { this.fullKey = fullKey; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getRequestsPerMinute() { return requestsPerMinute; }
    public void setRequestsPerMinute(Integer requestsPerMinute) { this.requestsPerMinute = requestsPerMinute; }

    public Integer getRequestsPerDay() { return requestsPerDay; }
    public void setRequestsPerDay(Integer requestsPerDay) { this.requestsPerDay = requestsPerDay; }

    public LocalDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(LocalDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
