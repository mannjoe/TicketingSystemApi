package com.catalyst.TicketingSystemApi.repository;

import com.catalyst.TicketingSystemApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByResetPasswordToken(String token);
    List<User> findByIsActiveTrue();
}
