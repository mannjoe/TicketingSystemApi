package com.catalyst.TicketingSystemApi.controller;

import com.catalyst.TicketingSystemApi.dto.TicketDropdownOptionsDto;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketCreate;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketGet;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketUpdate;
import com.catalyst.TicketingSystemApi.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> createTicket(@Valid @RequestBody TicketCreate ticketCreate) {
        return ResponseEntity.ok(ticketService.createTicket(ticketCreate));
    }

    @GetMapping("/statuses")
    public ResponseEntity<?> getAllStatuses() {
        return ResponseEntity.ok(ticketService.getAllStatuses());
    }

    @GetMapping
    public ResponseEntity<List<TicketGet>> getAllTickets() {
        List<TicketGet> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<TicketGet> getTicketByCode(@PathVariable String code) {
        TicketGet ticket = ticketService.getTicketByCode(code);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateTicket(@RequestBody TicketUpdate ticketUpdate) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketUpdate));
    }

    @GetMapping("/{code}/dropdown-options")
    public ResponseEntity<TicketDropdownOptionsDto> getDropdownOptions(
            @PathVariable String code) {
        TicketDropdownOptionsDto options = ticketService.getDropdownOptions(code);
        return ResponseEntity.ok(options);
    }
}
