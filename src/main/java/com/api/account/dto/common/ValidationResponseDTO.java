package com.api.account.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@AllArgsConstructor
public class ValidationResponseDTO {
    private Integer code;
    private HttpStatus status;
    private Map<String, Object> message;
}
