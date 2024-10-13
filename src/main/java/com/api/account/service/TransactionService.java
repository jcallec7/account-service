package com.api.account.service;

import com.api.account.dto.transaction.AccountStatementsDTO;
import com.api.account.dto.transaction.TransactionCreateDTO;
import com.api.account.dto.transaction.TransactionResponseDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    TransactionResponseDTO createTransaction(TransactionCreateDTO transactionCreateDTO) throws IllegalAccessException;
    Mono<List<AccountStatementsDTO>> getAccountStatements(Long clientId, LocalDate startDate, LocalDate endDate);
}
