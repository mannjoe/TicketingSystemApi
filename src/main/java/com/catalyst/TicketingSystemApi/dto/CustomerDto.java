package com.catalyst.TicketingSystemApi.dto;

import com.catalyst.TicketingSystemApi.model.Customer;
import com.catalyst.TicketingSystemApi.model.CustomerType;
import jakarta.validation.constraints.NotBlank;

public class CustomerDto {
    public record CustomerCreate(
            @NotBlank String name,
            @NotBlank String identifierNo,
            @NotBlank CustomerType type
    ) {}

    public record CustomerGet(
            Long id,
            String name,
            String identifierNo,
            CustomerType type,
            String email,
            String phone,
            String address,
            Boolean active
    ) {
        public static CustomerGet fromCustomer(Customer customer) {
            return new CustomerGet(
                    customer.getId(),
                    customer.getName(),
                    customer.getIdentifierNo(),
                    customer.getCustomerType(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.isActive()
            );
        }
    }

    public record CustomerUpdateByAdmin(
            Long id,
            @NotBlank String name,
            @NotBlank String identifierNo,
            @NotBlank CustomerType type,
            String email,
            String phone,
            String address,
            Boolean active
    ) {}
}
