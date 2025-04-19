package com.catalyst.TicketingSystemApi.dto;

import com.catalyst.TicketingSystemApi.model.User;
import com.catalyst.TicketingSystemApi.model.UserRole;

public class AuthDto {
    // Login Request
    public record LoginRequest(String username, String password) {
    }

    // User Response (for non-sensitive data)
    public record UserResponse(
            Long id,
            String username,
            String email,
            String firstName,
            String lastName,
            UserRole userRole
    ) {
        public static UserResponse fromUser(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUserRole()
            );
        }
    }

    // Login Response
    public record LoginResponse(
            String token,
            String refreshToken,
            UserResponse user
    ) {
    }

    public record ChangePasswordRequest(
            String currentPassword,
            String newPassword,
            String confirmNewPassword
    ) {
    }
}