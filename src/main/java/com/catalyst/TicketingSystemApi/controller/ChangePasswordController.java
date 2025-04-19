package com.catalyst.TicketingSystemApi.controller;

import com.catalyst.TicketingSystemApi.dto.AuthDto;
import com.catalyst.TicketingSystemApi.service.EmailService;
import com.catalyst.TicketingSystemApi.service.UserService;
import com.catalyst.TicketingSystemApi.util.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ChangePasswordController {
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {

        String email = requestBody.get("email");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Email is required."));
        }

        try {
            String token = RandomString.make(45);
            userService.updateResetPasswordToken(email, token);
            String resetPasswordLink = UrlUtil.getClientOriginUrl(request) + "/reset-password?token=" + token;
            emailService.sendResetPasswordEmail(email, resetPasswordLink);

            return ResponseEntity.ok()
                    .body(Collections.singletonMap("message",
                            "If an account exists for this email, a password reset link has been sent."));

        } catch (UnsupportedEncodingException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error while sending email."));
        }
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        String newPassword = requestBody.get("newPassword");
        String confirmPassword = requestBody.get("confirmPassword");

        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Token is required."));
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "New password is required."));
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Passwords do not match."));
        }

        try {
            userService.updatePassword(token, newPassword);
            return ResponseEntity.ok()
                    .body(Collections.singletonMap("message", "Your password has been successfully reset."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Invalid or expired token."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("message", "Failed to reset password. Please try again."));
        }
    }

    @PutMapping("/change-password/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable String username,
            @RequestBody AuthDto.ChangePasswordRequest request
    ) {
        try {
            userService.changePassword(username, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Your password has been successfully reset.");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalArgumentException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
