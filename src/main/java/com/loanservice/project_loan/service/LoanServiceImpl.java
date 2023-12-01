package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.LoanDTO;
import com.loanservice.project_loan.entity.Loan;
import com.loanservice.project_loan.mapper.LoanMapper;
import com.loanservice.project_loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    @Override
    public List<LoanDTO> getAllLoans() {
        return loanMapper.loansToDtos(loanRepository.findAll());
    }

    @Override
    public Optional<LoanDTO> getLoanById(Long loanId) {
        return loanRepository.findById(loanId).map(loanMapper::loanToLoanDto);
    }

    @Override
    public LoanDTO createOrUpdateLoan(LoanDTO loanDTO) {
        Loan savedLoan = loanRepository.save(loanMapper.loanDtoToLoan(loanDTO));
        return loanMapper.loanToLoanDto(savedLoan);
    }

    @Override
    public void deleteLoan(Long loanId) {
        loanRepository.deleteById(loanId);
    }
}
