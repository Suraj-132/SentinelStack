package com.sentinelstack.apigateway.service;

import com.sentinelstack.apigateway.dto.CreateUserRequest;
import com.sentinelstack.apigateway.dto.LoginRequest;
import com.sentinelstack.apigateway.dto.LoginResponse;
import com.sentinelstack.apigateway.dto.UserResponse;

public interface UserService {
    UserResponse register(CreateUserRequest req);
    LoginResponse login(LoginRequest req);
}
