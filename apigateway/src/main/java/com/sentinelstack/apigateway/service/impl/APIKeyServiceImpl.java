package com.sentinelstack.apigateway.service.impl;

import com.sentinelstack.apigateway.dto.APIKeyResponse;
import com.sentinelstack.apigateway.dto.CreateAPIKeyRequest;
import com.sentinelstack.apigateway.entity.APIKey;
import com.sentinelstack.apigateway.repository.APIKeyRepository;
import com.sentinelstack.apigateway.service.APIKeyService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class APIKeyServiceImpl implements APIKeyService {

    private final APIKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom secureRandom = new SecureRandom();

    public APIKeyServiceImpl(APIKeyRepository apiKeyRepository, PasswordEncoder passwordEncoder) {
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public APIKeyResponse createAPIKey(Long userId, CreateAPIKeyRequest request) {
        // Generate random API key
        String apiKey = generateSecureAPIKey();
        String keyPrefix = apiKey.substring(0, 12); // First 12 chars for display
        String keyHash = passwordEncoder.encode(apiKey);

        // Create APIKey entity
        APIKey key = new APIKey(userId, request.getName(), keyPrefix, keyHash);
        
        // Set rate limits (use defaults if not provided)
        if (request.getRequestsPerMinute() != null) {
            key.setRequestsPerMinute(request.getRequestsPerMinute());
        }
        if (request.getRequestsPerDay() != null) {
            key.setRequestsPerDay(request.getRequestsPerDay());
        }
        
        // Set expiration if provided
        if (request.getExpiresInDays() != null && request.getExpiresInDays() > 0) {
            key.setExpiresAt(LocalDateTime.now().plusDays(request.getExpiresInDays()));
        }

        APIKey savedKey = apiKeyRepository.save(key);

        // Create response
        APIKeyResponse response = new APIKeyResponse(
                savedKey.getId(),
                savedKey.getName(),
                savedKey.getKeyPrefix(),
                savedKey.getStatus().name(),
                savedKey.getRequestsPerMinute(),
                savedKey.getRequestsPerDay(),
                savedKey.getLastUsedAt(),
                savedKey.getExpiresAt(),
                savedKey.getCreatedAt()
        );
        
        // Include full key only once during creation
        response.setFullKey(apiKey);
        
        return response;
    }

    @Override
    public List<APIKeyResponse> getUserAPIKeys(Long userId) {
        List<APIKey> keys = apiKeyRepository.findByUserId(userId);
        
        return keys.stream()
                .map(key -> new APIKeyResponse(
                        key.getId(),
                        key.getName(),
                        key.getKeyPrefix(),
                        key.getStatus().name(),
                        key.getRequestsPerMinute(),
                        key.getRequestsPerDay(),
                        key.getLastUsedAt(),
                        key.getExpiresAt(),
                        key.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void revokeAPIKey(Long userId, Long keyId) {
        APIKey key = apiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("API Key not found"));
        
        if (!key.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to revoke this API key");
        }
        
        key.setStatus(APIKey.Status.REVOKED);
        apiKeyRepository.save(key);
    }

    @Override
    public boolean validateAPIKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return false;
        }

        // Extract prefix (first 12 chars)
        if (apiKey.length() < 12) {
            return false;
        }
        
        String keyPrefix = apiKey.substring(0, 12);
        
        // Find keys with matching prefix
        List<APIKey> keys = apiKeyRepository.findByUserId(null); // We'll optimize this later
        
        for (APIKey key : keys) {
            if (key.getKeyPrefix().equals(keyPrefix) && key.isActive()) {
                if (passwordEncoder.matches(apiKey, key.getKeyHash())) {
                    // Update last used timestamp
                    key.setLastUsedAt(LocalDateTime.now());
                    apiKeyRepository.save(key);
                    return true;
                }
            }
        }
        
        return false;
    }

    private String generateSecureAPIKey() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return "sk_live_" + encoded;
    }
}
