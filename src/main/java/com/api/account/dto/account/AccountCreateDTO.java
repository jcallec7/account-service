package com.api.account.dto.account;

import com.api.account.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreateDTO {

    @NotBlank
    private String accountNumber;
    @NotNull
    private AccountType accountType;
    @NotNull
    @PositiveOrZero
    private BigDecimal balance;
    @NotNull
    @Positive
    private Long clientId;

}
