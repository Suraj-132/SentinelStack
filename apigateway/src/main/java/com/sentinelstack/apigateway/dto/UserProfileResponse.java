package com.sentinelstack.apigateway.dto;

import java.time.LocalDateTime;

public class UserProfileResponse {
    
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String role;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public UserProfileResponse() {}

    public UserProfileResponse(Long id, String username, String email, String firstName, 
                              String lastName, String role, Boolean emailVerified,
                              LocalDateTime createdAt, LocalDateTime lastLoginAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = buildFullName(firstName, lastName, username);
        this.role = role;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    private String buildFullName(String firstName, String lastName, String username) {
        if (firstName == null && lastName == null) {
            return username;
        }
        return (firstName != null ? firstName : "") + 
               (firstName != null && lastName != null ? " " : "") + 
               (lastName != null ? lastName : "");
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
