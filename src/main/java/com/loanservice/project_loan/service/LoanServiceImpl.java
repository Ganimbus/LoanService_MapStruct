package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.dto.LoanDTO;
import com.loanservice.project_loan.entity.Loan;
import com.loanservice.project_loan.mapper.LoanMapper;
import com.loanservice.project_loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final ApplicationService applicationService;
    private final CustomerService customerService;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository,
                           LoanMapper loanMapper,
                           ApplicationService applicationService,
                           CustomerService customerService) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.applicationService = applicationService;
        this.customerService = customerService;
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
    public LoanDTO createLoan(LoanDTO loanDTO) {
        //Obtain applicationId
        Long applicationId = loanDTO.getApplicationId();
        Long customerId = loanDTO.getCustomerId();
        ApplicationDTO applicationDTO = applicationService.getApplicationById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        CustomerDTO customerDTO = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        String applicationState = applicationDTO.getState();

        if (!"aprobado".equals(applicationState)) {
            throw new RuntimeException("Application is not approved");
        }

        String frequency = loanDTO.getFrequency();
        String customerType = customerDTO.getCustomerType();
        //Check interest type and rate based on customer type
        String interstType = loanDTO.getInterestType();
        BigDecimal interestRate = loanDTO.getInterestRate();
        validateInterestTypeAndRate(customerType, interstType, interestRate, frequency);

        //Check frequency and adjust instalment range based on customer type
        int instalment = loanDTO.getInstalment();
        validateFrequencyAndInstalment(customerType, frequency, instalment);

        Loan savedLoan = loanRepository.save(loanMapper.loanDtoToLoan(loanDTO));
        return loanMapper.loanToLoanDto(savedLoan);
    }

    @Override
    public void deleteLoan(Long loanId) {
        loanRepository.deleteById(loanId);
    }

    private void validateInterestTypeAndRate(String customerType, String interestType, BigDecimal interestRate, String frequency) {
        //Check interest type and rate based on customer type
        if ("persona".equals(customerType)) {
            if (!"simple".equals(interestType) ||
                    interestRate.compareTo(BigDecimal.valueOf(0.1)) < 0 ||
                    interestRate.compareTo(BigDecimal.valueOf(0.15)) > 0) {
                throw new RuntimeException("Persona customers can only have simple interest between 10% and 15%");
            }
        } else if ("negocio".equals(customerType)) {
            //Check interest rate based on interest type
            if ("simple".equals(interestType)) {
                if (interestRate.compareTo(BigDecimal.valueOf(0.1)) < 0 &&
                        interestRate.compareTo(BigDecimal.valueOf(0.12)) > 0) {
                    throw new RuntimeException("Negocio customers can only have simple interest between 10% and 12%");
                }
            } else if ("compuesto".equals(interestType)) {
                //Check interest rate based on frequency
                //Calculate equivalent interest rate for mensual frequency
                if ("mensual".equals(frequency)){
                    if (interestRate.compareTo(BigDecimal.valueOf(0.07)) < 0 &&
                            interestRate.compareTo(BigDecimal.valueOf(0.1)) > 0) {
                        throw new RuntimeException("Negocio customers can only have compuesto interest between 7% and 10% with mensual frequency");
                    }
                } else if ("trimestral".equals(frequency)) {
                    //Calculate equivalent interest rate for trimestral frequency
                    if (interestRate.compareTo(BigDecimal.valueOf(0.20)) < 0 &&
                            interestRate.compareTo(BigDecimal.valueOf(0.3)) > 0) {
                        throw new RuntimeException("Negocio customers can only have compuesto interest between 20% and 30% with trimestral frequency");
                    }
                }
            }
        }
    }

    private void validateFrequencyAndInstalment(String customerType, String frequency, int instalment) {
        if ("persona".equals(customerType)) {
            if (!"mensual".equals(frequency) || instalment < 3 || instalment > 36) {
                throw new RuntimeException("Invalid frequency or instalment for persona");
            }
        }else if ("negocio".equals(customerType)) {
            int instalmentRange = ("mensual".equals(frequency))? 96 : ("trimestral".equals(frequency))? 32 : -1;
            if((instalment < 3 || instalment > instalmentRange)){
                throw new RuntimeException("Invalid instalment for negocio with frequency " + frequency);
            }
        }
    }
}
