package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    Optional<CustomerDTO> getCustomerById(Long customerId);
    CustomerDTO createOrUpdateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
}