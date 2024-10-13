package com.api.account.dto.transaction;

import com.api.account.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionCreateDTO {

    @NotNull
    private TransactionType type;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Long accountId;
}
