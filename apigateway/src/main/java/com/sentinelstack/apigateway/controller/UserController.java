package com.sentinelstack.apigateway.controller;

import com.sentinelstack.apigateway.dto.CreateUserRequest;
import com.sentinelstack.apigateway.dto.LoginRequest;
import com.sentinelstack.apigateway.dto.LoginResponse;
import com.sentinelstack.apigateway.dto.UserResponse;
import com.sentinelstack.apigateway.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService svc;

    public UserController(UserService svc)
    {

        this.svc = svc;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest req) {
        UserResponse created = svc.register(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        LoginResponse response = svc.login(req);
        return ResponseEntity.ok(response);
    }
}
