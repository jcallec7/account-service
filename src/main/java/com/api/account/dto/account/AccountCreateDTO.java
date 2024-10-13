package com.api.account.dto.account;

import com.api.account.enums.AccountType;
import jakarta.validation.constraints.*;
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
    @Digits(integer = 7, fraction = 2)
    private BigDecimal balance;
    @NotNull
    @Positive
    private Long clientId;

}
