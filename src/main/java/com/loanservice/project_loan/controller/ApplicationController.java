package com.loanservice.project_loan.controller;

import com.loanservice.project_loan.dto.ApplicationDTO;
import com.loanservice.project_loan.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long applicationId) {
        Optional<ApplicationDTO> applicationDTO = applicationService.getApplicationById(applicationId);
        return applicationDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<ApplicationDTO> createApplication(@RequestBody ApplicationDTO applicationDTO) {
        ApplicationDTO savedApplication = applicationService.createApplication(applicationDTO);
        return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.noContent().build();
    }
}