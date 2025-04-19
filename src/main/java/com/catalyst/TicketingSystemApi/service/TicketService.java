package com.catalyst.TicketingSystemApi.service;

import com.catalyst.TicketingSystemApi.dto.TicketDropdownOptionsDto;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketCreate;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketGet;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketUpdate;

import java.util.List;

public interface TicketService {
    List<String> getAllStatuses();
    List<TicketGet> getAllTickets();
    TicketGet getTicketByCode(String code);
    TicketGet createTicket(TicketCreate ticketCreate);
    TicketGet updateTicket(TicketUpdate ticketUpdate);
    TicketDropdownOptionsDto getDropdownOptions(String code);
}
