package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.CustomerDTO;
import com.loanservice.project_loan.entity.Customer;
import com.loanservice.project_loan.mapper.CustomerMapper;
import com.loanservice.project_loan.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.customersToDtos(customerRepository.findAll());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).map(customerMapper::customerToCustomerDto);
    }

    @Override
    public CustomerDTO createOrUpdateCustomer(CustomerDTO customerDTO) {
        Customer savedCustomer = customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO));
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
