package com.api.account.service.impl;

import com.api.account.dto.account.AccountUpdateDTO;
import com.api.account.dto.transaction.AccountStatementsDTO;
import com.api.account.dto.transaction.TransactionCreateDTO;
import com.api.account.dto.transaction.TransactionResponseDTO;
import com.api.account.enums.TransactionType;
import com.api.account.exception.BadRequestException;
import com.api.account.model.Account;
import com.api.account.model.Transaction;
import com.api.account.repository.AccountRepository;
import com.api.account.repository.TransactionRepository;
import com.api.account.service.AccountService;
import com.api.account.service.MessageService;
import com.api.account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MessageService messageService;
    private final AccountService accountService;
    private final WebClient webClient;

    public TransactionResponseDTO createTransaction(TransactionCreateDTO transactionCreateDTO) throws IllegalAccessException {

        BigDecimal amount = transactionCreateDTO.getAmount();
        Transaction transaction = new Transaction();
        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO();


        if (transactionCreateDTO.getType().getCode().equals(TransactionType.INVALID.getCode())) {
            throw new BadRequestException(messageService.getMessage("invalid.account.type"));
        }

        Account account = accountRepository.findById(transactionCreateDTO.getAccountId()).orElseThrow(
                () -> new BadRequestException(messageService.getMessage("register.not.found"))
        );

        if (transactionCreateDTO.getType().getCode().equals(TransactionType.DEBIT.getCode())) {

            if (amount.signum() > 0) {
                throw new BadRequestException(messageService.getMessage("invalid.transaction.value"));
            }

            if (account.getBalance().compareTo(amount.negate()) < 0) {
                throw new BadRequestException(messageService.getMessage("insufficient.balance"));
            }

        } else {

            if (amount.signum() < 0) {
                throw new BadRequestException(messageService.getMessage("invalid.transaction.value"));

            }

        }

        BigDecimal finalBalance = account.getBalance().add(amount);
        transaction.setAmount(amount);
        transaction.setInitialBalance(account.getBalance());
        transaction.setFinalBalance(finalBalance);
        transaction.setDate(LocalDateTime.now());
        transaction.setType(transactionCreateDTO.getType().getCode());
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        accountUpdateDTO.setBalance(finalBalance);
        accountService.updateAccount(account.getId(), accountUpdateDTO);

        ModelMapper modelMapper = new ModelMapper();

        //Configuración necesaria para setear el id, con esto evitamos ambiguedad
        //y que los datos se seteen por coincidencia o por un mappeo del Mapper
        //por coincidencia de nombres.
        modelMapper.addMappings(new PropertyMap<Transaction, TransactionResponseDTO>() {
            @Override
            protected void configure() {
                map(source.getAccount().getId(), destination.getAccountId());
            }
        });

        return modelMapper.map(transaction, TransactionResponseDTO.class);
    }

    public Mono<List<AccountStatementsDTO>> getAccountStatements(Long clientId, LocalDate startDate, LocalDate endDate) {

        Mono<List<Transaction>> transactionsMono = Mono.defer(() ->
                Mono.justOrEmpty(transactionRepository.findByAccountClientIdAndDateBetween(clientId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
        ).switchIfEmpty(Mono.error(new BadRequestException(messageService.getMessage("register.not.found"))));

        Mono<String> clientNameMono = webClient.get()
                .uri("/api/clientes/obtenerPorId/{id}", clientId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("name"));

        return transactionsMono.zipWith(clientNameMono)
                .map(tuple -> {
                    List<Transaction> transactions = tuple.getT1();
                    String clientName = tuple.getT2();

                    return transactions.stream()
                            .map(transaction -> new AccountStatementsDTO(
                                    transaction.getDate(),
                                    clientName,
                                    transaction.getAccount().getAccountNumber(),
                                    TransactionType.fromCode(transaction.getAccount().getAccountType()).getName(),
                                    transaction.getAccount().getStatus(),
                                    transaction.getInitialBalance(),
                                    transaction.getAmount(),
                                    transaction.getFinalBalance()
                            ))
                            .sorted(Comparator.comparing(AccountStatementsDTO::getDate).reversed())
                            .collect(Collectors.toList());
                });
    }
}
