package com.api.account.dto.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponseDTO {

    private Long id;
    private String accountNumber;
    private Integer accountType;
    private BigDecimal balance;
    private Long clientId;

}
