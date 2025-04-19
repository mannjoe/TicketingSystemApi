package com.catalyst.TicketingSystemApi.service;

import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerCreate;
import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerGet;
import com.catalyst.TicketingSystemApi.dto.CustomerDto.CustomerUpdateByAdmin;
import com.catalyst.TicketingSystemApi.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(CustomerCreate customerCreate);
    List<String> getAllTypes();
    List<CustomerGet> getAllCustomers();
    List<CustomerGet> getActiveCustomers();
    CustomerGet getCustomerById(Long id);
    CustomerGet updateCustomer(CustomerUpdateByAdmin customerUpdate);
}
