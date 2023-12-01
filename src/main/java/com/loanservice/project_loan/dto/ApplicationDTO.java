package com.loanservice.project_loan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {
    private Long applicationId;
    private Long employeeId;
    private Long customerId;
    private BigDecimal amount;
    private String state;
    private Date createDate;
}