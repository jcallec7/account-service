package com.api.account.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq")
    @SequenceGenerator(name = "accounts_seq", sequenceName = "accounts_seq", allocationSize = 1)
    private Long id;
    @Column(unique = true, nullable = false)
    private String accountNumber;
    private Integer accountType;
    private BigDecimal balance;
    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean status = true;
    private Long clientId;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

}
