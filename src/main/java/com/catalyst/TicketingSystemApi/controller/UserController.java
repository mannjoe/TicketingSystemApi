package com.catalyst.TicketingSystemApi.controller;

import com.catalyst.TicketingSystemApi.dto.UserDto.UserCreate;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserGet;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserUpdateByAdmin;
import com.catalyst.TicketingSystemApi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreate userCreate) {
        return ResponseEntity.ok(userService.createUser(userCreate));
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }

    @GetMapping
    public ResponseEntity<List<UserGet>> getAllUsers() {
        List<UserGet> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserGet>> getActiveUsers() {
        List<UserGet> users = userService.getActiveUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<UserGet> getUserById(@PathVariable Long id) {
        UserGet user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserGet> getUserByUsername(@PathVariable String username) {
        UserGet user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @Valid @RequestBody UserUpdateByAdmin userUpdate) {
        return ResponseEntity.ok(userService.updateUser(username, userUpdate));
    }
}
