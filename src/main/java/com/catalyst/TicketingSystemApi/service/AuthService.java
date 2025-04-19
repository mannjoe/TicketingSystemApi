package com.catalyst.TicketingSystemApi.service;

import com.catalyst.TicketingSystemApi.model.User;

public interface AuthService {
    User authenticateUser(String username, String password);
}
