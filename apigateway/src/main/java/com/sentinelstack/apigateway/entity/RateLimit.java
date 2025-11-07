package com.sentinelstack.apigateway.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate_limits")
public class RateLimit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "requests_per_minute")
    private Integer requestsPerMinute;
    
    @Column(name = "requests_per_hour")
    private Integer requestsPerHour;
    
    @Column(name = "requests_per_day")
    private Integer requestsPerDay;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getRequestsPerMinute() {
        return requestsPerMinute;
    }
    
    public void setRequestsPerMinute(Integer requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }
    
    public Integer getRequestsPerHour() {
        return requestsPerHour;
    }
    
    public void setRequestsPerHour(Integer requestsPerHour) {
        this.requestsPerHour = requestsPerHour;
    }
    
    public Integer getRequestsPerDay() {
        return requestsPerDay;
    }
    
    public void setRequestsPerDay(Integer requestsPerDay) {
        this.requestsPerDay = requestsPerDay;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
