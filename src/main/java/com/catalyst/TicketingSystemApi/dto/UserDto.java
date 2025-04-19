package com.catalyst.TicketingSystemApi.dto;

import com.catalyst.TicketingSystemApi.model.UserRole;
import com.catalyst.TicketingSystemApi.model.User;
import jakarta.validation.constraints.NotBlank;

public class UserDto {
    public record UserCreate(
            @NotBlank String username,
            @NotBlank String email,
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank String password,
            @NotBlank String confirmPassword,
            @NotBlank UserRole role
    ) {}

    public record UserGet(
            Long id,
            String username,
            String email,
            String fullName,
            String firstName,
            String lastName,
            UserRole role,
            Boolean active
    ) {
        public static UserGet fromUser(User user) {
            return new UserGet(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName() + ' ' + user.getLastName(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUserRole(),
                    user.isActive()
            );
        }
    }

    public record UserUpdateByAdmin(
            @NotBlank String username,
            @NotBlank String email,
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank Boolean active,
            @NotBlank UserRole role
    ) {}
}