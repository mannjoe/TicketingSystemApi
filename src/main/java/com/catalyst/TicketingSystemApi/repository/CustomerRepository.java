package com.catalyst.TicketingSystemApi.repository;

import com.catalyst.TicketingSystemApi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByIsActiveTrue();
}
