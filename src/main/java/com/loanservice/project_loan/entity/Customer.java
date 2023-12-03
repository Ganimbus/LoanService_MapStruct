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

    //@NotBlank(message = "Name is mandatory")
    @Column(name = "NAME")
    private String name;

    //@NotBlank(message = "Lastname is mandatory")
    @Column(name = "LASTNAME")
    private String lastname;

    @Size(min = 9, max = 9)
    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "ADDRESS")
    private String address;

    //@NotBlank(message = "RUC is mandatory")
    @Size(min = 11, max = 11)
    @Column(name = "RUC", unique = true)
    private String ruc;

    //@NotBlank(message = "Business name is mandatory")
    @Column(name = "BUSINESS_NAME")
    private String businessName;

    @Pattern(regexp = "^(persona|negocio)$", message = "Customer type must be persona or negocio")
    @Column(name = "CUSTOMER_TYPE")
    private String customerType;

    @Positive(message = "Salary must be greater than 0")
    @Column(name = "SALARY")
    private BigDecimal salary;

    @AssertTrue(message = "Name and Last name should be empty for a business customer")
    public boolean isNameAndLastnameEmptyForBusinessCustomer(){
        return !"negocio".equals(customerType) || (name == null && lastname == null);
    }

    @AssertTrue(message = "RUC and Business name should be empty for a person customer")
    public boolean isRucAndBusinessNameEmptyForPersonCustomer(){
        return !"persona".equals(customerType) || (ruc == null && businessName == null);
    }
}
