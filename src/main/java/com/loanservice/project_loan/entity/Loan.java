package com.loanservice.project_loan.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOAN_ID")
    private Long loanId;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID")
    private Application application;

    //from 3 to 60 for personal loans and from 3 to 96 for buisness loans
    @Min(value = 3, message = "Instalment must be greater than 2")
    @Max(value = 96, message = "Instalment must be less than 97")
    @Column(name = "INSTALMENT")
    private int instalment;

    @Positive(message = "Amount must be greater than 0")
    @Column(name = "AMOUNT")
    private BigDecimal amount;


    @Column(name = "INTEREST_RATE")
    private BigDecimal interestRate;

    @Pattern(regexp = "^(simple|compuesto)$", message = "Interest type must be simple or compound")
    @Column(name = "INTEREST_TYPE")
    private String interestType;

    @Pattern(regexp = "^(mensual|trimestral)$", message = "Frequency must be monthly or quarterly")
    @Column(name = "FREQUENCY")
    private String frequency;

    @Column(name = "START_DATE")
    private Date startDate;
}