package com.catalyst.TicketingSystemApi.service.impl;

import com.catalyst.TicketingSystemApi.dto.AuthDto.ChangePasswordRequest;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserCreate;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserGet;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserUpdateByAdmin;
import com.catalyst.TicketingSystemApi.exception.ResourceNotFoundException;
import com.catalyst.TicketingSystemApi.model.User;
import com.catalyst.TicketingSystemApi.model.UserRole;
import com.catalyst.TicketingSystemApi.repository.UserRepository;
import com.catalyst.TicketingSystemApi.service.UserService;
import com.catalyst.TicketingSystemApi.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public void updateResetPasswordToken(String email, String token) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(expiryTime);
            userRepository.save(user);
        }
    }

    public void updatePassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        if (user.getResetPasswordTokenExpiry() == null ||
                user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            user.clearPasswordResetToken();
            userRepository.save(user);
            throw new IllegalArgumentException("Token has expired.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.clearPasswordResetToken();

        userRepository.save(user);
    }

    public void validateAndCleanupToken(String token) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        if (user.getResetPasswordTokenExpiry() == null ||
                user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            user.clearPasswordResetToken();
            userRepository.save(user);
            throw new IllegalArgumentException("Token has expired");
        }
    }

    public User createUser(UserCreate userCreate) {
        User user = new User();
        if (!userCreate.password().equals(userCreate.confirmPassword())) {
            throw new IllegalArgumentException("Password and confirmation do not match");
        }
        user.setUsername(userCreate.username().toLowerCase().trim());
        user.setEmail(userCreate.email());
        user.setFirstName(FormatUtil.formatName(userCreate.firstName()));
        user.setLastName(FormatUtil.formatName(userCreate.lastName()));
        user.setPassword(passwordEncoder.encode(userCreate.password()));
        user.setUserRole(userCreate.role());
        user.setActive(true);

        return userRepository.save(user);
    }

    public List<String> getAllRoles() {
        return Arrays.stream(UserRole.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<UserGet> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserGet::fromUser)
                .collect(Collectors.toList());
    }

    public List<UserGet> getActiveUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActive)
                .map(UserGet::fromUser)
                .collect(Collectors.toList());
    }

    public UserGet getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return UserGet.fromUser(user);
    }

    public UserGet getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        return UserGet.fromUser(user);
    }

    public UserGet updateUser(String username, UserUpdateByAdmin userUpdate) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        user.setFirstName(FormatUtil.formatName(userUpdate.firstName()));
        user.setLastName(FormatUtil.formatName(userUpdate.lastName()));
        user.setEmail(userUpdate.email());
        user.setUserRole(userUpdate.role());
        user.setActive(userUpdate.active());

        userRepository.save(user);

        return UserGet.fromUser(user);
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public boolean isActiveUser(String username) {
    	User user = userRepository.findByUsername(username);
    	if (user == null) {
    		throw new UsernameNotFoundException("User not found");
    	}
    	return user.isActive();
    }
}
