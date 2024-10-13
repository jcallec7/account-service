package com.api.account.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccountStatementsDTO {

    private LocalDateTime date;
    private String client;
    private String accountNumber;
    private String accountType;
    private Boolean accountStatus;
    private BigDecimal initialBalance;
    private BigDecimal amount;
    private BigDecimal finalBalance;

}
