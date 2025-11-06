package com.sentinelstack.apigateway.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_requests", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_api_key_id", columnList = "api_key_id"),
        @Index(name = "idx_timestamp", columnList = "timestamp"),
        @Index(name = "idx_status_code", columnList = "status_code")
})
public class APIRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "api_key_id")
    private Long apiKeyId;

    @Column(nullable = false, length = 10)
    private String method;

    @Column(nullable = false, length = 500)
    private String path;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "response_time_ms", nullable = false)
    private Long responseTimeMs;

    @Column(name = "request_size_bytes")
    private Long requestSizeBytes;

    @Column(name = "response_size_bytes")
    private Long responseSizeBytes;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors
    public APIRequest() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getApiKeyId() { return apiKeyId; }
    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }

    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }

    public Long getRequestSizeBytes() { return requestSizeBytes; }
    public void setRequestSizeBytes(Long requestSizeBytes) { this.requestSizeBytes = requestSizeBytes; }

    public Long getResponseSizeBytes() { return responseSizeBytes; }
    public void setResponseSizeBytes(Long responseSizeBytes) { this.responseSizeBytes = responseSizeBytes; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
