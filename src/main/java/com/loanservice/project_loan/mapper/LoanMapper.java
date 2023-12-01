package com.loanservice.project_loan.mapper;

import com.loanservice.project_loan.dto.LoanDTO;
import com.loanservice.project_loan.entity.Loan;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mappings({
            @Mapping(target = "applicationId", source = "loan.application.applicationId"),
            @Mapping(target = "customerId", source = "loan.customer.customerId")
    })
    LoanDTO loanToLoanDto(Loan loan);

    @InheritInverseConfiguration
    Loan loanDtoToLoan(LoanDTO loanDTO);

    List<LoanDTO> loansToDtos(List<Loan> loans);
}
