package com.loanservice.project_loan.mapper;

import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.entity.Application;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mappings({
            @Mapping(target = "customerId", source = "application.customer.customerId"),
            @Mapping(target = "employeeId", source = "application.employee.employeeId")
    })
    ApplicationDTO applicationToApplicationDto(Application application);

    @InheritInverseConfiguration
    Application applicationDtoToApplication(ApplicationDTO applicationDTO);

    List<ApplicationDTO> applicationsToDtos(List<Application> applications);
}
