package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.dto.LoanDTO;
import com.loanservice.project_loan.dto.PaymentScheduleDTO;
import com.loanservice.project_loan.entity.Loan;
import com.loanservice.project_loan.mapper.LoanMapper;
import com.loanservice.project_loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

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

        loanDTO.setAmount(applicationDTO.getAmount());
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

    @Override
    public PaymentScheduleDTO getPaymentSchedule(Long loanId){
        Optional<LoanDTO> optionalLoanDTO = getLoanById(loanId);
        if (optionalLoanDTO.isEmpty()){
            throw new RuntimeException("Loan not found");
        }

        LoanDTO loanDTO = optionalLoanDTO.get();
        List<String> paymentDates_Amount = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loanDTO.getStartDate());

        BigDecimal paymentAmount;
            if ("simple".equals(loanDTO.getInterestType())){
                paymentAmount = calculateSimpleInterestPayment(loanDTO.getAmount(),
                        loanDTO.getInterestRate(),
                        loanDTO.getInstalment());
            } else {
                paymentAmount = calculateCompoundInterestPayment(loanDTO.getAmount(),
                        loanDTO.getInterestRate(),
                        loanDTO.getInstalment());
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String paymentDate = sdf.format(calendar.getTime());
            paymentDates_Amount.add(paymentDate + "-" + paymentAmount);

            for (int i = 1; i < loanDTO.getInstalment(); i++) {
                if ("mensual".equals(loanDTO.getFrequency())){
                    calendar.add(Calendar.MONTH, 1);
                } else if ("trimestral".equals(loanDTO.getFrequency())){
                    calendar.add(Calendar.MONTH, 3);
                }
                paymentDate = sdf.format(calendar.getTime());
                paymentDates_Amount.add(paymentDate + "-" + paymentAmount);
            }
        return new PaymentScheduleDTO(loanId, loanDTO.getCustomerId(), paymentDates_Amount);
    }

    private BigDecimal calculateSimpleInterestPayment(BigDecimal principal, BigDecimal interestRate, int period){
        BigDecimal interest = principal.multiply(interestRate);
        return principal.divide(BigDecimal.valueOf(period), RoundingMode.HALF_UP).add(interest).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateCompoundInterestPayment(BigDecimal principal, BigDecimal interestRate, int period){
        BigDecimal compoundFactor = BigDecimal.ONE.add(interestRate).pow(period);
        return principal.multiply(compoundFactor).divide(BigDecimal.valueOf(period), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    private void validateInterestTypeAndRate(String customerType, String interestType, BigDecimal interestRate, String frequency) {
        //Check interest type and rate based on customer type
        if ("persona".equals(customerType)) {
            if (!"simple".equals(interestType) ||
                    interestRate.compareTo(BigDecimal.valueOf(0.016)) < 0 ||
                    interestRate.compareTo(BigDecimal.valueOf(0.036)) > 0) {
                throw new RuntimeException("Persona customers can only have simple interest between 1.6% and 3.6%");
            }
        } else if ("negocio".equals(customerType)) {
            //Check interest rate based on interest type
            if ("simple".equals(interestType)) {
                if ("mensual".equals(frequency)) {
                    if (interestRate.compareTo(BigDecimal.valueOf(0.01)) < 0 ||
                            interestRate.compareTo(BigDecimal.valueOf(0.04)) > 0) {
                        throw new RuntimeException("Negocio customers can only have simple interest between 1% and 4% with mensual frequency");
                    }
                } else if ("trimestral".equals(frequency)) {
                    if (interestRate.compareTo(BigDecimal.valueOf(0.03)) < 0 ||
                            interestRate.compareTo(BigDecimal.valueOf(0.15)) > 0) {
                        throw new RuntimeException("Negocio customers can only have simple interest between 3% and 15% with trimestral frequency");
                    }
                }
            } else if ("compuesto".equals(interestType)) {
                //Check interest rate based on frequency
                //Calculate equivalent interest rate for mensual frequency
                if ("mensual".equals(frequency)){
                    if (interestRate.compareTo(BigDecimal.valueOf(0.008)) < 0 &&
                            interestRate.compareTo(BigDecimal.valueOf(0.04)) > 0) {
                        throw new RuntimeException("Negocio customers can only have compuesto interest between 0.8% and 4% with mensual frequency");
                    }
                } else if ("trimestral".equals(frequency)) {
                    //Calculate equivalent interest rate for trimestral frequency
                    if (interestRate.compareTo(BigDecimal.valueOf(0.04)) < 0 &&
                            interestRate.compareTo(BigDecimal.valueOf(0.18)) > 0) {
                        throw new RuntimeException("Negocio customers can only have compuesto interest between 4% and 18% with trimestral frequency");
                    }
                }
            }
        }
    }

    private void validateFrequencyAndInstalment(String customerType, String frequency, int instalment) {
        if ("persona".equals(customerType)) {
            if (!"mensual".equals(frequency) || instalment < 3 || instalment > 60) {
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
