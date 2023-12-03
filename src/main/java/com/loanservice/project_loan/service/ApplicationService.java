package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.ApplicationDTO;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<ApplicationDTO> getAllApplications();
    Optional<ApplicationDTO> getApplicationById(Long applicationId);
    ApplicationDTO createApplication(ApplicationDTO applicationDTO);
    void deleteApplication(Long applicationId);
}