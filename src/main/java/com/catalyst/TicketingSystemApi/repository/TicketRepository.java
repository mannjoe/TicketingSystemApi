package com.catalyst.TicketingSystemApi.repository;

import com.catalyst.TicketingSystemApi.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByCode(String code);
}
