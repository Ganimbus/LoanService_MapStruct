package com.loanservice.project_loan.service;

import java.util.Optional;

public interface BlacklistService {
    boolean isDniInBlacklist(String dni);
    Optional<String> getBlacklistReason(String dni);
}