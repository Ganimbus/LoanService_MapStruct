package com.loanservice.project_loan.service;

import com.loanservice.project_loan.entity.Blacklist;
import com.loanservice.project_loan.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;
    @Autowired
    public BlacklistServiceImpl(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    public boolean isDniInBlacklist(String dni){
        return blacklistRepository.existsById(dni);
    }

    @Override
    public Optional<String> getBlacklistReason(String dni){
        return blacklistRepository.findById(dni).map(Blacklist::getReason);
    }
}
