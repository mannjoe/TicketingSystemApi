package com.catalyst.TicketingSystemApi.dto;

import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerGet;
import com.catalyst.TicketingSystemApi.dto.UserDto.UserGet;
import com.catalyst.TicketingSystemApi.model.Ticket;
import com.catalyst.TicketingSystemApi.model.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TicketDto {
    public record TicketCreate(
            @NotNull TicketStatus status,
            @NotNull String dueDate,
            @NotNull Long assignee,
            @NotNull Long reporter,
            @NotNull Long requestBy,
            @NotBlank String title,
            @NotBlank String description
    ) {}

    public record TicketGet(
            String code,
            TicketStatus status,
            LocalDate dueDate,
            UserGet assignee,
            UserGet reporter,
            CustomerGet requestBy,
            String title,
            String description,
            LocalDate createdAtDate
    ) {
        public static TicketGet fromTicket(Ticket ticket) {
            return new TicketGet(
                    ticket.getCode(),
                    ticket.getTicketStatus(),
                    ticket.getDueDate(),
                    ticket.getAssignee() != null ? UserGet.fromUser(ticket.getAssignee()) : null,
                    ticket.getReporter() != null ? UserGet.fromUser(ticket.getReporter()) : null,
                    ticket.getRequestBy() != null ? CustomerGet.fromCustomer(ticket.getRequestBy()) : null,
                    ticket.getTitle(),
                    ticket.getDescription(),
                    ticket.getCreatedAtDate()
            );
        }
    }
    public record TicketUpdate(
            @NotBlank String code,
            @NotNull TicketStatus status,
            @NotNull String dueDate,
            @NotNull Long assignee,
            @NotNull Long reporter,
            @NotNull Long requestBy,
            @NotBlank String title,
            @NotBlank String description
    ) {}
}