package com.catalyst.TicketingSystemApi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "identifier_no", unique = true, nullable = false)
    private String identifierNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CustomerType customerType;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @Column(name = "created_at_date", updatable = false)
    private LocalDate createdAtDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @PrePersist
    protected void onCreate() {
        this.createdAtDate = LocalDate.now();
    }

    @OneToMany(mappedBy = "requestBy", fetch = FetchType.LAZY)
    private List<Ticket> requestedTickets;
}