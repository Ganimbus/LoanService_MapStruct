package com.loanservice.project_loan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @Size(min = 8, max = 8)
    @NotBlank(message = "DNI is mandatory")
    @Column(name = "DNI", unique = true)
    private String dni;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LASTNAME")
    private String lastname;

    @Size(min = 9, max = 9)
    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "ADDRESS")
    private String address;

    @Size(min = 11, max = 11)
    @Column(name = "RUC", unique = true)
    private String ruc;

    @Column(name = "BUSINESS_NAME")
    private String businessName;

    @Pattern(regexp = "^(persona|negocio)$", message = "Customer type must be persona or negocio")
    @Column(name = "CUSTOMER_TYPE")
    private String customerType;

    @Positive(message = "Salary must be greater than 0")
    @Column(name = "SALARY")
    private BigDecimal salary;
}
