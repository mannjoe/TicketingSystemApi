package com.catalyst.TicketingSystemApi.controller;

import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerCreate;
import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerGet;
import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerUpdateByAdmin;
import com.catalyst.TicketingSystemApi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerCreate customerCreate) {
        return ResponseEntity.ok(customerService.createCustomer(customerCreate));
    }

    @GetMapping("/types")
    public ResponseEntity<?> getAllTypes() {
        return ResponseEntity.ok(customerService.getAllTypes());
    }

    @GetMapping
    public ResponseEntity<List<CustomerGet>> getAllCustomers() {
        List<CustomerGet> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerGet>> getActiveCustomers() {
        List<CustomerGet> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<CustomerGet> getCustomerById(@PathVariable long id) {
        CustomerGet customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody CustomerUpdateByAdmin customerUpdate) {
        return ResponseEntity.ok(customerService.updateCustomer(customerUpdate));
    }
}
