package com.loanservice.project_loan.repository;

import com.loanservice.project_loan.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<BlackList, String> {
}