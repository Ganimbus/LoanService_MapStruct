package com.loanservice.project_loan;

import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.entity.Application;
import com.loanservice.project_loan.mapper.ApplicationMapper;
import com.loanservice.project_loan.repository.ApplicationRepository;
import com.loanservice.project_loan.service.ApplicationServiceImpl;
import com.loanservice.project_loan.service.BlacklistService;
import com.loanservice.project_loan.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationServiceImpl;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private BlacklistService blacklistService;
    @Mock
    private ApplicationMapper applicationMapper;

    @Test
    public void shouldCreateApplicationSuccessfully() {
        ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .customerId(1L)
                .amount(new BigDecimal(1000))
                .state("aprobado")
                .createDate(new Date())
                .build();

        //Simulate behavior of blacklistService
        when(blacklistService.isDniInBlacklist("00000000")).thenReturn(false);

        //Mock CustomerService behavior
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(CustomerDTO.builder()
                .dni("00000000")
                .salary(new BigDecimal(2000))
                .customerType("persona")
                .build()));

        //Simulate behavior of applicationMapper
        when(applicationMapper.applicationDTOToApplication(any(ApplicationDTO.class))).thenReturn(new Application());

        //Simulate behavior of applicationRepository
        when(applicationRepository.save(any(Application.class))).thenReturn(new Application());

        //Call method createApplication
        ApplicationDTO createApplicationDTO = applicationServiceImpl.createApplication(applicationDTO);

        //Verify that method has created an application successfully
        assertEquals(1L, createApplicationDTO.getCustomerId());
        assertEquals(new BigDecimal(1000), createApplicationDTO.getAmount());
        assertEquals("aprobado", createApplicationDTO.getState());
        assertEquals(new Date(), createApplicationDTO.getCreateDate());
    }
}
