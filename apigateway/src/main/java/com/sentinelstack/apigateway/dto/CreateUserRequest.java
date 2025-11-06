package com.sentinelstack.apigateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must be 3..50 chars")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 100, message = "password must be at least 6 chars")
    private String password;

    @Size(max = 50, message = "firstName must be max 50 chars")
    private String firstName;

    @Size(max = 50, message = "lastName must be max 50 chars")
    private String lastName;

    public CreateUserRequest() {}

    public CreateUserRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // getters + setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
