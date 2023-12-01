package com.loanservice.project_loan.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long customerId;
    private String dni;
    private String name;
    private String lastname;
    private String telephone;
    private String address;
    private String ruc;
    private String businessName;
    private String customerType;
    private BigDecimal salary;
}
