package com.loanservice.project_loan.mapper;

import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.entity.Customer;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDto(Customer customer);

    List<CustomerDTO> customersToDtos(List<Customer> customers);
}
