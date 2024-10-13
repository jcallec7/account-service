package com.api.account.dto.account;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountUpdateDTO {

    private String accountNumber;
    @PositiveOrZero
    private Integer accountType;
    @PositiveOrZero
    private BigDecimal balance;
    @PositiveOrZero
    private Long clientId;
    private Boolean status;

}
