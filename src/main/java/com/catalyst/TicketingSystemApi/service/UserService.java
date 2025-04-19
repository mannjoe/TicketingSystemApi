package com.catalyst.TicketingSystemApi.service;

import com.catalyst.TicketingSystemApi.dto.AuthDto.ChangePasswordRequest;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserCreate;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserGet;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserUpdateByAdmin;
import com.catalyst.TicketingSystemApi.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    UserGet getUserByUsername(String username);
    UserGet getUserById(Long id);
    UserGet updateUser(String username, UserUpdateByAdmin userUpdate);
    void changePassword(String username, ChangePasswordRequest request);
    List<UserGet> getAllUsers();
    List<UserGet> getActiveUsers();
    List<String> getAllRoles();
    boolean isActiveUser(String username);
    User createUser(UserCreate userCreate);
    void updateResetPasswordToken(String email, String token);
    void updatePassword(String token, String newPassword);
    void validateAndCleanupToken(String token);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
