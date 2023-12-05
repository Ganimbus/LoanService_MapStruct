package com.loanservice.project_loan;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.entity.Application;
import com.loanservice.project_loan.mapper.ApplicationMapper;
import com.loanservice.project_loan.repository.ApplicationRepository;
import com.loanservice.project_loan.service.ApplicationServiceImpl;
import com.loanservice.project_loan.service.BlacklistService;
import com.loanservice.project_loan.service.CustomerService;

import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private BlacklistService blacklistService;

    @Mock
    private CustomerService customerService;

    @Test
    public void testCreateApplication() {
        // Arrange
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setCustomerId(1L);
        applicationDTO.setAmount(new BigDecimal(5000));

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setDni("12345678");
        customerDTO.setSalary(new BigDecimal(10000));
        customerDTO.setCustomerType("persona");

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customerDTO));
        when(blacklistService.isDniInBlacklist("12345678")).thenReturn(false);
        when(applicationRepository.save(any())).thenReturn(new Application());
        when(applicationMapper.applicationToApplicationDto(any())).thenReturn(applicationDTO);

        // Act
        ApplicationDTO result = applicationService.createApplication(applicationDTO);

        // Assert
        assertEquals(applicationDTO, result);
        verify(customerService).getCustomerById(1L);
        verify(blacklistService).isDniInBlacklist("12345678");
        verify(applicationRepository).save(any());
        verify(applicationMapper).applicationToApplicationDto(any());
    }

    @Test
    public void testCreateApplicationWithBlacklistedDni() {
    // Arrange
    ApplicationDTO applicationDTO = new ApplicationDTO();
    applicationDTO.setCustomerId(1L);
    applicationDTO.setAmount(new BigDecimal(5000));

    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setDni("00000000");
    customerDTO.setSalary(new BigDecimal(10000));
    customerDTO.setCustomerType("persona");

    when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customerDTO));
    when(blacklistService.isDniInBlacklist("00000000")).thenReturn(true);

    // Act and Assert
    assertThrows(RuntimeException.class, () -> applicationService.createApplication(applicationDTO));

    verify(customerService).getCustomerById(1L);
    verify(blacklistService).isDniInBlacklist("00000000");
    }
}