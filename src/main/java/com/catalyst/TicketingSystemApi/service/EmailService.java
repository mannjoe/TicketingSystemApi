package com.catalyst.TicketingSystemApi.service;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendResetPasswordEmail(String email, String resetPasswordLink)
            throws MessagingException, UnsupportedEncodingException;
}
