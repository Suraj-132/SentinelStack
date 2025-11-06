package com.sentinelstack.apigateway.repository;

import com.sentinelstack.apigateway.entity.APIKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface APIKeyRepository extends JpaRepository<APIKey, Long> {
    
    List<APIKey> findByUserId(Long userId);
    
    Optional<APIKey> findByKeyHash(String keyHash);
    
    List<APIKey> findByUserIdAndStatus(Long userId, APIKey.Status status);
    
    long countByUserId(Long userId);
    
    boolean existsByKeyHash(String keyHash);
}
