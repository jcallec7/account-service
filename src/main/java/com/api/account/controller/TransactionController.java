package com.api.account.controller;


import com.api.account.dto.transaction.AccountStatementsDTO;
import com.api.account.dto.transaction.TransactionCreateDTO;
import com.api.account.dto.transaction.TransactionResponseDTO;
import com.api.account.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/movimientos")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/crear")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody @Valid TransactionCreateDTO transactionCreateDTO) throws IllegalAccessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(transactionCreateDTO));
    }

    @GetMapping("/reportes/obtenerPorCliente/{clientId}")
    public Mono<ResponseEntity<List<AccountStatementsDTO>>> getTransactionReports(
            @PathVariable Long clientId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {

        return transactionService.getAccountStatements(clientId, startDate, endDate)
                .map(ResponseEntity::ok);
    }

}
