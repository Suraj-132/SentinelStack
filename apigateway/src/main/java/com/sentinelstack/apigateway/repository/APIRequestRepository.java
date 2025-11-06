package com.sentinelstack.apigateway.repository;

import com.sentinelstack.apigateway.entity.APIRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface APIRequestRepository extends JpaRepository<APIRequest, Long> {
    
    List<APIRequest> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
    
    long countByUserId(Long userId);
    
    long countByUserIdAndTimestampAfter(Long userId, LocalDateTime after);
    
    long countByUserIdAndTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<APIRequest> findByUserIdAndTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT COUNT(r) FROM APIRequest r WHERE r.userId = :userId AND r.timestamp >= :startDate")
    long countRequestsSince(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT AVG(r.responseTimeMs) FROM APIRequest r WHERE r.userId = :userId")
    Double getAverageResponseTime(@Param("userId") Long userId);
    
    @Query("SELECT AVG(r.responseTimeMs) FROM APIRequest r WHERE r.userId = :userId AND r.timestamp BETWEEN :startDate AND :endDate")
    Double averageResponseTimeByUserIdAndTimestampBetween(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
