
package com.sentinelstack.apigateway.dto;

public class LoginResponse {

    private String token;
    private String tokenType = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}
