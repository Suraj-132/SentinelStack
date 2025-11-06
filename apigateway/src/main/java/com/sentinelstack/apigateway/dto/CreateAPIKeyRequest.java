package com.sentinelstack.apigateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAPIKeyRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    private Integer requestsPerMinute;
    private Integer requestsPerDay;
    private Integer expiresInDays;

    public CreateAPIKeyRequest() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public void setRequestsPerMinute(Integer requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }

    public Integer getRequestsPerDay() {
        return requestsPerDay;
    }

    public void setRequestsPerDay(Integer requestsPerDay) {
        this.requestsPerDay = requestsPerDay;
    }

    public Integer getExpiresInDays() {
        return expiresInDays;
    }

    public void setExpiresInDays(Integer expiresInDays) {
        this.expiresInDays = expiresInDays;
    }
}
