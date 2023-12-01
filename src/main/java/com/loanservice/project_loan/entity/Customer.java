package com.loanservice.project_loan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "CUSTOMER")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "DNI")
    private String dni;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "RUC")
    private String ruc;

    @Column(name = "BUSINESS_NAME")
    private String businessName;

    @Column(name = "CUSTOMER_TYPE")
    private String customerType;

    @Column(name = "SALARY")
    private BigDecimal salary;
}
