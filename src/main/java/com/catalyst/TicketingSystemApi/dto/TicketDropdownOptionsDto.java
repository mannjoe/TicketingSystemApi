package com.catalyst.TicketingSystemApi.dto;

import com.catalyst.TicketingSystemApi.dto.UserDto.UserGet;
import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerGet;
import java.util.List;

public record TicketDropdownOptionsDto(
        List<UserGet> assigneeOptions,
        List<UserGet> reporterOptions,
        List<CustomerGet> requestByOptions
) {
}
