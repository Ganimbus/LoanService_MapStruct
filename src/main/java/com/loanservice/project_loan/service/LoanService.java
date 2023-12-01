package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.LoanDTO;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> getAllLoans();
    Optional<LoanDTO> getLoanById(Long loanId);
    LoanDTO createOrUpdateLoan(LoanDTO loanDTO);
    void deleteLoan(Long loanId);
}
