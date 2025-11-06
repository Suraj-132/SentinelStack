package com.sentinelstack.apigateway.service.impl;

import com.sentinelstack.apigateway.dto.CreateUserRequest;
import com.sentinelstack.apigateway.dto.LoginRequest;
import com.sentinelstack.apigateway.dto.LoginResponse;
import com.sentinelstack.apigateway.dto.UserResponse;
import com.sentinelstack.apigateway.entity.User;
import com.sentinelstack.apigateway.repository.UserRepository;
import com.sentinelstack.apigateway.security.JwtTokenProvider;
import com.sentinelstack.apigateway.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserRepository repo, PasswordEncoder encoder, JwtTokenProvider jwtTokenProvider) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public UserResponse register(CreateUserRequest req) {
        if (repo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (repo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());

//        if (req.getRole() != null) {
//            user.setRole(User.Role.valueOf(req.getRole().toUpperCase()));
//        }

        User saved = repo.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
//                saved.getRole().name(),
                saved.getCreatedAt()
        );
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        // Find user by username or email
        User user = repo.findByUsername(req.getUsernameOrEmail())
                .or(() -> repo.findByEmail(req.getUsernameOrEmail()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username/email or password"));

        // Verify password
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username/email or password");
        }

        // Update last login timestamp
        user.setLastLoginAt(java.time.LocalDateTime.now());
        repo.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getUsername());

        return new LoginResponse(token);
    }
}
