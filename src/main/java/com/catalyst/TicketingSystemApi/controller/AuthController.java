package com.catalyst.TicketingSystemApi.controller;

import com.catalyst.TicketingSystemApi.config.security.JwtService;
import com.catalyst.TicketingSystemApi.dto.AuthDto.LoginRequest;
import com.catalyst.TicketingSystemApi.dto.AuthDto.LoginResponse;
import com.catalyst.TicketingSystemApi.dto.AuthDto.UserResponse;
import com.catalyst.TicketingSystemApi.model.User;
import com.catalyst.TicketingSystemApi.service.AuthService;
import com.catalyst.TicketingSystemApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (!userService.isActiveUser(request.username())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is not active");
        }
        User user = authService.authenticateUser(request.username(), request.password());
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return ResponseEntity.ok(new LoginResponse(jwtToken, refreshToken, UserResponse.fromUser(user)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String token = refreshToken.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null) {
            User user = (User) userService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, user)) {
                String newJwtToken = jwtService.generateToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                return ResponseEntity.ok(new LoginResponse(newJwtToken, newRefreshToken, UserResponse.fromUser(user)));
            }
        }

        throw new IllegalArgumentException("Invalid refresh token");
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<Boolean> validateResetToken(
            @RequestParam String token) {
        try {
            userService.validateAndCleanupToken(token);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.ok(false);
        }
    }
}