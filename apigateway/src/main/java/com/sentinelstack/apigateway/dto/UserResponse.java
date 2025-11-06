
package com.sentinelstack.apigateway.dto;

import java.time.LocalDateTime;

    public record UserResponse(Long id, String username, String email,  LocalDateTime createdAt) {}


