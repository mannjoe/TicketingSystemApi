package com.catalyst.TicketingSystemApi.service.impl;

import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerGet;
import com.catalyst.TicketingSystemApi.dto.TicketDropdownOptionsDto;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketCreate;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketGet;
import com.catalyst.TicketingSystemApi.dto.TicketDto.TicketUpdate;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserGet;
import com.catalyst.TicketingSystemApi.exception.ResourceNotFoundException;
import com.catalyst.TicketingSystemApi.model.Customer;
import com.catalyst.TicketingSystemApi.model.Ticket;
import com.catalyst.TicketingSystemApi.model.TicketStatus;
import com.catalyst.TicketingSystemApi.model.User;
import com.catalyst.TicketingSystemApi.repository.CustomerRepository;
import com.catalyst.TicketingSystemApi.repository.TicketRepository;
import com.catalyst.TicketingSystemApi.repository.UserRepository;
import com.catalyst.TicketingSystemApi.service.TicketService;
import com.catalyst.TicketingSystemApi.util.DateParserUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public List<String> getAllStatuses() {
        return Arrays.stream(TicketStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<TicketGet> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(TicketGet::fromTicket)
                .collect(Collectors.toList());
    }

    public TicketGet getTicketByCode(String code) {
        Ticket ticket = ticketRepository.findByCode(code);
        if (ticket == null) {
            throw new ResourceNotFoundException("Ticket not found with code: " + code);
        }
        return TicketGet.fromTicket(ticket);
    }

    public TicketGet createTicket(TicketCreate ticketCreate) {
        Ticket ticket = new Ticket();
        ticket.setCode(generateTicketCode());
        ticket.setTicketStatus(ticketCreate.status());
        ticket.setDueDate(DateParserUtil.parseToLocalDate(ticketCreate.dueDate()));
        ticket.setTitle(ticketCreate.title());
        ticket.setDescription(ticketCreate.description());
        ticket.setAssignee(userRepository.findById(ticketCreate.assignee())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found with id: " + ticketCreate.assignee())));

        ticket.setReporter(userRepository.findById(ticketCreate.reporter())
                .orElseThrow(() -> new EntityNotFoundException("Reporter not found with id: " + ticketCreate.reporter())));

        ticket.setRequestBy(customerRepository.findById(ticketCreate.requestBy())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + ticketCreate.requestBy())));

        ticketRepository.save(ticket);

        return getTicketByCode(ticket.getCode());

    }

    public TicketGet updateTicket(TicketUpdate ticketUpdate) {
        String code = ticketUpdate.code();
        if (code == null) {
            throw new IllegalArgumentException("Ticket code cannot be null");
        }

        Ticket ticket = ticketRepository.findByCode(code);
        ticket.setTicketStatus(ticketUpdate.status());
        ticket.setDueDate(DateParserUtil.parseToLocalDate(ticketUpdate.dueDate()));
        ticket.setTitle(ticketUpdate.title());
        ticket.setDescription(ticketUpdate.description());
        ticket.setAssignee(userRepository.findById(ticketUpdate.assignee())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found with id: " + ticketUpdate.assignee())));
        ticket.setReporter(userRepository.findById(ticketUpdate.reporter())
                .orElseThrow(() -> new EntityNotFoundException("Reporter not found with id: " + ticketUpdate.reporter())));

        ticket.setRequestBy(customerRepository.findById(ticketUpdate.requestBy())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + ticketUpdate.requestBy())));

        ticketRepository.save(ticket);

        return TicketGet.fromTicket(ticket);
    }

    private String generateTicketCode() {
        int currentYear = LocalDate.now().getYear();
        long nano = System.nanoTime();
        String uniquePart = String.format("%04d", nano % 10000);
        return "TICK-" + currentYear + "-" + uniquePart;
    }

    public TicketDropdownOptionsDto getDropdownOptions(String code) {
        // 1. Find the ticket or throw exception
        Ticket ticket = ticketRepository.findByCode(code);
        if (ticket == null) {
            throw new ResourceNotFoundException("Ticket not found with code: " + code);
        }

        // 2. Get active users and current assignee/reporter
        List<User> activeUsers = userRepository.findByIsActiveTrue();
        User currentAssignee = userRepository.findById(ticket.getAssignee().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        User currentReporter = userRepository.findById(ticket.getReporter().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporter not found"));

        // 3. Get active customers and current requestBy
        List<Customer> activeCustomers = customerRepository.findByIsActiveTrue();
        Customer currentRequestBy = customerRepository.findById(ticket.getRequestBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("RequestBy customer not found"));

        // 4. Combine and convert to DTOs
        return new TicketDropdownOptionsDto(
                combineAndConvertUsers(activeUsers, currentAssignee),
                combineAndConvertUsers(activeUsers, currentReporter),
                combineAndConvertCustomers(activeCustomers, currentRequestBy)
        );
    }

    private List<UserGet> combineAndConvertUsers(List<User> activeUsers, User currentUser) {
        // Ensure current user is included even if inactive
        boolean containsCurrent = activeUsers.stream()
                .anyMatch(u -> u.getId().equals(currentUser.getId()));

        List<User> combined = new ArrayList<>(activeUsers);
        if (!containsCurrent) {
            combined.addFirst(currentUser);
        }

        return combined.stream()
                .map(UserGet::fromUser)
                .collect(Collectors.toList());
    }

    private List<CustomerGet> combineAndConvertCustomers(List<Customer> activeCustomers, Customer currentCustomer) {
        // Ensure current customer is included even if inactive
        boolean containsCurrent = activeCustomers.stream()
                .anyMatch(c -> c.getId().equals(currentCustomer.getId()));

        List<Customer> combined = new ArrayList<>(activeCustomers);
        if (!containsCurrent) {
            combined.addFirst(currentCustomer);
        }

        return combined.stream()
                .map(CustomerGet::fromCustomer)
                .collect(Collectors.toList());
    }
}
