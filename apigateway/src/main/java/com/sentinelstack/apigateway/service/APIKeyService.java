package com.sentinelstack.apigateway.service;

import com.sentinelstack.apigateway.dto.APIKeyResponse;
import com.sentinelstack.apigateway.dto.CreateAPIKeyRequest;

import java.util.List;

public interface APIKeyService {
    APIKeyResponse createAPIKey(Long userId, CreateAPIKeyRequest request);
    List<APIKeyResponse> getUserAPIKeys(Long userId);
    void revokeAPIKey(Long userId, Long keyId);
    boolean validateAPIKey(String apiKey);
}
