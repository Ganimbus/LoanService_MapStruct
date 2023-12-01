package com.loanservice.project_loan.service;

import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.entity.Application;
import com.loanservice.project_loan.mapper.ApplicationMapper;
import com.loanservice.project_loan.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
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
    public ApplicationDTO createOrUpdateApplication(ApplicationDTO applicationDTO) {
        Application savedApplication = applicationRepository.save(applicationMapper.applicationDtoToApplication(applicationDTO));
        return applicationMapper.applicationToApplicationDto(savedApplication);
    }

    @Override
    public void deleteApplication(Long applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}
