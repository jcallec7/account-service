package com.api.account.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {

    INVALID(0, "Código invalido"),
    CREDIT(1, "Credito"),
    DEBIT(2, "Debito");

    private final Integer code;
    private final String name;

    @JsonValue
    public Integer getCode() {
        return code;
    }

    @JsonCreator
    public static TransactionType fromCode(Integer code) {

        for (TransactionType type : TransactionType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return TransactionType.INVALID;
    }
}
