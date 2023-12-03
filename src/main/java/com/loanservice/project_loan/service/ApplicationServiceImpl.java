package com.loanservice.project_loan.service;


import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.entity.Application;
import com.loanservice.project_loan.mapper.ApplicationMapper;
import com.loanservice.project_loan.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final BlacklistService blacklistService;
    private final CustomerService customerService;


    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  ApplicationMapper applicationMapper,
                                  BlacklistService blacklistService,
                                  CustomerService customerService) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.blacklistService = blacklistService;
        this.customerService = customerService;
    }

    @Override
    public List<ApplicationDTO> getAllApplications() {
        return applicationMapper.applicationsToDtos(applicationRepository.findAll());
    }

    @Override
    public Optional<ApplicationDTO> getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId).map(applicationMapper::applicationToApplicationDto);
    }

    @Override
    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        Long customerId = applicationDTO.getCustomerId();

        CustomerDTO customerDTO = customerService.getCustomerById(customerId)
                .orElseThrow(() ->new RuntimeException("Customer not found"));
        String customerDni = customerDTO.getDni();
        BigDecimal customerSalary = customerDTO.getSalary();
        String customerType = customerDTO.getCustomerType();

        //Verify if DNI is in the blacklist
       if (blacklistService.isDniInBlacklist(customerDni)) {
                throw new RuntimeException("Customer is in the blacklist");
            }

       //Verify amount of the application
        BigDecimal loanApplicationAmount = applicationDTO.getAmount();

       BigDecimal minAmount = ("persona".equals(customerType)) ? new BigDecimal(1000) : new BigDecimal(20000);
       BigDecimal maxAmount = ("persona".equals(customerType)) ? customerSalary.multiply(new BigDecimal(10)) : customerSalary.multiply(new BigDecimal(5));
       if(loanApplicationAmount.compareTo(minAmount) < 0 || loanApplicationAmount.compareTo(maxAmount) > 0){
           applicationDTO.setState("rechazado");
       } else {
           applicationDTO.setState("aprobado");

       }
        applicationDTO.setCreateDate(new Date());
        Application savedApplication = applicationRepository.save(applicationMapper.applicationDtoToApplication(applicationDTO));
        return applicationMapper.applicationToApplicationDto(savedApplication);
    }

    @Override
    public void deleteApplication(Long applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}
