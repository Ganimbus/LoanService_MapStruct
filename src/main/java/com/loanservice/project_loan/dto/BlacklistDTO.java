package com.loanservice.project_loan.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BlacklistDTO {
 private String dni;
 private String reason;

}
