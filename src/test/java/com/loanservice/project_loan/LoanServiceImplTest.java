package com.loanservice.project_loan;


import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.dto.LoanDTO;
import com.loanservice.project_loan.entity.Loan;
import com.loanservice.project_loan.mapper.LoanMapper;
import com.loanservice.project_loan.repository.LoanRepository;
import com.loanservice.project_loan.service.ApplicationService;
import com.loanservice.project_loan.service.CustomerService;
import com.loanservice.project_loan.service.LoanServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private CustomerService customerService;

    @Test
    public void testCreateLoan() {
        // Arrange
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setApplicationId(1L);
        loanDTO.setFrequency("mensual");
        loanDTO.setInterestRate(new BigDecimal("0.1"));
        loanDTO.setInterestType("simple");
        loanDTO.setInstalment(12);
        loanDTO.setStartDate(null);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(1L);
        customerDTO.setCustomerType("persona");

        when(applicationService.getApplicationById(1L)).thenReturn(Optional.of(new ApplicationDTO()));
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customerDTO));
        when(loanRepository.save(any())).thenReturn(new Loan());
        when(loanMapper.loanToLoanDto(any())).thenReturn(loanDTO);

        // Act
        LoanDTO result = loanService.createLoan(loanDTO);

        // Assert
        assertEquals(loanDTO, result);
        verify(applicationService).getApplicationById(1L);
        verify(customerService).getCustomerById(1L);
        verify(loanRepository).save(any());
        verify(loanMapper).loanToLoanDto(any());
    }


}
