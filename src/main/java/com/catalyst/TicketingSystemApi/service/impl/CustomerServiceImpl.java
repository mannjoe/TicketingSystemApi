package com.catalyst.TicketingSystemApi.service.impl;

import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerCreate;
import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerGet;
import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerUpdateByAdmin;
import com.catalyst.TicketingSystemApi.exception.ResourceNotFoundException;
import com.catalyst.TicketingSystemApi.model.Customer;
import com.catalyst.TicketingSystemApi.model.CustomerType;
import com.catalyst.TicketingSystemApi.repository.CustomerRepository;
import com.catalyst.TicketingSystemApi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(CustomerCreate customerCreate) {
        Customer customer = new Customer();

        customer.setName(customerCreate.name().trim());
        customer.setIdentifierNo(customerCreate.identifierNo().trim());
        customer.setCustomerType(customerCreate.type());
        customer.setActive(true);

        return customerRepository.save(customer);
    }

    public List<String> getAllTypes() {
        return Arrays.stream(CustomerType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<CustomerGet> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerGet::fromCustomer)
                .collect(Collectors.toList());
    }

    public List<CustomerGet> getActiveCustomers() {
        return customerRepository.findAll().stream()
                .filter(Customer::isActive)
                .map(CustomerGet::fromCustomer)
                .collect(Collectors.toList());
    }

    public CustomerGet getCustomerById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        return customerRepository.findById(id)
                .map(CustomerGet::fromCustomer)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public CustomerGet updateCustomer(CustomerUpdateByAdmin customerUpdate) {
        Long id = customerUpdate.id();
        if (id == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (customerUpdate == null) {
            throw new IllegalArgumentException("Customer update data cannot be null");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setName(customerUpdate.name());
        customer.setIdentifierNo(customerUpdate.identifierNo());
        customer.setCustomerType(customerUpdate.type());
        customer.setEmail(customerUpdate.email());
        customer.setPhone(customerUpdate.phone());
        customer.setAddress(customerUpdate.address());
        customer.setActive(customerUpdate.active());

        Customer updatedCustomer = customerRepository.save(customer);
        return CustomerGet.fromCustomer(updatedCustomer);
    }
}
