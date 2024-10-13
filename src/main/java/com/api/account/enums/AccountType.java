package com.api.account.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    INVALID(0, "Código invalido"),
    SAVINGS(1, "Cuenta de Ahorros"),
    CURRENT(2, "Cuenta Corriente");

    private final Integer code;
    private final String name;

    @JsonValue
    public Integer getCode() {
        return code;
    }

    @JsonCreator
    public static AccountType fromCode(Integer code) {

        for (AccountType type : AccountType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return AccountType.INVALID;
    }

}
