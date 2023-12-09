package com.loanservice.project_loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentScheduleDTO {
    private Long loanId;
    private Long customerId;
    private List<String> paymentDates_Amount;
}
