package com.sentinelstack.apigateway.controller;

import com.sentinelstack.apigateway.dto.APIKeyResponse;
import com.sentinelstack.apigateway.dto.CreateAPIKeyRequest;
import com.sentinelstack.apigateway.entity.User;
import com.sentinelstack.apigateway.repository.UserRepository;
import com.sentinelstack.apigateway.service.APIKeyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
public class APIKeyController {

    private final APIKeyService apiKeyService;
    private final UserRepository userRepository;

    public APIKeyController(APIKeyService apiKeyService, UserRepository userRepository) {
        this.apiKeyService = apiKeyService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<APIKeyResponse> createAPIKey(@Valid @RequestBody CreateAPIKeyRequest request) {
        Long userId = getCurrentUserId();
        APIKeyResponse response = apiKeyService.createAPIKey(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<APIKeyResponse>> listAPIKeys() {
        Long userId = getCurrentUserId();
        List<APIKeyResponse> keys = apiKeyService.getUserAPIKeys(userId);
        return ResponseEntity.ok(keys);
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revokeAPIKey(@PathVariable Long keyId) {
        Long userId = getCurrentUserId();
        apiKeyService.revokeAPIKey(userId, keyId);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }
        
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return user.getId();
    }
}
