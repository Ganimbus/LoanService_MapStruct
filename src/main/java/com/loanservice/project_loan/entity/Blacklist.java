package com.loanservice.project_loan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "BLACK_LIST")
public class Blacklist {
    @Id
    @Column(name = "DNI")
    private String dni;

    @Column(name = "REASON")
    private String reason;
}
