package com.api.account.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {

    private Long id;
    private LocalDateTime date;
    private Integer type;
    private BigDecimal initialBalance;
    private BigDecimal amount;
    private BigDecimal finalBalance;
    private Long accountId;

}
